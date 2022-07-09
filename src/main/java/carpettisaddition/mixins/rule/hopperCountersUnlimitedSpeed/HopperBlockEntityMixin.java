package carpettisaddition.mixins.rule.hopperCountersUnlimitedSpeed;

import carpet.CarpetSettings;
import carpet.utils.WoolTool;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 11700
//$$ import net.minecraft.block.BlockState;
//$$ import java.util.function.BooleanSupplier;
//#else
import java.util.function.Supplier;
//#endif

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends LootableContainerBlockEntity
{
	private static final int OPERATION_LIMIT = Short.MAX_VALUE;

	//#if MC >= 11700
	//$$ protected HopperBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState)
	//$$ {
	//$$ 	super(blockEntityType, blockPos, blockState);
	//$$ }
	//#else
	public HopperBlockEntityMixin()
	{
		super(BlockEntityType.HOPPER);
	}
	//#endif

	//#if MC >= 11700
 //$$
	//$$ @Shadow
	//$$ private static boolean insert(World world, BlockPos blockPos, BlockState blockState, Inventory inventory)
	//$$ {
	//$$ 	return false;
	//$$ }
	//#else
	@Shadow protected abstract boolean insert();
	@Shadow protected abstract boolean isFull();
	@Shadow public abstract double getHopperX();
	@Shadow public abstract double getHopperY();
	@Shadow public abstract double getHopperZ();
	@Shadow protected abstract void setCooldown(int cooldown);
	//#endif

	@Inject(
			method = "insertAndExtract",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11800
					//$$ target = "Lnet/minecraft/block/entity/HopperBlockEntity;setTransferCooldown(I)V",
					//#else
					target = "Lnet/minecraft/block/entity/HopperBlockEntity;setCooldown(I)V",
					//#endif
					shift = At.Shift.AFTER
			)
	)
	//#if MC >= 11700
	//$$ private static void doHopperCountersUnlimitedSpeed(World world, BlockPos blockPos, BlockState blockState, HopperBlockEntity hopperBlockEntity, BooleanSupplier booleanSupplier, CallbackInfoReturnable<Boolean> cir)
	//#else
	private void doHopperCountersUnlimitedSpeed(Supplier<Boolean> extractMethod, CallbackInfoReturnable<Boolean> cir)
	//#endif
	{
		if (CarpetSettings.hopperCounters && CarpetTISAdditionSettings.hopperCountersUnlimitedSpeed)
		{
			// hopper counter check
			//#if MC >= 11700
			//$$ DyeColor wool_color = WoolTool.getWoolColorAtPosition(world, hopperBlockEntity.getPos().offset(hopperBlockEntity.getCachedState().get(HopperBlock.FACING)));
			//#else
			World world = getWorld();
			if (world == null)
			{
				return;
			}
			DyeColor wool_color = WoolTool.getWoolColorAtPosition(world, new BlockPos(getHopperX(), getHopperY(), getHopperZ()).offset(this.getCachedState().get(HopperBlock.FACING)));
			//#endif

			if (wool_color == null)
			{
				return;
			}
			// hopper counter check ends

			// unlimited speed
			for (int i = OPERATION_LIMIT - 1; i >= 0; i--)
			{
				boolean flag = false;

				// vanilla copy starts

				//#if MC >= 11700
				//$$ if (!hopperBlockEntity.isEmpty())
				//$$ {
				//$$ 	flag = insert(world, blockPos, blockState, hopperBlockEntity);
				//$$ }
				//$$ if (!((HopperBlockEntityAccessor)hopperBlockEntity).invokeIsFull())
				//$$ {
				//$$ 	flag |= booleanSupplier.getAsBoolean();
				//$$ }
				//#else
				if (!this.isInvEmpty())
				{
					flag = this.insert();
				}
				if (!this.isFull())
				{
					flag |= (Boolean)extractMethod.get();
				}
				//#endif
				// vanilla copy ends

				if (!flag)
				{
					break;
				}
				if (i == 0)
				{
					BlockPos hopperPos =
							//#if MC >= 11700
							//$$ hopperBlockEntity.getPos();
							//#else
							new BlockPos(getHopperX(), getHopperY(), getHopperZ());
							//#endif

					CarpetTISAdditionServer.LOGGER.warn(String.format("Hopper at %s exceeded hopperCountersUnlimitedSpeed operation limit %d", hopperPos, OPERATION_LIMIT));
				}
			}

			// no cooldown
			//#if MC >= 11700
			//$$ ((HopperBlockEntityAccessor)hopperBlockEntity).invokeSetCooldown(0);
			//#else
			this.setCooldown(0);
			//#endif
		}
	}

	// to avoid repeatedly extraction an item entity in onEntityCollided
	@Inject(
			method = "extract(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/entity/ItemEntity;)Z",
			at = @At(value = "HEAD"),
			cancellable = true
	)
	private static void dontExtractRemovedItem(Inventory inventory, ItemEntity itemEntity, CallbackInfoReturnable<Boolean> cir)
	{
		if (CarpetTISAdditionSettings.hopperCountersUnlimitedSpeed)
		{
			if (
					//#if MC >= 11700
					//$$ itemEntity.isRemoved()
					//#else
					itemEntity.removed
					//#endif
			)
			{
				cir.setReturnValue(false);
			}
		}
	}
}

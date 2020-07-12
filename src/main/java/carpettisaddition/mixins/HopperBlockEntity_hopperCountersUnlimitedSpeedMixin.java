package carpettisaddition.mixins;

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

import java.util.function.Supplier;


@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntity_hopperCountersUnlimitedSpeedMixin extends LootableContainerBlockEntity
{
	private static final int OPERATION_LIMIT = Short.MAX_VALUE;

	@Shadow protected abstract boolean insert();
	@Shadow protected abstract boolean isFull();
	@Shadow public abstract double getHopperX();
	@Shadow public abstract double getHopperY();
	@Shadow public abstract double getHopperZ();

	public HopperBlockEntity_hopperCountersUnlimitedSpeedMixin()
	{
		super(BlockEntityType.HOPPER);
	}

	@Inject(
			method = "insertAndExtract",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/entity/HopperBlockEntity;setCooldown(I)V"
			)
	)
	private void onActionSuccess(Supplier<Boolean> extractMethod, CallbackInfoReturnable<Boolean> cir)
	{
		if (CarpetSettings.hopperCounters && CarpetTISAdditionSettings.hopperCountersUnlimitedSpeed)
		{
			World world = getWorld();
			if (world == null)
			{
				return;
			}
			DyeColor wool_color = WoolTool.getWoolColorAtPosition(world, new BlockPos(getHopperX(), getHopperY(), getHopperZ()).offset(this.getCachedState().get(HopperBlock.FACING)));
			if (wool_color == null)
			{
				return;
			}
			for (int i = OPERATION_LIMIT - 1; i >= 0; i--)
			{
				boolean flag = false;
				if (!this.isInvEmpty())
				{
					flag = this.insert();
				}
				if (!this.isFull())
				{
					flag |= (Boolean)extractMethod.get();
				}
				if (!flag)
				{
					break;
				}
				if (i == 0)
				{
					CarpetTISAdditionServer.LOGGER.warn(String.format("Hopper in %s exceeded hopperCountersUnlimitedSpeed operation limit %d", new BlockPos(getHopperX(), getHopperY(), getHopperZ()), OPERATION_LIMIT));
				}
			}
		}
	}

	// to avoid repeatedly extraction an item entity in onEntityCollided
	@Inject(
			method = "extract(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/entity/ItemEntity;)Z",
			at = @At(value = "HEAD"),
			cancellable = true
	)
	private static void dontExtractEmptyItem(Inventory inventory, ItemEntity itemEntity, CallbackInfoReturnable<Boolean> cir)
	{
		if (itemEntity.getStack().isEmpty())
		{
			cir.setReturnValue(false);
			cir.cancel();
		}
	}
}

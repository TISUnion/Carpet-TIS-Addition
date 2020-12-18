package carpettisaddition.mixins.rule.hopperCountersUnlimitedSpeed;

import carpet.CarpetSettings;
import carpet.utils.WoolTool;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.BlockState;
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

import java.util.function.BooleanSupplier;


@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends LootableContainerBlockEntity
{
	private static final int OPERATION_LIMIT = Short.MAX_VALUE;

	@Shadow
	private static boolean insert(World world, BlockPos blockPos, BlockState blockState, Inventory inventory)
	{
		return false;
	}

	protected HopperBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState)
	{
		super(blockEntityType, blockPos, blockState);
	}

	@Inject(
			method = "insertAndExtract",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/entity/HopperBlockEntity;setCooldown(I)V"
			)
	)
	private static void onActionSuccess(World world, BlockPos blockPos, BlockState blockState, HopperBlockEntity hopperBlockEntity, BooleanSupplier booleanSupplier, CallbackInfoReturnable<Boolean> cir)
	{
		if (CarpetSettings.hopperCounters && CarpetTISAdditionSettings.hopperCountersUnlimitedSpeed)
		{
			if (world == null)
			{
				return;
			}
			DyeColor wool_color = WoolTool.getWoolColorAtPosition(world, hopperBlockEntity.getPos().offset(hopperBlockEntity.getCachedState().get(HopperBlock.FACING)));
			if (wool_color == null)
			{
				return;
			}
			for (int i = OPERATION_LIMIT - 1; i >= 0; i--)
			{
				boolean flag = false;
				// vanilla copy starts
				if (!hopperBlockEntity.isEmpty())

				{
					flag = insert(world, blockPos, blockState, hopperBlockEntity);
				}
				if (!((HopperBlockEntityAccessor)hopperBlockEntity).callIsFull())
				{
					flag |= booleanSupplier.getAsBoolean();
				}
				// vanilla copy ends

				if (!flag)
				{
					break;
				}
				if (i == 0)
				{
					CarpetTISAdditionServer.LOGGER.warn(String.format("Hopper in %s exceeded hopperCountersUnlimitedSpeed operation limit %d", hopperBlockEntity.getPos(), OPERATION_LIMIT));
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
	private static void dontExtractRemovedItem(Inventory inventory, ItemEntity itemEntity, CallbackInfoReturnable<Boolean> cir)
	{
		if (itemEntity.isRemoved() && CarpetTISAdditionSettings.hopperCountersUnlimitedSpeed)
		{
			cir.setReturnValue(false);
			cir.cancel();
		}
	}
}

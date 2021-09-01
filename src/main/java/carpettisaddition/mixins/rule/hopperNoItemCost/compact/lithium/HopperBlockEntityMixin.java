package carpettisaddition.mixins.rule.hopperNoItemCost.compact.lithium;

import carpet.utils.WoolTool;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.hopperNoItemCost.HopperNoItemCostHelper;
import me.jellysquid.mods.lithium.api.inventory.LithiumInventory;
import me.jellysquid.mods.lithium.common.hopper.InventoryHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BooleanSupplier;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin
{
	@Inject(
			method = "insertAndExtract",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/entity/HopperBlockEntity;insert(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/inventory/Inventory;)Z"
			)
	)
	private static void CTALithiumCompact$beforeInsert(World world, BlockPos pos, BlockState state, HopperBlockEntity hopperBlockEntity, BooleanSupplier booleanSupplier, CallbackInfoReturnable<Boolean> cir)
	{
		if (CarpetTISAdditionSettings.hopperNoItemCost)
		{
			DyeColor wool_color = WoolTool.getWoolColorAtPosition(world, pos.offset(Direction.UP));
			if (wool_color != null)
			{
				if (hopperBlockEntity instanceof LithiumInventory)
				{
					HopperNoItemCostHelper.currentHopperInvList.set(InventoryHelper.getLithiumStackList((LithiumInventory)hopperBlockEntity));
				}
			}
		}
	}

	@Inject(
			method = "insertAndExtract",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/entity/HopperBlockEntity;insert(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/inventory/Inventory;)Z",
					shift = At.Shift.AFTER
			)
	)
	private static void CTALithiumCompact$afterInsert(World world, BlockPos pos, BlockState state, HopperBlockEntity hopperBlockEntity, BooleanSupplier booleanSupplier, CallbackInfoReturnable<Boolean> cir)
	{
		if (CarpetTISAdditionSettings.hopperNoItemCost)
		{
			HopperNoItemCostHelper.currentHopperInvList.set(null);
		}
	}
}

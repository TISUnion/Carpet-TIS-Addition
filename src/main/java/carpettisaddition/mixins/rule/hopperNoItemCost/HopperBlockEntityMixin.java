package carpettisaddition.mixins.rule.hopperNoItemCost;

import carpet.utils.WoolTool;
import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends LootableContainerBlockEntity
{
	protected HopperBlockEntityMixin(BlockEntityType<?> blockEntityType)
	{
		super(blockEntityType);
	}

	@Shadow public abstract double getHopperX();

	@Shadow public abstract double getHopperY();

	@Shadow public abstract double getHopperZ();

	@Inject(
			method = "insert",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/inventory/Inventory;markDirty()V",
					shift = At.Shift.AFTER
			),
			locals = LocalCapture.CAPTURE_FAILSOFT
	)
	private void hopperNoItemCost(CallbackInfoReturnable<Boolean> cir, Inventory inventory, Direction direction, int i, ItemStack itemStack, ItemStack itemStack2)
	{
		if (CarpetTISAdditionSettings.hopperNoItemCost)
		{
			World world = this.getWorld();
			if (world != null)
			{
				DyeColor wool_color = WoolTool.getWoolColorAtPosition(world, new BlockPos(this.getHopperX(), this.getHopperY(), this.getHopperZ()).offset(Direction.UP));
				if (wool_color != null)
				{
					// restore the hopper inventory slot
					this.setStack(i, itemStack);
				}
			}
		}
	}
}

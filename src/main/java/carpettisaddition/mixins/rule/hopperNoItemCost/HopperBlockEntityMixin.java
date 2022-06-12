package carpettisaddition.mixins.rule.hopperNoItemCost;

import carpet.utils.WoolTool;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.scounter.SupplierCounterCommand;
import net.minecraft.block.BlockState;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends LootableContainerBlockEntity
{
	protected HopperBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState)
	{
		super(blockEntityType, blockPos, blockState);
	}

	@Inject(
			method = "insert",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/inventory/Inventory;markDirty()V",
					shift = At.Shift.AFTER
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private static void hopperNoItemCost(World world, BlockPos pos, BlockState state, Inventory inventory, CallbackInfoReturnable<Boolean> cir, Inventory inventory2, Direction direction, int i, ItemStack itemStack, ItemStack itemStack2)
	{
		if (CarpetTISAdditionSettings.hopperNoItemCost)
		{
			if (world != null)
			{
				DyeColor wool_color = WoolTool.getWoolColorAtPosition(world, pos.offset(Direction.UP));
				if (wool_color != null)
				{
					if (SupplierCounterCommand.isActivated())
					{
						int delta = inventory.getStack(i).getCount() - itemStack.getCount();
						if (delta <= 0)
						{
							ItemStack countingStack = itemStack.copy();
							countingStack.setCount(-delta);
							SupplierCounterCommand.getInstance().addFor(wool_color, countingStack);
						}
					}

					// restore the hopper inventory slot
					inventory.setStack(i, itemStack);
				}
			}
		}
	}
}

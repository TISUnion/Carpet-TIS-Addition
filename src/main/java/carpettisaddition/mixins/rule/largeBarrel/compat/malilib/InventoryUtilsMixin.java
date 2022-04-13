package carpettisaddition.mixins.rule.largeBarrel.compat.malilib;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.largeBarrel.LargeBarrelHelper;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = @Condition(ModIds.malilib))
@Pseudo
@Mixin(targets = "fi.dy.masa.malilib.util.InventoryUtils")
public abstract class InventoryUtilsMixin
{
	@SuppressWarnings("UnresolvedMixinReference")
	@Inject(
			method = "getInventory",
			at = @At(value = "RETURN", ordinal = 1),
			remap = false,
			cancellable = true
	)
	private static void letMalilibRecognizeLargBarrel(World world, BlockPos pos, CallbackInfoReturnable<Inventory> cir)
	{
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			Inventory inv = cir.getReturnValue();
			if (inv instanceof BarrelBlockEntity)
			{
				LargeBarrelHelper.enabledOffThreadBlockEntityAccess.set(true);
				try
				{
					Inventory largeBarrelInventory = LargeBarrelHelper.getInventory(world.getBlockState(pos), world, pos);
					if (largeBarrelInventory != null)
					{
						cir.setReturnValue(largeBarrelInventory);
					}
				}
				finally
				{
					LargeBarrelHelper.enabledOffThreadBlockEntityAccess.set(false);
				}
			}
		}
	}
}

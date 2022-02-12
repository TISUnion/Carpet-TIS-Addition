package carpettisaddition.mixins.rule.preciseEntityPlacement;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.preciseEntityPlacement.PreciseEntityPlacer;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnEggItem.class)
public abstract class SpawnEggItemMixin
{
	@Inject(
			method = "useOnBlock",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/EntityType;spawnFromItemStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;"
			)
	)
	private void preciseEntityPlacement(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir)
	{
		if (CarpetTISAdditionSettings.preciseEntityPlacement)
		{
			PreciseEntityPlacer.spawnEggTargetPos.set(context.getHitPos());
		}
	}

	@ModifyVariable(
			method = "use",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/hit/BlockHitResult;getBlockPos()Lnet/minecraft/util/math/BlockPos;"
			)
	)
	private BlockHitResult preciseEntityPlacement(BlockHitResult blockHitResult)
	{
		if (CarpetTISAdditionSettings.preciseEntityPlacement)
		{
			PreciseEntityPlacer.spawnEggTargetPos.set(blockHitResult.getPos());
		}
		return blockHitResult;
	}
}

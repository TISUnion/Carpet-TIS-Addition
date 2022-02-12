package carpettisaddition.mixins.rule.preciseEntityPlacement;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.preciseEntityPlacement.PreciseEntityPlacer;
import net.minecraft.item.ArmorStandItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandItem.class)
public abstract class ArmorStandItemItemMixin
{
	@Inject(
			method = "useOnBlock",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/text/Text;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;"
			)
	)
	private void preciseEntityPlacement(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir)
	{
		if (CarpetTISAdditionSettings.preciseEntityPlacement)
		{
			PreciseEntityPlacer.spawnEggTargetPos.set(context.getHitPos());
		}
	}
}

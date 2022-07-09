package carpettisaddition.mixins.rule.xpTrackingDistance;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin
{
	@ModifyConstant(
			//#if MC >= 11700
			//$$ method = {"tick", "expensiveUpdate"},
			//$$ require = 1,
			//#else
			method = "tick",
			require = 3,
			//#endif
			constant = @Constant(doubleValue = 8.0D)
	)
	private double modifyGiveUpDistance(double value)
	{
		return CarpetTISAdditionSettings.xpTrackingDistance;
	}

	@ModifyConstant(
			//#if MC >= 11700
			//$$ method = {"tick", "expensiveUpdate"},
			//$$ require = 1,
			//#else
			method = "tick",
			require = 2,
			//#endif
			constant = @Constant(doubleValue = 64.0D)
	)
	private double modifyGiveUpDistanceSquare(double value)
	{
		return CarpetTISAdditionSettings.xpTrackingDistance * CarpetTISAdditionSettings.xpTrackingDistance;
	}

	@Redirect(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/player/PlayerEntity;isSpectator()Z"
			)
	)
	private boolean isSpectatorOrTrackingDistanceIsZero(PlayerEntity player)
	{
		return CarpetTISAdditionSettings.xpTrackingDistance == 0 || player.isSpectator();
	}
}

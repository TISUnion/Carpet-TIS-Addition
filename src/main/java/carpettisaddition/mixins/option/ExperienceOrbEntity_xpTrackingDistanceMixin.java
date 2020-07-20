package carpettisaddition.mixins.option;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntity_xpTrackingDistanceMixin
{
	@ModifyConstant(
			method = "tick",
			require = 3,
			constant = @Constant(doubleValue = 8.0D)
	)
	private double modifyGiveUpDistance(double value)
	{
		return CarpetTISAdditionSettings.xpTrackingDistance;
	}

	@ModifyConstant(
			method = "tick",
			require = 2,
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

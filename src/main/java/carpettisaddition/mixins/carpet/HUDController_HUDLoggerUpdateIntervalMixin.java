package carpettisaddition.mixins.carpet;

import carpet.utils.HUDController;
import carpettisaddition.CarpetTISAdditionSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;


@Mixin(HUDController.class)
public abstract class HUDController_HUDLoggerUpdateIntervalMixin
{
	@ModifyConstant(
			method = "update_hud",
			constant = @Constant(
					intValue = 20,
					ordinal = 0
			),
			remap = false
	)
	private static int customizeUpdateInterval(int value)
	{
		return CarpetTISAdditionSettings.HUDLoggerUpdateInterval;
	}
}

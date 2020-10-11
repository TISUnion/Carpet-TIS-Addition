package carpettisaddition.mixins.rule.HUDLoggerUpdateInterval;

import carpet.logging.HUDController;
import carpettisaddition.CarpetTISAdditionSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;


@Mixin(HUDController.class)
public abstract class HUDControllerMixin
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

package carpettisaddition.mixins.rule.tileTickLimit;

import carpettisaddition.CarpetTISAdditionSettings;
import me.jellysquid.mods.lithium.common.world.scheduler.LithiumServerTickScheduler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Pseudo
@Mixin(value = LithiumServerTickScheduler.class, priority = 998)
public abstract class LithiumServerTickSchedulerMixin<T>
{
	@ModifyConstant(
			method = "selectTicks",
			constant = {
					@Constant(intValue = 65536),  // vanilla value
					@Constant(intValue = 65565),  // value in lithium (<=0.6.0)
			},
			require = 1,
			remap = false
	)
	private static int modifyTileTickLimit(int value)
	{
		return CarpetTISAdditionSettings.tileTickLimit;
	}
}

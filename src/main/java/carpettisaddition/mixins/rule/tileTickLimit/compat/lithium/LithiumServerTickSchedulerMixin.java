package carpettisaddition.mixins.rule.tileTickLimit.compat.lithium;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

//#if MC >= 11800
//$$ import carpettisaddition.utils.compat.DummyClass;
//#else
import me.jellysquid.mods.lithium.common.world.scheduler.LithiumServerTickScheduler;
//#endif

@Restriction(require = {
		@Condition(value = ModIds.minecraft, versionPredicates = "<1.18"),
		@Condition(ModIds.lithium)
})
@Mixin(
		//#if MC >= 11800
		//$$ value = DummyClass.class,
		//#else
		value = LithiumServerTickScheduler.class,
		//#endif
		priority = 998
)
public abstract class LithiumServerTickSchedulerMixin
{
	//#if MC < 11800
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
	//#endif
}

package carpettisaddition.mixins.rule.tileTickLimit;

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
import net.minecraft.server.world.ServerTickScheduler;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.18"))
@Mixin(
		//#if MC >= 11800
		//$$ value = DummyClass.class,
		//#else
		value = ServerTickScheduler.class,
		//#endif
		priority = 998
)
public abstract class ServerTickSchedulerMixin<T>
{
	//#if MC <11800
	@ModifyConstant(
			method = "tick",
			constant = @Constant(intValue = 65536),
			require = 2
	)
	private static int modifyTileTickLimit(int value)
	{
		return CarpetTISAdditionSettings.tileTickLimit;
	}
	//#endif
}

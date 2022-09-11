package carpettisaddition.mixins.rule.yeetUpdateSuppressionCrash;

import carpettisaddition.utils.compat.DummyClass;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DummyClass.class)
public abstract class ServerPlayerEntityMixin
{
	// fabric carpet already has rule updateSuppressionCrashFix in 1.17+
	// so we disable our rule yeetUpdateSuppressionCrash
}

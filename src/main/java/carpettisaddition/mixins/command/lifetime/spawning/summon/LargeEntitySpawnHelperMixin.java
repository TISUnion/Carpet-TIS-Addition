package carpettisaddition.mixins.command.lifetime.spawning.summon;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11900
//$$ import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
//$$ import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
//$$ import net.minecraft.entity.LargeEntitySpawnHelper;
//$$ import net.minecraft.entity.mob.MobEntity;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//$$ import java.util.Optional;
//#else
import carpettisaddition.utils.compat.DummyClass;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
@Mixin(
		//#if MC >= 11900
		//$$ LargeEntitySpawnHelper.class
		//#else
		DummyClass.class
		//#endif
)
public abstract class LargeEntitySpawnHelperMixin
{
	//#if MC >= 11900
	//$$ @Inject(method = "trySpawnAt", at= @At("RETURN"))
	//$$ private static <T extends MobEntity> void onEntitySummonLifeTimeTracker(CallbackInfoReturnable<Optional<T>> cir)
	//$$ {
	//$$ 	cir.getReturnValue().ifPresent(entity -> ((LifetimeTrackerTarget)entity).recordSpawning(LiteralSpawningReason.SUMMON));
	//$$ }
	//#endif
}
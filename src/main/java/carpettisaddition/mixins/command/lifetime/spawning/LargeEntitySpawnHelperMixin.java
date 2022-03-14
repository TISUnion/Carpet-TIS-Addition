package carpettisaddition.mixins.command.lifetime.spawning;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.LargeEntitySpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LargeEntitySpawnHelper.class)
public abstract class LargeEntitySpawnHelperMixin
{
	@Inject(method = "trySpawnAt", at= @At("RETURN"))
	private static <T extends MobEntity> void onEntitySummonLifeTimeTracker(CallbackInfoReturnable<Optional<T>> cir)
	{
		cir.getReturnValue().ifPresent(entity -> ((LifetimeTrackerTarget)entity).recordSpawning(LiteralSpawningReason.SUMMON));
	}
}

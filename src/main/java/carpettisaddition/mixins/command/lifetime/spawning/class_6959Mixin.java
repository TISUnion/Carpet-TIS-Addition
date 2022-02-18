package carpettisaddition.mixins.command.lifetime.spawning;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.class_6959;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

// Summon Util or whatever
@Mixin(class_6959.class)
public abstract class class_6959Mixin
{
	@Inject(method = "method_40585", at= @At("RETURN"))
	private static <T extends MobEntity> void onEntitySummonLifeTimeTracker(CallbackInfoReturnable<Optional<T>> cir)
	{
		cir.getReturnValue().ifPresent(entity -> ((LifetimeTrackerTarget)entity).recordSpawning(LiteralSpawningReason.SUMMON));
	}
}

package carpettisaddition.mixins.command.lifetime.spawning.summon;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin
{
	@Inject(method = "spawnIronGolem", at= @At("RETURN"))
	private void onVillagerSummonedIronGolemLifeTimeTracker(CallbackInfoReturnable<IronGolemEntity> cir)
	{
		if (cir.getReturnValue() != null)
		{
			((LifetimeTrackerTarget)cir.getReturnValue()).recordSpawning(LiteralSpawningReason.SUMMON);
		}
	}
}

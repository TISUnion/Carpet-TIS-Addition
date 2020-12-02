package carpettisaddition.mixins.command.lifetime.deathdamage;

import carpettisaddition.commands.lifetime.interfaces.EntityDamageable;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin implements EntityDamageable
{
	private DamageSource deathDamageSource;

	@Inject(
			method = "damage",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/ExperienceOrbEntity;remove()V"
			)
	)
	private	void onDeathLifeTimeTracker(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		this.deathDamageSource = source;
	}

	@Override
	public DamageSource getDeathDamageSource()
	{
		return this.deathDamageSource;
	}
}

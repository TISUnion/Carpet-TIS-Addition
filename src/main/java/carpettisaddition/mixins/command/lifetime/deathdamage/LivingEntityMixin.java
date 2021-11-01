package carpettisaddition.mixins.command.lifetime.deathdamage;

import carpettisaddition.commands.lifetime.interfaces.DamageableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements DamageableEntity
{
	private DamageSource deathDamageSource;

	@Inject(
			method = "onDeath",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/damage/DamageSource;getAttacker()Lnet/minecraft/entity/Entity;"
			)
	)
	void onDeathLifeTimeTracker(DamageSource source, CallbackInfo ci)
	{
		this.deathDamageSource = source;
	}

	@Override
	public DamageSource getDeathDamageSource()
	{
		return this.deathDamageSource;
	}
}

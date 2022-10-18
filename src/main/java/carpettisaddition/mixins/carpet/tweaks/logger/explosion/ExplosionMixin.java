package carpettisaddition.mixins.carpet.tweaks.logger.explosion;

import carpettisaddition.helpers.carpet.tweaks.logger.explosion.ExplosionLogHelperWithEntity;
import carpettisaddition.utils.ReflectionUtil;
import net.minecraft.entity.Entity;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Inject after than fabric carpet's mixin
 *   <1.4.59: {@link carpet.mixins.ExplosionMixin}
 *   >=1.4.59: {@link carpet.mixins.Explosion_optimizedTntMixin}
 * So the eLogger field is constructed
 */
@Mixin(value = Explosion.class, priority = 2000)
public abstract class ExplosionMixin
{
	@Shadow @Final private Entity entity;

	@Inject(
			//#if MC >= 11600
			//$$ method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/explosion/Explosion$DestructionType;)V",
			//#else
			method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;DDDFZLnet/minecraft/world/explosion/Explosion$DestructionType;)V",
			//#endif
			at = @At("TAIL")
	)
	private void hackCarpetExplosionLogHelper(CallbackInfo ci)
	{
		ReflectionUtil.getField(this, "eLogger").ifPresent(eLogger -> {
			if (eLogger instanceof ExplosionLogHelperWithEntity)
			{
				((ExplosionLogHelperWithEntity)eLogger).setEntity$TISCM(this.entity);
			}
		});
	}
}

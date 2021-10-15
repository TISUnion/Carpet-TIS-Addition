package carpettisaddition.mixins.rule.optimizedTNTHighPriority;

import carpet.CarpetSettings;
import carpet.helpers.OptimizedExplosion;
import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * priority value restrictions:
 * - less than 1000 ({@link me.jellysquid.mods.lithium.mixin.world.fast_explosions.MixinExplosion}, priority = default value = 1000)
 * - more than 800 ({@link carpettisaddition.mixins.carpet.tweaks.rule.tntRandomRange.ExplosionMixin}, priority = 800)
 * so it injects after wrapping world.random but before lithium explosion optimization
 */
@Mixin(value = Explosion.class, priority = 900)
public abstract class ExplosionMixin
{
	@SuppressWarnings("ConstantConditions")
	@Inject(method = "collectBlocksAndDamageEntities", at = @At("HEAD"), cancellable = true)
	private void onExplosionAButWithHighPriority(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.optimizedTNTHighPriority)
		{
			// copy of carpet's onExplosionA method in ExplosionMixin begins
			if (CarpetSettings.optimizedTNT)
			{
				OptimizedExplosion.doExplosionA((Explosion) (Object) this);
				ci.cancel();
			}
			// copy ends
		}
	}
}

package carpettisaddition.mixins.rule.optimizedTNTHighPriority;

import carpet.CarpetSettings;
import carpet.helpers.OptimizedExplosion;
import carpet.logging.logHelpers.ExplosionLogHelper;
import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

/**
 * priority value restrictions:
 * - less than 1000 ({@link me.jellysquid.mods.lithium.mixin.world.explosions.ExplosionMixin}, priority = default value = 1000)
 * - more than 800 ({@link carpettisaddition.mixins.carpet.tweaks.rule.tntRandomRange.ExplosionMixin}, priority = 800)
 * so it injects after wrapping world.random but before lithium explosion optimization
 */
@Mixin(value = Explosion.class, priority = 900)
public abstract class ExplosionMixin
{
	private static final Field EXPLOSION_LOGGER_FIELD;

	static
	{
		Field target = null;
		try
		{
			// finding fabric-carpet's eLogger field
			for (Field field : ExplosionMixin.class.getDeclaredFields())
			{
				if (field.getType() == ExplosionLogHelper.class)
				{
					field.setAccessible(true);
					target = field;
					break;
				}
			}
		}
		catch (Exception ignored)
		{
		}
		EXPLOSION_LOGGER_FIELD = target;
	}

	@SuppressWarnings("ConstantConditions")
	@Inject(method = "collectBlocksAndDamageEntities", at = @At("HEAD"), cancellable = true)
	private void onExplosionAButWithHighPriority(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.optimizedTNTHighPriority && EXPLOSION_LOGGER_FIELD != null)
		{
			try
			{
				ExplosionLogHelper eLogger = (ExplosionLogHelper)EXPLOSION_LOGGER_FIELD.get(this);

				// copy of carpet's onExplosionA method in ExplosionMixin begins
				if (CarpetSettings.optimizedTNT)
				{
					OptimizedExplosion.doExplosionA((Explosion) (Object) this, eLogger);
					ci.cancel();
				}
				// copy ends
			}
			catch (IllegalAccessException ignored)
			{
			}
		}
	}
}

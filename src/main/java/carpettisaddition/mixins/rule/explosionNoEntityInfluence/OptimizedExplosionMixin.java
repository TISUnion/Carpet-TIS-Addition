package carpettisaddition.mixins.rule.explosionNoEntityInfluence;

import carpet.helpers.OptimizedExplosion;
import carpet.logging.logHelpers.ExplosionLogHelper;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.mixins.rule.tooledTNT.ExplosionMixin;
import net.minecraft.entity.Entity;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;

@Mixin(OptimizedExplosion.class)
public abstract class OptimizedExplosionMixin
{
	@Shadow(remap = false)
	private static List<Entity> entitylist;

	/**
	 * See {@link ExplosionMixin} for mixin at vanilla in for this rule
	 */
	@Inject(
			method = "doExplosionA",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/world/World;getEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/List;",
					shift = At.Shift.AFTER
			)
	)
	private static void explosionNoEntityInfluence(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.explosionNoEntityInfluence)
		{
			entitylist = Collections.emptyList();
		}
	}
}

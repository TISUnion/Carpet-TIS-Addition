package carpettisaddition.mixins.rule.explosionNoEntityInfluence;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.Entity;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Collections;
import java.util.List;

@Mixin(Explosion.class)
public abstract class ExplosionMixin
{
	@ModifyVariable(
			method = "collectBlocksAndDamageEntities",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/world/World;getEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/List;",
					shift = At.Shift.AFTER
			)
	)
	private List<Entity> explosionNoEntityInfluence(List<Entity> entities)
	{
		if (CarpetTISAdditionSettings.explosionNoEntityInfluence)
		{
			entities = Collections.emptyList();
		}
		return entities;
	}
}

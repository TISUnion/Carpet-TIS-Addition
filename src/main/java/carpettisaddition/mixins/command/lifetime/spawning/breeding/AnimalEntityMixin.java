package carpettisaddition.mixins.command.lifetime.spawning.breeding;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.passive.AnimalEntity;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11600
//$$ import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
//$$ import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
//$$ import net.minecraft.entity.Entity;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.ModifyArg;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.16"))
@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin
{
	//#if MC >= 11600
	//$$ @ModifyArg(
	//$$ 		method = "breed",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V",
	//$$ 				ordinal = 0
	//$$ 		)
	//$$ )
	//$$ private Entity onAnimalBirthLifeTimeTracker(Entity entity)
	//$$ {
	//$$ 	if (entity != null)
	//$$ 	{
	//$$ 		((LifetimeTrackerTarget)entity).recordSpawning(LiteralSpawningReason.BREEDING);
	//$$ 	}
	//$$ 	return entity;
	//$$ }
	//#endif
}
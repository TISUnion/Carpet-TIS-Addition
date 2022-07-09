package carpettisaddition.mixins.command.lifetime.spawning.conversion;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11600
//$$ import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
//$$ import carpettisaddition.commands.lifetime.spawning.MobConversionSpawningReason;
//$$ import net.minecraft.entity.Entity;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.ModifyArg;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.16"))
@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity
{
	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world)
	{
		super(entityType, world);
	}

	//#if MC >= 11600
	//$$ @ModifyArg(
	//$$ 		//#if MC >= 11700
	//$$ 		//$$ method = "convertTo",
	//$$ 		//#else
	//$$ 		method = "method_29243",  // convertTo
	//$$ 		//#endif
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
	//$$ 		)
	//$$ )
	//$$ private Entity recordSelfRemoval$LifeTimeTracker(Entity targetEntity)
	//$$ {
	//$$ 	((LifetimeTrackerTarget)targetEntity).recordSpawning(new MobConversionSpawningReason(this.getType()));
	//$$ 	return targetEntity;
	//$$ }
	//#endif
}
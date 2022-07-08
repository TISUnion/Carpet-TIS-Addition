package carpettisaddition.mixins.command.lifetime.spawning.conversion;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.MobConversionSpawningReason;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.16"))
@Mixin(ZombieVillagerEntity.class)
public abstract class ZombieVillagerEntityMixin extends ZombieEntity
{
	public ZombieVillagerEntityMixin(EntityType<? extends ZombieEntity> type, World world)
	{
		super(type, world);
	}

	//#if MC < 11600
	@ModifyArg(
			method = "finishConversion",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			)
	)
	private Entity recordCuredVillagerSpawning$LifeTimeTracker(Entity villager)
	{
		((LifetimeTrackerTarget)villager).recordSpawning(new MobConversionSpawningReason(this.getType()));
		return villager;
	}
	//#endif
}

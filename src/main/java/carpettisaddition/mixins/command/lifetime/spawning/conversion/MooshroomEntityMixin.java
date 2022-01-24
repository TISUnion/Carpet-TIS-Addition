package carpettisaddition.mixins.command.lifetime.spawning.conversion;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.MobConversionSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MooshroomEntity.class)
public abstract class MooshroomEntityMixin extends Entity
{
	public MooshroomEntityMixin(EntityType<?> type, World world)
	{
		super(type, world);
	}

	@ModifyArg(
			method = "sheared",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z",
					ordinal = 0
			)
	)
	private Entity recordCowSpawning$LifeTimeTracker(Entity cow)
	{
		((LifetimeTrackerTarget)cow).recordSpawning(new MobConversionSpawningReason(this.getType()));
		return cow;
	}
}

package carpettisaddition.mixins.command.lifetime.spawning.summon;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CarvedPumpkinBlock.class)
public abstract class CarvedPumpkinBlockMixin
{
	@ModifyArg(
			//#if MC >= 11903
			//$$ method = "spawnEntity",
			//#else
			method = "trySpawnEntity",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			)
			//#if MC < 11903
			, require = 2
			//#endif
	)
	//#if MC >= 11903
	//$$ static
	//#endif
	private Entity onGolemSummonedLifeTimeTracker(Entity entity)
	{
		((LifetimeTrackerTarget)entity).recordSpawning(LiteralSpawningReason.SUMMON);
		return entity;
	}
}

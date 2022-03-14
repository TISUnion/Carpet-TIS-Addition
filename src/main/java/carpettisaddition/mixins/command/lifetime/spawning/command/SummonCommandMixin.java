package carpettisaddition.mixins.command.lifetime.spawning.command;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.SummonCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SummonCommand.class)
public abstract class SummonCommandMixin
{
	@ModifyVariable(
			method = "execute",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Lnet/minecraft/text/Text;Z)V",
					ordinal = 0
			)
	)
	private static Entity onEntitySummonLifeTimeTracker(Entity entity)
	{
		if (entity != null)
		{
			((LifetimeTrackerTarget)entity).recordSpawning(LiteralSpawningReason.COMMAND);
		}
		return entity;
	}
}

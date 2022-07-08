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
					//#if MC >= 11600
					//$$ ordinal = 0
					//#else
					ordinal = 1
					//#endif
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

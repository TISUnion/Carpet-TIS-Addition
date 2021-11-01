package carpettisaddition.mixins.command.lifetime.spawning;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.SummonCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SummonCommand.class)
public abstract class SummonCommandMixin
{
	@Inject(
			method = "execute",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/entity/EntityType;loadEntityWithPassengers(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/World;Ljava/util/function/Function;)Lnet/minecraft/entity/Entity;"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private static void onEntitySummonLifeTimeTracker(ServerCommandSource source, Identifier entity, Vec3d pos, CompoundTag nbt, boolean initialize, CallbackInfoReturnable<Integer> cir, CompoundTag compoundTag, ServerWorld serverWorld, Entity entity2)
	{
		if (entity2 != null)
		{
			((LifetimeTrackerTarget) entity2).recordSpawning(LiteralSpawningReason.COMMAND);
		}
	}
}

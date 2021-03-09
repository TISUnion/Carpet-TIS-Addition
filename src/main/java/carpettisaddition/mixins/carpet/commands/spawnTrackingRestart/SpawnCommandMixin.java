package carpettisaddition.mixins.carpet.commands.spawnTrackingRestart;

import carpet.commands.SpawnCommand;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import static net.minecraft.server.command.CommandManager.literal;

@Mixin(SpawnCommand.class)
public abstract class SpawnCommandMixin
{
	/**
	 * Attach literal("restart") to node literal("tracking") by modifying argument in then(literal("tracking"))
	 */
	@ModifyArg(
			method = "register",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "stringValue=tracking",
							ordinal = 0
					),
					to = @At(
							value = "CONSTANT",
							args = "stringValue=test",
							ordinal = 0
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;then(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;",
					ordinal = 4
			),
			index = 0,
			remap = false
	)
	private static ArgumentBuilder<ServerCommandSource, ?> appendRestartArgumentOnSpawnTracking(ArgumentBuilder<ServerCommandSource, ?> builder)
	{
		builder.then(
				literal("restart").
						executes(c -> {
							int result = 0;
							result += SpawnCommandInvoker.callStopTracking(c.getSource());
							result += SpawnCommandInvoker.callStartTracking(c.getSource(), null, null);
							return result > 0 ? 1 : 0;
						})
		);
		return builder;
	}
}

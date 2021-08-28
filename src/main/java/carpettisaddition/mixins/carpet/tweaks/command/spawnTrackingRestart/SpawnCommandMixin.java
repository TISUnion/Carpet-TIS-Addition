package carpettisaddition.mixins.carpet.tweaks.command.spawnTrackingRestart;

import carpet.commands.SpawnCommand;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import static net.minecraft.server.command.CommandManager.literal;

@Mixin(SpawnCommand.class)
public abstract class SpawnCommandMixin
{
	@Shadow(remap = false)
	private static int stopTracking(ServerCommandSource source)
	{
		return 0;
	}

	@Shadow(remap = false)
	private static int startTracking(ServerCommandSource source, BlockPos a, BlockPos b)
	{
		return 0;
	}

	/**
	 * Add literal("restart") as a child of node literal("tracking") by modifying argument in then(literal("tracking"))
	 */
	@Dynamic
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
							result += stopTracking(c.getSource());
							result += startTracking(c.getSource(), null, null);
							return result > 0 ? 1 : 0;
						})
		);
		return builder;
	}
}

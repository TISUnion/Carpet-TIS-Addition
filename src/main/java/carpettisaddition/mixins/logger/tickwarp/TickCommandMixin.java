package carpettisaddition.mixins.logger.tickwarp;

import carpet.commands.TickCommand;
import carpettisaddition.logging.loggers.tickwarp.TickWarpHUDLogger;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TickCommand.class)
public abstract class TickCommandMixin
{
	@ModifyArg(
			method = "register",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/CommandDispatcher;register(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;"
			),
			index = 0,
			remap = false
	)
	private static LiteralArgumentBuilder<ServerCommandSource> registerTickWarpInfo(LiteralArgumentBuilder<ServerCommandSource> builder)
	{
		TickWarpHUDLogger.getInstance().extendCommand(builder);
		return builder;
	}
}

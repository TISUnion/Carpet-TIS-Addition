package carpettisaddition.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;

public interface CommandExtender
{
	void extendCommand(LiteralArgumentBuilder<ServerCommandSource> builder, CommandRegistryAccess commandBuildContext);
}

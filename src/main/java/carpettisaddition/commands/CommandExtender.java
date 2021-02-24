package carpettisaddition.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

public interface CommandExtender
{
	void extendCommand(LiteralArgumentBuilder<ServerCommandSource> builder);
}

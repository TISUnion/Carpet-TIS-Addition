package carpettisaddition.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_7157;
import net.minecraft.server.command.ServerCommandSource;

public interface CommandExtender
{
	void extendCommand(LiteralArgumentBuilder<ServerCommandSource> builder, class_7157 commandBuildContext);
}

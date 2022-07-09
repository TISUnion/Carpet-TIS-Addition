package carpettisaddition.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

//#if MC >= 11900
//$$ import net.minecraft.command.CommandRegistryAccess;
//#endif

public interface CommandExtender
{
	void extendCommand(
			LiteralArgumentBuilder<ServerCommandSource> builder
			//#if MC >= 11900
			//$$ , CommandRegistryAccess commandBuildContext
			//#endif
	);
}

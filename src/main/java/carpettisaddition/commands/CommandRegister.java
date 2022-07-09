package carpettisaddition.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

//#if MC >= 11900
//$$ import net.minecraft.command.CommandRegistryAccess;
//#endif

public interface CommandRegister
{
	void registerCommand(
			CommandDispatcher<ServerCommandSource> dispatcher
			//#if MC >= 11900
			//$$ , CommandRegistryAccess commandBuildContext
			//#endif
	);
}

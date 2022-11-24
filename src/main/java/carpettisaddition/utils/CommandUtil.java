package carpettisaddition.utils;

import carpettisaddition.mixins.utils.ServerCommandSourceAccessor;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;

public class CommandUtil
{
	public static boolean isConsoleCommandSource(ServerCommandSource commandSource)
	{
		if (commandSource != null)
		{
			CommandOutput output = ((ServerCommandSourceAccessor)commandSource).getOutput();
			return output instanceof MinecraftServer;
		}
		return false;
	}

	public static boolean isPlayerCommandSource(ServerCommandSource commandSource)
	{
		if (commandSource != null)
		{
			try
			{
				commandSource.getPlayer();
				return true;
			}
			catch (CommandSyntaxException e)
			{
				return false;
			}
		}
		return false;
	}
}

package carpettisaddition.utils;

import net.minecraft.server.command.ServerCommandSource;

public class CarpetModUtil
{
	/**
	 * Copied from {@link carpet.settings.SettingsManager#canUseCommand} in fabric-carpet 1.4.19
	 * In fabric-carpet 1.4.19 the signature of method canUseCommand is changed, so I made a copy version of the method
	 * for backwards compatibility for Minecraft 1.16.4 and some 1.17 snapshots
	 * Firstly try to use carpet's method, if failed, use our own copied implementation
	 */
	public static boolean canUseCommand(ServerCommandSource source, Object commandLevel)
	{
		//#if MC >= 11600
		//$$ return carpet.settings.SettingsManager.canUseCommand(source, commandLevel);
		//#else
		if (commandLevel instanceof Boolean) return (Boolean) commandLevel;
		String commandLevelString = commandLevel.toString();
		switch (commandLevelString)
		{
			case "true": return true;
			case "false": return false;
			case "ops": return source.hasPermissionLevel(2); // typical for other cheaty commands
			case "0":
			case "1":
			case "2":
			case "3":
			case "4":
				return source.hasPermissionLevel(Integer.parseInt(commandLevelString));
		}
		return false;
		//#endif
	}
}

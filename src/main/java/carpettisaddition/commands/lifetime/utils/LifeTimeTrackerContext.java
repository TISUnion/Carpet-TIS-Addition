package carpettisaddition.commands.lifetime.utils;

import net.minecraft.server.command.ServerCommandSource;

public class LifeTimeTrackerContext
{
	// a context in command execution, the command source for the current statistic printing
	public static final ThreadLocal<ServerCommandSource> commandSource = ThreadLocal.withInitial(() -> null);
}

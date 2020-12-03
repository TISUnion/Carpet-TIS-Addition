package carpettisaddition.logging;

import carpet.logging.Logger;
import carpettisaddition.mixins.carpet.LoggerRegistryInvoker;

public class LoggerRegistry
{
	// a wrapped method to reduce merge conflicts
	public static void registerLogger(String name, Logger logger)
	{
		LoggerRegistryInvoker.callRegisterLogger(name, logger);
	}
}

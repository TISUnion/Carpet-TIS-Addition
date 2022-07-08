package carpettisaddition.logging.compat;

import carpet.logging.Logger;
import carpettisaddition.mixins.carpet.access.LoggerRegistryInvoker;

/**
 * Used in mc 1.14.4 where carpet doesn't provide logging support for carpet extensions
 */
public class LoggerRegistry
{
	// a wrapped method to reduce merge conflicts
	public static void registerLogger(String name, Logger logger)
	{
		LoggerRegistryInvoker.callRegisterLogger(name, logger);
	}
}
package carpettisaddition.logging.compat;

import java.lang.reflect.Field;

/**
 * Used in mc 1.14.4 where carpet doesn't provide logging support for carpet extensions
 */
public interface IExtensionLogger
{
	Field getAcceleratorField();
}
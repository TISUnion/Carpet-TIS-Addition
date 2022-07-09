package carpettisaddition.logging.compat;

import carpet.logging.HUDLogger;

import java.lang.reflect.Field;

/**
 * Used in mc 1.14.4 where carpet doesn't provide logging support for carpet extensions
 */
public class ExtensionHUDLogger extends HUDLogger implements IExtensionLogger
{
	private final Field acceleratorField;

	public ExtensionHUDLogger(Field acceleratorField, String logName, String def, String[] options)
	{
		super(
				//#if MC >= 11500
				acceleratorField,
				//#endif

				logName, def, options

				//#if MC >= 11700
				//$$ , false
				//#endif
		);
		this.acceleratorField = acceleratorField;
	}

	@Override
	public Field getAcceleratorField()
	{
		return acceleratorField;
	}
}
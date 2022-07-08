package carpettisaddition.logging.compat;

import carpet.logging.Logger;

import java.lang.reflect.Field;

/**
 * Used in mc 1.14.4 where carpet doesn't provide logging support for carpet extensions
 */
public class ExtensionLogger extends Logger implements IExtensionLogger
{
	private final Field acceleratorField;

	public ExtensionLogger(Field acceleratorField, String logName, String def, String[] options)
	{
		super(
				//#if MC >= 11500
				acceleratorField,
				//#endif
				logName, def, options
		);
		this.acceleratorField = acceleratorField;
	}

	@Override
	public Field getAcceleratorField()
	{
		return acceleratorField;
	}
}
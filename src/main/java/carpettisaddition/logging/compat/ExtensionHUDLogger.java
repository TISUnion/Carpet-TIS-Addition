package carpettisaddition.logging.compat;

import carpet.logging.HUDLogger;

import java.lang.reflect.Field;

public class ExtensionHUDLogger extends HUDLogger implements IExtensionLogger
{
	private final Field acceleratorField;

	public ExtensionHUDLogger(Field acceleratorField, String logName, String def, String[] options)
	{
		super(logName, def, options);
		this.acceleratorField = acceleratorField;
	}

	@Override
	public Field getAcceleratorField()
	{
		return acceleratorField;
	}
}

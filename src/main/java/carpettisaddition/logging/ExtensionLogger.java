package carpettisaddition.logging;

import carpet.logging.Logger;

import java.lang.reflect.Field;

public class ExtensionLogger extends Logger implements IExtensionLogger
{
	private final Field acceleratorField;

	public ExtensionLogger(Field acceleratorField, String logName, String def, String[] options)
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

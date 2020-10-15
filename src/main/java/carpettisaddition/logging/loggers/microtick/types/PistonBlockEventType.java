package carpettisaddition.logging.loggers.microtick.types;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;

public enum PistonBlockEventType
{
	PUSH("Push"),
	RETRACT("Retract"),
	DROP("Drop");

	private static final PistonBlockEventType[] VALUES = PistonBlockEventType.values();
	private final String name;

	PistonBlockEventType(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return MicroTickLoggerManager.tr("piston_block_event_type." + this.name, this.name);
	}

	public static PistonBlockEventType byId(int id)
	{
		return VALUES[Math.abs(id % VALUES.length)];
	}
}

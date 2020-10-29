package carpettisaddition.logging.loggers.microtiming.enums;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;

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
		return MicroTimingLoggerManager.tr("piston_block_event_type." + this.name, this.name);
	}

	public static PistonBlockEventType byId(int id)
	{
		return VALUES[Math.abs(id % VALUES.length)];
	}
}

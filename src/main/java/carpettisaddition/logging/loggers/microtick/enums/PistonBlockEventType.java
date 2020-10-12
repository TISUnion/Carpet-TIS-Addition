package carpettisaddition.logging.loggers.microtick.enums;

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
		return this.name;
	}

	public static PistonBlockEventType getById(int id)
	{
		return VALUES[id];
	}
}

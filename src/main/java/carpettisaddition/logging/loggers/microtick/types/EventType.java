package carpettisaddition.logging.loggers.microtick.types;

public enum EventType
{
	ACTION_START("Begin"),
	ACTION_END("End"),
	EVENT("Event");

	private final String name;

	EventType(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}

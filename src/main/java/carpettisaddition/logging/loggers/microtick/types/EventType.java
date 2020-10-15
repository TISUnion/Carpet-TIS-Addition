package carpettisaddition.logging.loggers.microtick.types;

public enum EventType
{
	ACTION_START("Begin"),
	ACTION_END("End"),
	ACTION("Action"),  // merge of ACTION_START and ACTION_END
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

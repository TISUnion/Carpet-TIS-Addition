package carpettisaddition.logging.loggers.microtick.types;

public enum MessageType
{
	ACTION_START("Begin"),
	ACTION_END("End"),
	EVENT("Event");

	private final String name;

	MessageType(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}

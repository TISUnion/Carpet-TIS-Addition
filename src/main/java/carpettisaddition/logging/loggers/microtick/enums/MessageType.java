package carpettisaddition.logging.loggers.microtick.enums;

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

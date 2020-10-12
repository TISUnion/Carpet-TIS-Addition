package carpettisaddition.logging.loggers.microtick.enums;

public enum ActionRelation
{
	PRE_ACTION("Starting"),
	POST_ACTION("Finished");

	private final String name;

	ActionRelation(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}

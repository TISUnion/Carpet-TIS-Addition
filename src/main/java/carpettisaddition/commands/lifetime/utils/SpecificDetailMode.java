package carpettisaddition.commands.lifetime.utils;

import java.util.Arrays;

public enum SpecificDetailMode
{
	LIFE_TIME,
	SPAWNING,
	REMOVAL;

	private static final String[] SUGGESTIONS = Arrays.stream(values()).map(SpecificDetailMode::toString).toArray(String[]::new);

	public static String[] getSuggestion()
	{
		return SUGGESTIONS;
	}

	@Override
	public String toString()
	{
		return super.toString().toLowerCase();
	}

	public static SpecificDetailMode fromString(String str)
	{
		return valueOf(str.toUpperCase());
	}
}

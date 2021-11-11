package carpettisaddition.helpers.rule.persistentLoggerSubscription;

import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;

public class DataStorage extends LinkedHashMap<String, DataStorage.PlayerEntry>
{
	// logger name -> option
	public static class PlayerEntry extends LinkedHashMap<String, @Nullable String>
	{
	}
}

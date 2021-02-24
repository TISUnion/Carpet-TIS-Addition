package carpettisaddition.utils.stacktrace.yarn;

import carpettisaddition.utils.stacktrace.StackTraceDeobfuscator;

import java.io.InputStream;

public class BundledMappingProvider
{
	private static final String MAPPING_NAME = "yarn-1.15.2+build.17-v2";

	public static boolean loadMapping()
	{
		InputStream inputStream = BundledMappingProvider.class.getClassLoader().getResourceAsStream(String.format("assets/carpettisaddition/%s.tiny", MAPPING_NAME));
		if (inputStream == null)
		{
			return false;
		}
		return StackTraceDeobfuscator.loadMappings(inputStream, MAPPING_NAME);
	}
}

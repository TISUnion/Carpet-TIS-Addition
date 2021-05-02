package carpettisaddition.utils.deobfuscator.yarn;

import carpettisaddition.utils.deobfuscator.StackTraceDeobfuscator;

import java.io.InputStream;

public class BundledMappingProvider
{
	private static final String MAPPING_NAME = "yarn-1.16.5+build.8-v2";

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

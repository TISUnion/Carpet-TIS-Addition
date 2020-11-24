package carpettisaddition.utils.stacktrace;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.translations.Translator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.fabricmc.mapping.reader.v2.MappingGetter;
import net.fabricmc.mapping.reader.v2.TinyMetadata;
import net.fabricmc.mapping.reader.v2.TinyV2Factory;
import net.fabricmc.mapping.reader.v2.TinyVisitor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class StackTraceDeobfuscator
{
	private static final String MAPPING_FILE_NAME = "yarn-1.16.4+build.7-v2.tiny";
	private static final Map<String, String> mappings = Maps.newHashMap();
	static final Translator translator = new Translator("util", "stack_trace");

	public static void loadMappings()
	{
		InputStream inputStream = StackTraceDeobfuscator.class.getClassLoader().getResourceAsStream("assets/carpettisaddition/" + MAPPING_FILE_NAME);
		if (inputStream != null)
		{
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			try (BufferedReader mappingReader = new BufferedReader(inputStreamReader))
			{
				TinyV2Factory.visit(mappingReader, new MappingVisitor(mappings));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		if (mappings.isEmpty())
		{
			CarpetTISAdditionServer.LOGGER.warn("Fail to load mapping");
		}
	}

	public static StackTraceElement[] deobfuscateStackTrace(StackTraceElement[] stackTraceElements, String ignoreClassPath)
	{
		List<StackTraceElement> list = Lists.newArrayList();
		for (StackTraceElement element : stackTraceElements)
		{
			String remappedClass = mappings.get(element.getClassName());
			String remappedMethod = mappings.get(element.getMethodName());
			StackTraceElement newElement = new StackTraceElement(
					remappedClass != null ? remappedClass : element.getClassName(),
					remappedMethod != null ? remappedMethod : element.getMethodName(),
					remappedClass != null ? getFileName(remappedClass) : element.getFileName(),
					element.getLineNumber()
			);
			list.add(newElement);
			if (ignoreClassPath != null && newElement.getClassName().startsWith(ignoreClassPath))
			{
				list.clear();
			}
		}
		list.add(0, new StackTraceElement(translator.tr("Deobfuscated stack trace"), "", MAPPING_FILE_NAME, -1));
		return list.toArray(new StackTraceElement[0]);
	}

	public static StackTraceElement[] deobfuscateStackTrace(StackTraceElement[] stackTraceElements)
	{
		return deobfuscateStackTrace(stackTraceElements, null);
	}

	private static String getFileName(String className)
	{
		if (className.isEmpty())
		{
			return className;
		}
		return className.substring(className.lastIndexOf('.') + 1).split("\\$",2)[0] + ".java";
	}

	private static class MappingVisitor implements TinyVisitor
	{
		private final Map<String, String> mappings;
		private int intermediaryIndex;
		private int namedIndex;

		public MappingVisitor(Map<String, String> mappings)
		{
			this.mappings = mappings;
		}

		private void putMappings(MappingGetter name)
		{
			String intermediaryName = name.get(this.intermediaryIndex).replace('/', '.');
			String remappedName = name.get(this.namedIndex).replace('/', '.');
			this.mappings.put(intermediaryName, remappedName);
		}

		@Override
		public void start(TinyMetadata metadata)
		{
			this.intermediaryIndex = metadata.index("intermediary");
			this.namedIndex = metadata.index("named");
		}

		@Override
		public void pushClass(MappingGetter name)
		{
			this.putMappings(name);
		}

		@Override
		public void pushField(MappingGetter name, String descriptor)
		{
			this.putMappings(name);
		}

		@Override
		public void pushMethod(MappingGetter name, String descriptor)
		{
			this.putMappings(name);
		}
	}
}

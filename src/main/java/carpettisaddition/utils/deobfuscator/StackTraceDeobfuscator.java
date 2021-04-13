package carpettisaddition.utils.deobfuscator;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.deobfuscator.yarn.BundledMappingProvider;
import carpettisaddition.utils.deobfuscator.yarn.OnlineMappingProvider;
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
	private static String MAPPING_VERSION = "unknown";
	private static final Map<String, String> mappings = Maps.newHashMap();
	static final Translator translator = new Translator("util", "stack_trace");

	public static void init()
	{
		if (!BundledMappingProvider.loadMapping())
		{
			CarpetTISAdditionServer.LOGGER.debug("Bundled mapping is not found, switched to online mapping provider");
			OnlineMappingProvider.getMapping();
		}
	}

	public static boolean loadMappings(InputStream inputStream, String mappingVersion)
	{
		try (BufferedReader mappingReader = new BufferedReader(new InputStreamReader(inputStream)))
		{
			TinyV2Factory.visit(mappingReader, new MappingVisitor(mappings));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (mappings.isEmpty())
		{
			CarpetTISAdditionServer.LOGGER.warn("Fail to load mapping {}", mappingVersion);
			return false;
		}
		else
		{
			MAPPING_VERSION = mappingVersion;
			return true;
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
		list.add(0, new StackTraceElement(translator.tr("Deobfuscated stack trace"), "", MAPPING_VERSION, -1));
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

package carpettisaddition.utils;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileUtil
{
	public static void prepareFileDirectories(File file) throws IOException
	{
		File dir = file.getParentFile();
		if (!dir.exists())
		{
			if (!dir.mkdirs())
			{
				throw new IOException("Directory creation failed");
			}
		}
		else if (!dir.isDirectory())
		{
			throw new IOException("Directory exists but it's not a directory");
		}
	}

	public static boolean isFile(File file)
	{
		return file.exists() && file.isFile();
	}

	public static boolean isFile(String fileName)
	{
		return isFile(new File(fileName));
	}

	public static String readResourceFileAsString(String path) throws IOException
	{
		InputStream inputStream = FileUtil.class.getClassLoader().getResourceAsStream(path);
		if (inputStream == null)
		{
			throw new IOException("Null input stream from path " + path);
		}
		return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
	}
}

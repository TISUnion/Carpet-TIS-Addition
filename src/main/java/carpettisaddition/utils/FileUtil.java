package carpettisaddition.utils;

import java.io.File;
import java.io.IOException;

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
}

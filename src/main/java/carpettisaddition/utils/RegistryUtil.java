package carpettisaddition.utils;

import carpettisaddition.CarpetTISAdditionMod;
import net.minecraft.util.Identifier;

public class RegistryUtil
{
	public static Identifier id(String namespace, String path)
	{
		return new Identifier(namespace, path);
	}

	public static Identifier id(String path)
	{
		return id(CarpetTISAdditionMod.MOD_ID, path);
	}
}

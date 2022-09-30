package carpettisaddition.utils;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public class NbtUtil
{
	public static CompoundTag stringList2Nbt(List<String> list)
	{
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("length", list.size());
		for (int i = 0; i < list.size(); i++)
		{
			nbt.putString(String.valueOf(i), list.get(i));
		}
		return nbt;
	}

	public static List<String> nbt2StringList(CompoundTag nbt)
	{
		List<String> list = Lists.newArrayList();
		int length = nbt.getInt("length");
		for (int i = 0; i < length; i++)
		{
			list.add(nbt.getString(String.valueOf(i)));
		}
		return list;
	}
}

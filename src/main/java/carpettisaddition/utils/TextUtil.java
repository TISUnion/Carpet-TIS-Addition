package carpettisaddition.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class TextUtil
{
	public static String tp(Vec3d pos) {return String.format("/tp %s %s %s", pos.getX(), pos.getY(), pos.getZ());}
	public static String tp(Vec3i pos) {return String.format("/tp %d %d %d", pos.getX(), pos.getY(), pos.getZ());}
	public static String tp(ChunkPos pos) {return String.format("/tp %d ~ %d", pos.x * 16 + 8, pos.z * 16 + 8);}
	public static String tp(Vec3d pos, DimensionWrapper dimensionType) {return String.format("/execute in %s run", dimensionType) + tp(pos).replace('/', ' ');}
	public static String tp(Vec3i pos, DimensionWrapper dimensionType) {return String.format("/execute in %s run", dimensionType) + tp(pos).replace('/', ' ');}
	public static String tp(ChunkPos pos, DimensionWrapper dimensionType) {return String.format("/execute in %s run", dimensionType) + tp(pos).replace('/', ' ');}

	public static String tp(Entity entity)
	{
		if (entity instanceof PlayerEntity)
		{
			String name = ((PlayerEntity)entity).getGameProfile().getName();
			return String.format("/tp %s", name);
		}
		String uuid = entity.getUuid().toString();
		return String.format("/tp %s", uuid);
	}

	public static String coord(Vec3d pos) {return String.format("[%.1f, %.1f, %.1f]", pos.getX(), pos.getY(), pos.getZ());}
	public static String coord(Vec3i pos) {return String.format("[%d, %d, %d]", pos.getX(), pos.getY(), pos.getZ());}
	public static String coord(ChunkPos pos) {return String.format("[%d, %d]", pos.x, pos.z);}
}

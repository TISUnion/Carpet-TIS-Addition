package carpettisaddition.helpers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.List;
import java.util.Set;


public class CustomStatsHelper
{
	public static final Identifier BREAK_BEDROCK;
	private static final Set<String> stats;

	private static void addStat(Identifier stat)
	{
		if (stat != null)
		{
			stats.add(stat.toString());
		}
	}

	static
	{
		stats = Sets.newHashSet();
		addStat(BREAK_BEDROCK = Registry.CUSTOM_STAT.getId(new Identifier("break_bedrock")));
	}

	public static void addStatsToNearestPlayers(World world, BlockPos blockPos, double radius, Identifier stat, int amount)
	{
		if (world.getServer() != null)
		{
			Vec3d pos = new Vec3d(blockPos).add(0.5D, 0.5D, 0.5D);
			world.getServer().getPlayerManager().getPlayerList().stream()
					.filter(
							player -> player.squaredDistanceTo(pos) <= radius * radius
					)
					.min(Comparator.comparingDouble(
							player -> player.squaredDistanceTo(pos)
					))
					.ifPresent(
							player -> player.increaseStat(stat, amount)
					);
		}
	}

	public static boolean isCustomStat(Stat<?> stat)
	{
		return stats.contains(stat.getValue().toString());
	}
}

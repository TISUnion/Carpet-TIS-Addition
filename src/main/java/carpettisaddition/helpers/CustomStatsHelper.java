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
	// Don't forget to mixin Stats class with StatsMixin
	public static final Identifier BREAK_BEDROCK;
	public static final Identifier FIREWORK_BOOST;
	private static final Set<String> customStats;

	private static void addStat(Identifier stat)
	{
		if (stat != null)
		{
			customStats.add(stat.toString());
		}
	}

	static
	{
		customStats = Sets.newHashSet();
		addStat(BREAK_BEDROCK = Registry.CUSTOM_STAT.getId(new Identifier("break_bedrock")));
		addStat(FIREWORK_BOOST = Registry.CUSTOM_STAT.getId(new Identifier("firework_boost")));
	}

	public static void addStatsToNearestPlayers(World world, BlockPos blockPos, double radius, Identifier stat, int amount)
	{
		if (world.getServer() != null)
		{
			Vec3d pos = Vec3d.ofCenter(blockPos);
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
		return customStats.contains(stat.getValue().toString());
	}
}

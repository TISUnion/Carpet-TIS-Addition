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

	public static void addStatsToNearestPlayers(World world, BlockPos pos, double radius, Identifier stat, int amount)
	{
		Comparator<Pair<Double, ServerPlayerEntity>> comparator = (a, b) ->
		{
			if (a.getFirst() < b.getFirst())
			{
				return -1;
			}
			else if (a.getFirst() > b.getFirst())
			{
				return 1;
			}
			return 0;
		};
		if (world.getServer() != null)
		{
			List<Pair<Double, ServerPlayerEntity>> list = Lists.newArrayList();
			for (ServerPlayerEntity player : world.getServer().getPlayerManager().getPlayerList())
				if (player.dimension == world.getDimension().getType() && !player.isSpectator())
				{
					double distance = player.squaredDistanceTo((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D);
					if (distance < radius * radius)
					{
						list.add(new Pair<>(distance, player));
					}
				}
			if (list.size() > 0)
			{
				list.sort(comparator);
				PlayerEntity player = list.get(0).getSecond();
				player.increaseStat(stat, amount);
			}
		}
	}

	public static boolean isCustomStat(Stat<?> stat)
	{
		return stats.contains(stat.getValue().toString());
	}
}

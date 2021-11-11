package carpettisaddition.utils;

import carpettisaddition.CarpetTISAdditionServer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class GameUtil
{
	public static long getGameTime()
	{
		return Objects.requireNonNull(CarpetTISAdditionServer.minecraft_server.getWorld(World.OVERWORLD)).getTime();
	}

	public static boolean isOnServerThread()
	{
		return CarpetTISAdditionServer.minecraft_server != null && CarpetTISAdditionServer.minecraft_server.isOnThread();
	}

	/**
	 * See the exit point for the looping in {@link SpawnHelper#setupSpawn}
	 */
	public static boolean countsTowardsMobcap(Entity entity)
	{
		if (entity instanceof MobEntity)
		{
			MobEntity mobEntity = (MobEntity)entity;
			return !mobEntity.isPersistent() && !mobEntity.cannotDespawn();
		}
		return false;
	}

	/**
	 * Return a BlockPos that is out of the world limit
	 */
	public static BlockPos getInvalidBlockPos()
	{
		return new BlockPos(0, -1024, 0);
	}

	@Nullable
	public static PlayerEntity getPlayerFromName(String playerName)
	{
		return CarpetTISAdditionServer.minecraft_server.getPlayerManager().getPlayer(playerName);
	}
}

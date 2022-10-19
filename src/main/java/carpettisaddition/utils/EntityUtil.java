package carpettisaddition.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityUtil
{
	public static PlayerAbilities getPlayerAbilities(@NotNull PlayerEntity player)
	{
		//#if MC >= 11700
		//$$ return player.getAbilities();
		//#else
		return player.abilities;
		//#endif
	}

	public static boolean isCreativePlayer(@Nullable Entity entity)
	{
		if (entity instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity)entity;
			return player.isCreative();
		}
		return false;
	}

	public static boolean isFlyingCreativePlayer(@Nullable Entity entity)
	{
		if (entity instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity)entity;
			return isCreativePlayer(entity) && getPlayerAbilities(player).flying;
		}
		return false;
	}

	public static float getYaw(@NotNull Entity entity)
	{
		//#if MC >= 11700
		//$$ return entity.getYaw();
		//#else
		return entity.yaw;
		//#endif
	}

	public static float getPitch(@NotNull Entity entity)
	{
		//#if MC >= 11700
		//$$ return entity.getPitch();
		//#else
		return entity.pitch;
		//#endif
	}
}

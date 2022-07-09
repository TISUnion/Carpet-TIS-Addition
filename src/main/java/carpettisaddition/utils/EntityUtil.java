package carpettisaddition.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;

public class EntityUtil
{
	public static PlayerAbilities getPlayerAbilities(PlayerEntity player)
	{
		//#if MC >= 11700
		//$$ return player.getAbilities();
		//#else
		return player.abilities;
		//#endif
	}

	public static boolean isFlyingCreativePlayer(Entity entity)
	{
		if (entity instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity)entity;
			return player.isCreative() && getPlayerAbilities(player).flying;
		}
		return false;
	}

	public static float getYaw(Entity entity)
	{
		//#if MC >= 11700
		//$$ return entity.getYaw();
		//#else
		return entity.yaw;
		//#endif
	}

	public static float getPitch(Entity entity)
	{
		//#if MC >= 11700
		//$$ return entity.getPitch();
		//#else
		return entity.pitch;
		//#endif
	}
}

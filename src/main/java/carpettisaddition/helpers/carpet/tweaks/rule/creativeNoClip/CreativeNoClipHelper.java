package carpettisaddition.helpers.carpet.tweaks.rule.creativeNoClip;

//#if MC >= 11500
import carpet.CarpetSettings;
//#else
//$$ import carpettisaddition.utils.compat.carpet.CarpetSettings;
//#endif

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class CreativeNoClipHelper
{
	public static final ThreadLocal<Boolean> ignoreNoClipPlayersFlag = ThreadLocal.withInitial(() -> false);

	public static boolean isNoClipPlayer(Entity entity)
	{
		if (CarpetSettings.creativeNoClip && entity instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) entity;
			return player.isCreative() && player.abilities.flying;
		}
		return false;
	}
}

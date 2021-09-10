package carpettisaddition.helpers.carpet.tweaks.rule.creativeNoClip;

import carpet.CarpetSettings;
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
			return player.isCreative() && player.getAbilities().flying;
		}
		return false;
	}
}

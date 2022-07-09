package carpettisaddition.helpers.carpet.tweaks.rule.creativeNoClip;

//#if MC >= 11500
import carpet.CarpetSettings;
//#else
//$$ import carpettisaddition.utils.compat.carpet.CarpetSettings;
//#endif

import carpettisaddition.utils.EntityUtil;
import net.minecraft.entity.Entity;

public class CreativeNoClipHelper
{
	public static final ThreadLocal<Boolean> ignoreNoClipPlayersFlag = ThreadLocal.withInitial(() -> false);

	public static boolean isNoClipPlayer(Entity entity)
	{
		return CarpetSettings.creativeNoClip && EntityUtil.isFlyingCreativePlayer(entity);
	}
}

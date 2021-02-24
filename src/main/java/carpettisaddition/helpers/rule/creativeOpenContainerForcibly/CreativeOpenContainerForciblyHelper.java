package carpettisaddition.helpers.rule.creativeOpenContainerForcibly;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.player.PlayerEntity;

public class CreativeOpenContainerForciblyHelper
{
	public static boolean canOpenForcibly(PlayerEntity player)
	{
		return CarpetTISAdditionSettings.creativeOpenContainerForcibly && player.isCreative();
	}
}

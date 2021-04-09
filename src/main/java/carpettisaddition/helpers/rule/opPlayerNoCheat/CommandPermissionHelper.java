package carpettisaddition.helpers.rule.opPlayerNoCheat;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;


public class CommandPermissionHelper
{
	public static boolean canCheat(ServerCommandSource source, int level)
	{
		return source.hasPermissionLevel(level) && !((source.getEntity() instanceof ServerPlayerEntity) && CarpetTISAdditionSettings.opPlayerNoCheat);
	}
}

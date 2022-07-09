package carpettisaddition.logging.loggers.lifetime;

//#if MC >= 11500
import carpet.logging.HUDLogger;
//#else
//$$ import carpettisaddition.logging.compat.ExtensionHUDLogger;
//#endif

import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.commands.lifetime.utils.LifeTimeTrackerUtil;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import net.minecraft.entity.player.PlayerEntity;

public class LifeTimeStandardCarpetHUDLogger extends
		//#if MC >= 11500
		HUDLogger
		//#else
		//$$ ExtensionHUDLogger
		//#endif
{
	public LifeTimeStandardCarpetHUDLogger()
	{
		super(
				TISAdditionLoggerRegistry.getLoggerField(LifeTimeHUDLogger.NAME), LifeTimeHUDLogger.NAME, null, null
				//#if MC >= 11700
				//$$ , false
				//#endif
		);
	}

	@Override
	public void addPlayer(String playerName, String option)
	{
		super.addPlayer(playerName, option);
		PlayerEntity player = this.playerFromName(playerName);
		if (player != null)
		{
			if (!LifeTimeTrackerUtil.getEntityTypeFromName(option).isPresent())
			{
				LifeTimeTracker.getInstance().sendUnknownEntity(player.getCommandSource(), option);
			}
		}
	}

	@Override
	public String[] getOptions()
	{
		return LifeTimeTracker.getInstance().getAvailableEntityType().toArray(String[]::new);
	}
}

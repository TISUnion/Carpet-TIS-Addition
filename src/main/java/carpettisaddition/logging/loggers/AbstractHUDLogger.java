package carpettisaddition.logging.loggers;

import carpet.logging.HUDLogger;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.BaseText;

public abstract class AbstractHUDLogger extends AbstractLogger
{
	public AbstractHUDLogger(String name, boolean strictOption)
	{
		super(name, strictOption);
	}

	public abstract BaseText[] onHudUpdate(String option, PlayerEntity playerEntity);

	@Override
	public HUDLogger createCarpetLogger()
	{
		return TISAdditionLoggerRegistry.standardHUDLogger(
				this.getName(), this.getDefaultLoggingOption(), this.getSuggestedLoggingOption()
				//#if MC >= 11700
				//$$ , true
				//#endif
		);
	}
}

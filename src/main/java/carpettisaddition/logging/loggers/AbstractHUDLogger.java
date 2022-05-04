package carpettisaddition.logging.loggers;

import carpet.logging.HUDLogger;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;

public abstract class AbstractHUDLogger extends AbstractLogger
{
	public AbstractHUDLogger(String name)
	{
		super(name);
	}

	public abstract MutableText[] onHudUpdate(String option, PlayerEntity playerEntity);

	@Override
	public HUDLogger createCarpetLogger()
	{
		return TISAdditionLoggerRegistry.standardHUDLogger(this.getName(), this.getDefaultLoggingOption(), this.getSuggestedLoggingOption(), true);
	}
}

package carpettisaddition.logging.loggers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.BaseText;

public abstract class AbstractHUDLogger extends AbstractLogger
{
	public AbstractHUDLogger(String name)
	{
		super(name);
	}

	public abstract BaseText[] onHudUpdate(String option, PlayerEntity playerEntity);
}

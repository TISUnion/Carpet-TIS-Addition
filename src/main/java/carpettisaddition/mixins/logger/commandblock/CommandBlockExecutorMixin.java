package carpettisaddition.mixins.logger.commandblock;

import carpettisaddition.logging.loggers.commandblock.CommandBlockLogger;
import carpettisaddition.logging.loggers.commandblock.ICommandBlockExecutor;
import net.minecraft.world.CommandBlockExecutor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CommandBlockExecutor.class)
public abstract class CommandBlockExecutorMixin implements ICommandBlockExecutor
{
	private long lastLoggedTime = -CommandBlockLogger.MINIMUM_LOG_INTERVAL;

	@Override
	public long getLastLoggedTime()
	{
		return this.lastLoggedTime;
	}

	@Override
	public void setLastLoggedTime(long time)
	{
		this.lastLoggedTime = time;
	}
}

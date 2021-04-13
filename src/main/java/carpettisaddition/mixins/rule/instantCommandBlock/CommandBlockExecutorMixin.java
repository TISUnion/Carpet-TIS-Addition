package carpettisaddition.mixins.rule.instantCommandBlock;

import carpettisaddition.helpers.rule.instantCommandBlock.ICommandBlockExecutor;
import net.minecraft.world.CommandBlockExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CommandBlockExecutor.class)
public abstract class CommandBlockExecutorMixin implements ICommandBlockExecutor
{
	@Shadow private long lastExecution;

	private boolean	ignoreWorldTimeCheck = false;

	@Override
	public void setIgnoreWorldTimeCheck(boolean ignoreWorldTimeCheck)
	{
		this.ignoreWorldTimeCheck = ignoreWorldTimeCheck;
	}

	@Redirect(
			method = "execute",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/world/CommandBlockExecutor;lastExecution:J",
					ordinal = 0
			)
	)
	private long dontCheckLastExecutionTimeIfItsInstant(CommandBlockExecutor executor)
	{
		if (this.ignoreWorldTimeCheck)
		{
			return -1;
		}
		return this.lastExecution;
	}
}

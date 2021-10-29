package carpettisaddition.mixins.logger.microtiming;

import carpettisaddition.logging.loggers.microtiming.interfaces.ITileTickListWithServerWorld;
import net.minecraft.class_6757;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

// ServerTickScheduler in 1.17
@Mixin(class_6757.class)
public abstract class ServerTickSchedulerMixin implements ITileTickListWithServerWorld
{
	private ServerWorld serverWorld$CTA;

	@Override
	@Nullable
	public ServerWorld getServerWorld()
	{
		return this.serverWorld$CTA;
	}

	@Override
	public void setServerWorld(ServerWorld serverWorld$CTA)
	{
		this.serverWorld$CTA = serverWorld$CTA;
	}
}

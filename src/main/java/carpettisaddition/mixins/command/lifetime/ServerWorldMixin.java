package carpettisaddition.mixins.command.lifetime;

import carpettisaddition.commands.lifetime.LifeTimeWorldTracker;
import carpettisaddition.commands.lifetime.interfaces.ServerWorldWithLifeTimeTracker;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin implements ServerWorldWithLifeTimeTracker
{
	private LifeTimeWorldTracker lifeTimeWorldTracker;

	@Inject(method = "<init>", at = @At(value = "RETURN"))
	private void onConstructLifeTimeLogger(CallbackInfo ci)
	{
		this.lifeTimeWorldTracker = new LifeTimeWorldTracker((ServerWorld)(Object)this);
	}

	@Override
	public LifeTimeWorldTracker getLifeTimeWorldTracker()
	{
		return this.lifeTimeWorldTracker;
	}
}

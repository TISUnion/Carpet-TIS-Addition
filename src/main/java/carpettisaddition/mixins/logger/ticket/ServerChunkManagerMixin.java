package carpettisaddition.mixins.logger.ticket;

import carpettisaddition.logging.loggers.ticket.IChunkTicketManager;
import net.minecraft.server.world.ChunkTicketManager;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerChunkManager.class)
public abstract class ServerChunkManagerMixin
{
	@Shadow @Final private ChunkTicketManager ticketManager;

	@Shadow @Final ServerWorld world;

	@Inject(method = "<init>", at = @At(value = "RETURN"))
	private void onConstructedTicketLogger(CallbackInfo ci)
	{
		((IChunkTicketManager)this.ticketManager).setServerWorld(this.world);
	}
}

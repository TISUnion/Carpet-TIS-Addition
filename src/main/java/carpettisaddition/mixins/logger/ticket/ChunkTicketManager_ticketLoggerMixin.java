package carpettisaddition.mixins.logger.ticket;

import carpettisaddition.interfaces.IChunkTicketManager;
import carpettisaddition.logging.logHelpers.TicketLogHelper;
import net.minecraft.server.world.ChunkTicket;
import net.minecraft.server.world.ChunkTicketManager;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ChunkTicketManager.class)
public abstract class ChunkTicketManager_ticketLoggerMixin implements IChunkTicketManager
{
	private ServerWorld world;

	@Override
	public void setServerWorld(ServerWorld world)
	{
		this.world = world;
	}

	@Inject(
			method = "addTicket(JLnet/minecraft/server/world/ChunkTicket;)V",
			at = @At(value = "HEAD")
	)
	private void onAddTicket(long position, ChunkTicket<?> chunkTicket, CallbackInfo ci)
	{
		TicketLogHelper.onAddTicket(this.world, position, chunkTicket);
	}

	@Inject(
			method = "removeTicket(JLnet/minecraft/server/world/ChunkTicket;)V",
			at = @At(value = "HEAD")
	)
	private void onRemoveTicket(long position, ChunkTicket<?> chunkTicket, CallbackInfo ci)
	{
		TicketLogHelper.onRemoveTicket(this.world, position, chunkTicket);
	}
}

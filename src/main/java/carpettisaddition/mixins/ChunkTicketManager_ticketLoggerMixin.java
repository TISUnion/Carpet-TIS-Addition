package carpettisaddition.mixins;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import net.minecraft.server.world.ChunkTicket;
import net.minecraft.server.world.ChunkTicketManager;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;


@Mixin(ChunkTicketManager.class)
public abstract class ChunkTicketManager_ticketLoggerMixin
{
	@Inject(method = "addTicket", at = @At(value = "HEAD"))
	private void onAddTicket(long position, ChunkTicket<?> chunkTicket, CallbackInfo ci)
	{
		LoggerRegistry.getLogger("ticket").log((option) ->
		{
			if (Arrays.asList(option.split(",")).contains(chunkTicket.getType().toString()))
			{
				ChunkPos pos = new ChunkPos(position);
				long expiryTicks = chunkTicket.getType().getExpiryTicks();
				return new BaseText[]{Messenger.c(
						String.format("w Ticket %s ", chunkTicket.getType()),
						String.format("^w Level = %d\nDuration = %s", chunkTicket.getLevel(), expiryTicks > 0 ? expiryTicks + " gt" : "Permanent"),
						"g @ ",
						String.format("w [%d, %d]", pos.x, pos.z)
				)};
			}
			else
			{
				return null;
			}
		});
	}
}

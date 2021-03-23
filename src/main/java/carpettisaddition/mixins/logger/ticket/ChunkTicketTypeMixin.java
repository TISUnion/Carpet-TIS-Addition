package carpettisaddition.mixins.logger.ticket;

import carpettisaddition.logging.loggers.ticket.TicketLogger;
import net.minecraft.server.world.ChunkTicketType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkTicketType.class)
public abstract class ChunkTicketTypeMixin<T>
{
	@SuppressWarnings({"ConstantConditions", "unchecked"})
	@Inject(method = "<init>", at = @At("TAIL"))
	private void recordTicketType(CallbackInfo ci)
	{
		TicketLogger.getInstance().addTicketType((ChunkTicketType<T>)(Object)this);
	}
}

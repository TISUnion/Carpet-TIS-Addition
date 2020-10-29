package carpettisaddition.logging.loggers.microtiming.tickstages;

import carpet.utils.Messenger;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;

public class PlayerEntityTickStageExtra extends TickStageExtraBase
{
	private final ServerPlayerEntity player;

	public PlayerEntityTickStageExtra(ServerPlayerEntity player)
	{
		this.player = player;
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				String.format("w %s\n", this.tr("Ticking player entity")),
				String.format("w %s: %s", this.tr("Player"), this.player.getGameProfile().getName())
		);
	}
}

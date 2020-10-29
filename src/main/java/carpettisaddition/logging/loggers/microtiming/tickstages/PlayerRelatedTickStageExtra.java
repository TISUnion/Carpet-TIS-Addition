package carpettisaddition.logging.loggers.microtiming.tickstages;

import carpet.utils.Messenger;
import carpettisaddition.utils.Util;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;

public abstract class PlayerRelatedTickStageExtra extends TickStageExtraBase
{
	protected final ServerPlayerEntity player;

	public PlayerRelatedTickStageExtra(ServerPlayerEntity player)
	{
		this.player = player;
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				String.format("w %s: %s", this.tr("Player"), this.player.getGameProfile().getName())
		);
	}

	@Override
	public ClickEvent getClickEvent()
	{
		return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Util.getTeleportCommand(this.player));
	}
}

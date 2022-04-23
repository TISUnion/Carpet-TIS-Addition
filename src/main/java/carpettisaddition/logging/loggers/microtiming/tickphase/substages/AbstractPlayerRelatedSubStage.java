package carpettisaddition.logging.loggers.microtiming.tickphase.substages;

import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.TextUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;

public abstract class AbstractPlayerRelatedSubStage extends AbstractSubStage
{
	protected final ServerPlayerEntity player;

	public AbstractPlayerRelatedSubStage(ServerPlayerEntity player)
	{
		this.player = player;
	}

	@Override
	public MutableText toText()
	{
		return Messenger.c(
				tr("player"),
				String.format("w : %s", this.player.getGameProfile().getName())
		);
	}

	@Override
	public ClickEvent getClickEvent()
	{
		return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.tp(this.player));
	}
}

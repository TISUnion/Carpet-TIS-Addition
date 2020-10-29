package carpettisaddition.logging.loggers.microtiming.tickstages;

import carpet.utils.Messenger;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;

public class PlayerEntityTickStageExtra extends PlayerRelatedTickStageExtra
{
	public PlayerEntityTickStageExtra(ServerPlayerEntity player)
	{
		super(player);
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				String.format("w %s\n", this.tr("Ticking player entity")),
				super.toText()
		);
	}
}

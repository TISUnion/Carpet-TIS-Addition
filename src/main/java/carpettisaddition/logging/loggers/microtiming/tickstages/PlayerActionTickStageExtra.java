package carpettisaddition.logging.loggers.microtiming.tickstages;

import carpet.utils.Messenger;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;

public class PlayerActionTickStageExtra extends PlayerRelatedTickStageExtra
{
	public PlayerActionTickStageExtra(ServerPlayerEntity player)
	{
		super(player);
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				String.format("w %s\n", this.tr("sync_tasks", "Sync task executions in main thread including player actions")),
				super.toText()
		);
	}
}

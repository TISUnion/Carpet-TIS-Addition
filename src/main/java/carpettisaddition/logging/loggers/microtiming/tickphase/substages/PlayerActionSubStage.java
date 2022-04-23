package carpettisaddition.logging.loggers.microtiming.tickphase.substages;

import carpettisaddition.utils.Messenger;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

public class PlayerActionSubStage extends AbstractPlayerRelatedSubStage
{
	public PlayerActionSubStage(ServerPlayerEntity player)
	{
		super(player);
	}

	@Override
	public MutableText toText()
	{
		return Messenger.c(
				tr("player_action"), Messenger.newLine(),
				super.toText()
		);
	}
}

package carpettisaddition.logging.loggers.microtiming.tickphase.substages;

import carpet.utils.Messenger;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;

public class PlayerActionSubStage extends AbstractPlayerRelatedSubStage
{
	public PlayerActionSubStage(ServerPlayerEntity player)
	{
		super(player);
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				String.format("w %s\n", this.tr("player_action", "Executing player actions sent by clients")),
				super.toText()
		);
	}
}

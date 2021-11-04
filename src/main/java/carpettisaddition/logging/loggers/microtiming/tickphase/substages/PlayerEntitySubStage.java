package carpettisaddition.logging.loggers.microtiming.tickphase.substages;

import carpettisaddition.utils.Messenger;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;

public class PlayerEntitySubStage extends AbstractPlayerRelatedSubStage
{
	public PlayerEntitySubStage(ServerPlayerEntity player)
	{
		super(player);
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				tr("ticking_player_entity"), Messenger.newLine(),
				super.toText()
		);
	}
}

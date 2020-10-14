package carpettisaddition.logging.loggers.microtick.events;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.MicroTickUtil;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import net.minecraft.server.world.BlockAction;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class ScheduleBlockEventEvent extends BaseEvent
{
	private final BlockAction blockAction;

	public ScheduleBlockEventEvent(World world, BlockAction blockAction)
	{
		super(world, EventType.EVENT, "schedule_block_event");
		this.blockAction = blockAction;
	}

	@Override
	public Text toText()
	{
		return Messenger.c(
				MicroTickUtil.getTranslatedName(blockAction.getBlock()),
				"q  Scheduled",
				"c  BlockEvent",
				ExecuteBlockEventEvent.getMessageExtra(blockAction)
		);
	}
}

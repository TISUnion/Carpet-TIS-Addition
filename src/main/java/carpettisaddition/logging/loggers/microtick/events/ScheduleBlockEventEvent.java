package carpettisaddition.logging.loggers.microtick.events;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import carpettisaddition.logging.loggers.microtick.utils.MicroTickUtil;
import net.minecraft.server.world.BlockAction;
import net.minecraft.text.BaseText;

import java.util.Objects;

public class ScheduleBlockEventEvent extends BaseEvent
{
	private final BlockAction blockAction;

	public ScheduleBlockEventEvent(BlockAction blockAction)
	{
		super(EventType.EVENT, "schedule_block_event");
		this.blockAction = blockAction;
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				MicroTickUtil.getTranslatedName(blockAction.getBlock()),
				"q  Scheduled",
				"c  BlockEvent",
				ExecuteBlockEventEvent.getMessageExtra(blockAction)
		);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ScheduleBlockEventEvent)) return false;
		if (!super.equals(o)) return false;
		ScheduleBlockEventEvent that = (ScheduleBlockEventEvent) o;
		return Objects.equals(blockAction, that.blockAction);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), blockAction);
	}
}

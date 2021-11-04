package carpettisaddition.logging.loggers.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.utils.Messenger;
import net.minecraft.server.world.BlockEvent;
import net.minecraft.text.BaseText;

import java.util.Objects;

public class ScheduleBlockEventEvent extends BaseEvent
{
	private final BlockEvent blockAction;
	private final boolean success;

	public ScheduleBlockEventEvent(BlockEvent blockAction, boolean success)
	{
		super(EventType.EVENT, "schedule_block_event", blockAction.block());
		this.blockAction = blockAction;
		this.success = success;
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				Messenger.formatting(tr("scheduled"), COLOR_ACTION),
				Messenger.getSpaceText(),
				Messenger.fancy(
						Messenger.formatting(tr("blockevent"), COLOR_TARGET),
						ExecuteBlockEventEvent.getMessageExtraMessengerHoverText(blockAction),
						null
				),
				"w  ",
				MicroTimingUtil.getSuccessText(this.success, false)
		);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ScheduleBlockEventEvent)) return false;
		if (!super.equals(o)) return false;
		ScheduleBlockEventEvent that = (ScheduleBlockEventEvent) o;
		return success == that.success &&
				Objects.equals(blockAction, that.blockAction);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), blockAction);
	}
}

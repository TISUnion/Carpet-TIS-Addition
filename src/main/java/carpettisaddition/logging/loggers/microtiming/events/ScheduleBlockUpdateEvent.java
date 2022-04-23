package carpettisaddition.logging.loggers.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.Direction;

import java.util.List;

public class ScheduleBlockUpdateEvent extends AbstractBlockUpdateEvent
{
	public ScheduleBlockUpdateEvent(Block sourceBlock, BlockUpdateType blockUpdateType, Direction exceptSide)
	{
		super(EventType.EVENT, "schedule_block_update", sourceBlock, blockUpdateType, exceptSide);
	}

	@Override
	public MutableText toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(Messenger.formatting(tr("scheduled"), COLOR_ACTION));
		list.add(Messenger.getSpaceText());
		list.add(Messenger.fancy(
				Messenger.formatting(this.blockUpdateType.toText(), COLOR_TARGET),
				this.getUpdateTypeExtraMessage(),
				null
		));
		return Messenger.c(list.toArray(new Object[0]));
	}
}

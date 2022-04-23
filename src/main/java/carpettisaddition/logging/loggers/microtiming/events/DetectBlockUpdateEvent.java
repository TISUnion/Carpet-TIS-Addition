package carpettisaddition.logging.loggers.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.Direction;

import java.util.List;

public class DetectBlockUpdateEvent extends AbstractBlockUpdateEvent
{
	public DetectBlockUpdateEvent(EventType eventType, Block sourceBlock, BlockUpdateType blockUpdateType, Direction exceptSide)
	{
		super(eventType, "detect_block_update", sourceBlock, blockUpdateType, exceptSide);
	}

	@Override
	public MutableText toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(Messenger.formatting(tr("emit"), COLOR_ACTION));
		list.add(Messenger.getSpaceText());
		list.add(Messenger.fancy(
				Messenger.formatting(this.blockUpdateType.toText(), COLOR_TARGET),
				this.getUpdateTypeExtraMessage(),
				null
		));
		list.add(Messenger.getSpaceText());
		switch (this.getEventType())
		{
			case ACTION_START:
				list.add(Messenger.formatting(tr("started"), COLOR_RESULT));
				break;
			case ACTION_END:
				list.add(Messenger.formatting(tr("ended"), COLOR_RESULT));
				break;
			default:
				list.add(Messenger.formatting(tr("detected"), COLOR_RESULT));
				break;
		}
		return Messenger.c(list.toArray(new Object[0]));
	}
}

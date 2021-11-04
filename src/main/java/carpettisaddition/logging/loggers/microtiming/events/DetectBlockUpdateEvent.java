package carpettisaddition.logging.loggers.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.text.BaseText;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class DetectBlockUpdateEvent extends BaseEvent
{
	private final BlockUpdateType updateType;
	private final Supplier<BaseText> updateTypeExtraMessage;
	private BaseText updateTypeExtraMessageCache;
	private final Block fromBlock;

	public DetectBlockUpdateEvent(EventType eventType, Block fromBlock, BlockUpdateType blockUpdateType, Supplier<BaseText> updateTypeExtraMessage)
	{
		super(eventType, "detect_block_update", fromBlock);
		this.fromBlock = fromBlock;
		this.updateType = blockUpdateType;
		this.updateTypeExtraMessage = updateTypeExtraMessage;
		this.updateTypeExtraMessageCache = null;
	}

	private BaseText getUpdateTypeExtraMessage()
	{
		if (this.updateTypeExtraMessageCache == null)
		{
			this.updateTypeExtraMessageCache = this.updateTypeExtraMessage.get();
		}
		return this.updateTypeExtraMessageCache;
	}

	@Override
	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(Messenger.formatting(tr("emit"), COLOR_ACTION));
		list.add(Messenger.getSpaceText());
		list.add(Messenger.fancy(Messenger.formatting(this.updateType.toText(), COLOR_TARGET), this.getUpdateTypeExtraMessage(), null));
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

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof DetectBlockUpdateEvent)) return false;
		if (!super.equals(o)) return false;
		DetectBlockUpdateEvent that = (DetectBlockUpdateEvent) o;
		return updateType == that.updateType &&
				Objects.equals(this.getUpdateTypeExtraMessage(), that.getUpdateTypeExtraMessage()) &&
				Objects.equals(fromBlock, that.fromBlock);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), updateType, this.getUpdateTypeExtraMessage(), fromBlock);
	}
}

package carpettisaddition.logging.loggers.microtick.events;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtick.enums.EventType;
import carpettisaddition.utils.Util;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.text.BaseText;

import java.util.List;
import java.util.Objects;

public class DetectBlockUpdateEvent extends BaseEvent
{
	private final BlockUpdateType updateType;
	private final String updateTypeExtraMessage;
	private final Block fromBlock;

	public DetectBlockUpdateEvent(EventType eventType, Block fromBlock, BlockUpdateType blockUpdateType, String updateTypeExtraMessage)
	{
		super(eventType, "detect_block_update");
		this.fromBlock = fromBlock;
		this.updateType = blockUpdateType;
		this.updateTypeExtraMessage = updateTypeExtraMessage;
	}

	@Override
	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(this.getEnclosedTranslatedBlockNameHeaderText(this.fromBlock));
		list.add("c " + MicroTickLoggerManager.tr("Emit"));
		list.add(Util.getSpaceText());
		list.add("c " + this.updateType);
		list.add("^w " + this.updateTypeExtraMessage);
		list.add(Util.getSpaceText());
		list.add("e " + this.tr("Detected"));
		if (this.getEventType() == EventType.ACTION_END)
		{
			list.add(Util.getSpaceText());
			list.add("c " + MicroTickLoggerManager.tr("ended"));
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
				Objects.equals(updateTypeExtraMessage, that.updateTypeExtraMessage) &&
				Objects.equals(fromBlock, that.fromBlock);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), updateType, updateTypeExtraMessage, fromBlock);
	}
}

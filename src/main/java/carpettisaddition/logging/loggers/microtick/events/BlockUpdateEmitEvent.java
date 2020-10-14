package carpettisaddition.logging.loggers.microtick.events;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.types.BlockUpdateType;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import carpettisaddition.logging.loggers.microtick.utils.MicroTickUtil;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.text.BaseText;

import java.util.List;
import java.util.Objects;

public class BlockUpdateEmitEvent extends BaseEvent
{
	private final BlockUpdateType updateType;
	private final String updateTypeExtraMessage;
	private final Block fromBlock;

	public BlockUpdateEmitEvent(EventType eventType, Block fromBlock, BlockUpdateType blockUpdateType, String updateTypeExtraMessage)
	{
		super(eventType, "block_update_emit");
		this.fromBlock = fromBlock;
		this.updateType = blockUpdateType;
		this.updateTypeExtraMessage = updateTypeExtraMessage;
	}

	@Override
	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(MicroTickUtil.getTranslatedName(this.fromBlock));
		list.add(String.format("c  %s", this.updateType));
		list.add(String.format("^w %s", this.updateTypeExtraMessage));
		if (this.getEventType() == EventType.ACTION_END)
		{
			list.add("q  ended");
		}
		return Messenger.c(list.toArray(new Object[0]));
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof BlockUpdateEmitEvent)) return false;
		if (!super.equals(o)) return false;
		BlockUpdateEmitEvent that = (BlockUpdateEmitEvent) o;
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

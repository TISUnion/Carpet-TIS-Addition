package carpettisaddition.logging.loggers.microtick.events;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import carpettisaddition.logging.loggers.microtick.utils.MicroTickUtil;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.text.BaseText;

import java.util.List;

public class EmitBlockUpdateEvent extends BaseEvent
{
	private final Block block;
	private final String methodName;
	public EmitBlockUpdateEvent(EventType eventType, Block block, String methodName)
	{
		super(eventType, "emit_block_update");
		this.block = block;
		this.methodName = methodName;
	}

	@Override
	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(MicroTickUtil.getTranslatedName(this.block));
		list.add(String.format("q  %s", "Emit "));
		list.add(String.format("c  %s", "BlockUpdates"));
		if (this.methodName != null)
		{
			list.add(String.format("^w Method name (yarn): %s", this.methodName));
		}
		if (this.getEventType() == EventType.ACTION_END)
		{
			list.add("q  ended");
		}
		return Messenger.c(list.toArray(new Object[0]));
	}
}

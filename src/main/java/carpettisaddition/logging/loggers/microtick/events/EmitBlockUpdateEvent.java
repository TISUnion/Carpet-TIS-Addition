package carpettisaddition.logging.loggers.microtick.events;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import carpettisaddition.utils.Util;
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
	public boolean isImportant()
	{
		return false;
	}

	@Override
	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(this.getEnclosedTranslatedBlockNameHeaderText(this.block));
		list.add("c " + this.tr("Emit Updates"));
		if (this.methodName != null)
		{
			list.add(String.format("^w %s: %s", this.tr("method_name", "Method name (yarn)"), this.methodName));
		}
		if (this.getEventType() == EventType.ACTION_END)
		{
			list.add(Util.getSpaceText());
			list.add("c " + MicroTickLoggerManager.tr("ended"));
		}
		return Messenger.c(list.toArray(new Object[0]));
	}
}

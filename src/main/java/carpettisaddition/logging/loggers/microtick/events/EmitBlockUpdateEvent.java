package carpettisaddition.logging.loggers.microtick.events;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.enums.EventType;
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
		list.add(COLOR_ACTION + this.tr("Emit"));
		list.add(Util.getSpaceText());
		list.add(COLOR_TARGET + this.tr("Updates"));
		if (this.methodName != null)
		{
			list.add(String.format("^w %s: %s", this.tr("method_name", "Method name (yarn)"), this.methodName));
		}
		switch (this.getEventType())
		{
			case ACTION_START:
				list.add(Util.getSpaceText());
				list.add(COLOR_RESULT + MicroTickLoggerManager.tr("started"));
				break;
			case ACTION_END:
				list.add(Util.getSpaceText());
				list.add(COLOR_RESULT + MicroTickLoggerManager.tr("ended"));
				break;
			default:
				break;
		}
		return Messenger.c(list.toArray(new Object[0]));
	}
}

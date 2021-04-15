package carpettisaddition.logging.loggers.microtiming.events;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.text.BaseText;

import java.util.List;
import java.util.Objects;

public class EmitBlockUpdateEvent extends BaseEvent
{
	private final String methodName;

	public EmitBlockUpdateEvent(EventType eventType, Block block, String methodName)
	{
		super(eventType, "emit_block_update", block);
		this.methodName = methodName;
	}

	@Override
	public boolean isImportant()
	{
		return false;
	}

	protected BaseText getUpdatesTextHoverText()
	{
		return Messenger.s(String.format("%s: %s", this.tr("method_name", "Method name (yarn)"), this.methodName));
	}

	@Override
	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		BaseText updatesText = Messenger.c(
				COLOR_ACTION + this.tr("Emit"),
				TextUtil.getSpaceText(),
				COLOR_TARGET + this.tr("Updates")
		);
		if (this.methodName != null)
		{
			TextUtil.attachHoverText(updatesText, this.getUpdatesTextHoverText());
		}
		list.add(updatesText);
		switch (this.getEventType())
		{
			case ACTION_START:
				list.add(TextUtil.getSpaceText());
				list.add(COLOR_RESULT + MicroTimingLoggerManager.tr("started"));
				break;
			case ACTION_END:
				list.add(TextUtil.getSpaceText());
				list.add(COLOR_RESULT + MicroTimingLoggerManager.tr("ended"));
				break;
			default:
				break;
		}
		return Messenger.c(list.toArray(new Object[0]));
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof EmitBlockUpdateEvent)) return false;
		if (!super.equals(o)) return false;
		EmitBlockUpdateEvent that = (EmitBlockUpdateEvent) o;
		return Objects.equals(block, that.block) &&
				Objects.equals(methodName, that.methodName);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), block, methodName);
	}
}

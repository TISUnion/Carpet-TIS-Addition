package carpettisaddition.logging.loggers.microtiming.events;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.enums.PistonBlockEventType;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.logging.loggers.microtiming.utils.ToTextAble;
import carpettisaddition.utils.Util;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.PistonBlock;
import net.minecraft.server.world.BlockAction;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.Objects;

public class ExecuteBlockEventEvent extends BaseEvent
{
	private final BlockAction blockAction;
	private Boolean returnValue;
	private FailInfo failInfo;

	public ExecuteBlockEventEvent(EventType eventType, BlockAction blockAction, Boolean returnValue, FailInfo failInfo)
	{
		super(eventType, "execute_block_event");
		this.blockAction = blockAction;
		this.returnValue = returnValue;
		this.failInfo = failInfo;
		if (this.failInfo != null)
		{
			this.failInfo.setEvent(this);
		}
	}

	public static String getMessageExtraMessengerHoverText(BlockAction blockAction)
	{
		int eventID = blockAction.getType();
		int eventParam = blockAction.getData();
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("^w eventID: %d", eventID));
		if (blockAction.getBlock() instanceof PistonBlock)
		{
			builder.append(String.format(" (%s)", PistonBlockEventType.byId(eventID)));
		}
		builder.append(String.format("\neventParam: %d", eventParam));
		if (blockAction.getBlock() instanceof PistonBlock)
		{
			builder.append(String.format(" (%s)", MicroTimingUtil.getFormattedDirectionString(Direction.byId(eventParam))));
		}
		return builder.toString();
	}

	@Override
	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(this.getEnclosedTranslatedBlockNameHeaderText(blockAction.getBlock()));
		list.add(COLOR_ACTION + this.tr("Execute"));
		if (this.blockAction.getBlock() instanceof PistonBlock)
		{
			list.add(Util.getSpaceText());
			list.add(COLOR_TARGET + PistonBlockEventType.byId(blockAction.getType()));
		}
		else
		{
			list.add(Util.getSpaceText());
			list.add(COLOR_TARGET + this.tr("BlockEvent"));
		}
		list.add(getMessageExtraMessengerHoverText(blockAction));
		if (returnValue != null)
		{
			list.add("w  ");
			list.add(MicroTimingUtil.getSuccessText(this.returnValue, true, this.failInfo != null && !this.returnValue ? this.failInfo.toText() : null));
		}
		return Messenger.c(list.toArray(new Object[0]));
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ExecuteBlockEventEvent)) return false;
		if (!super.equals(o)) return false;
		ExecuteBlockEventEvent that = (ExecuteBlockEventEvent) o;
		return Objects.equals(blockAction, that.blockAction) &&
				Objects.equals(returnValue, that.returnValue);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), blockAction, returnValue);
	}

	@Override
	public void mergeQuitEvent(BaseEvent quitEvent)
	{
		super.mergeQuitEvent(quitEvent);
		if (quitEvent instanceof ExecuteBlockEventEvent)
		{
			this.returnValue = ((ExecuteBlockEventEvent)quitEvent).returnValue;
			this.failInfo = ((ExecuteBlockEventEvent)quitEvent).failInfo;
		}
	}

	public static class FailInfo implements ToTextAble
	{
		private final FailReason reason;
		private final Block actualBlock;
		private ExecuteBlockEventEvent event;

		public FailInfo(FailReason reason, Block block)
		{
			this.reason = reason;
			this.actualBlock = block;  // for custom BLOCK_CHANGED reason
		}

		public void setEvent(ExecuteBlockEventEvent event)
		{
			this.event = event;
		}

		@Override
		public BaseText toText()
		{
			switch (this.reason)
			{
				case BLOCK_CHANGED:
					return Messenger.c(
							"w " + this.event.tr("fail_info.block_changed", "Block has changed"),
							"w : ",
							MicroTimingUtil.getTranslatedText(this.event.blockAction.getBlock()),
							"g  -> ",
							MicroTimingUtil.getTranslatedText(this.actualBlock)
					);
				case EVENT_FAIL:
				default:
					return Messenger.c(
							"w " + this.event.tr("fail_info.event_fail", "Event Failed")
					);
			}
		}
	}

	public enum FailReason
	{
		BLOCK_CHANGED,
		EVENT_FAIL
	}
}

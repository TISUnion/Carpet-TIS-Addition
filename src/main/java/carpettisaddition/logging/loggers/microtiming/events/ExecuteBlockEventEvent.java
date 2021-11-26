package carpettisaddition.logging.loggers.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.enums.PistonBlockEventType;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.utils.Messenger;
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
		super(eventType, "execute_block_event", blockAction.getBlock());
		this.blockAction = blockAction;
		this.returnValue = returnValue;
		this.failInfo = failInfo;
		if (this.failInfo != null)
		{
			this.failInfo.setEvent(this);
		}
	}

	public static BaseText getMessageExtraMessengerHoverText(BlockAction blockAction)
	{
		int eventID = blockAction.getType();
		int eventParam = blockAction.getData();
		List<Object> builder = Lists.newArrayList();
		builder.add(String.format("w eventID: %d", eventID));
		if (blockAction.getBlock() instanceof PistonBlock)
		{
			builder.add(Messenger.c(Messenger.s(" ("), PistonBlockEventType.fromId(eventID).toText(), Messenger.s(")")));
		}
		builder.add(Messenger.newLine());
		builder.add(String.format("w eventParam: %d", eventParam));
		if (blockAction.getBlock() instanceof PistonBlock)
		{
			builder.add("w  (");
			builder.add(MicroTimingUtil.getFormattedDirectionText(Direction.byId(eventParam)));
			builder.add("w )");
		}
		return Messenger.c(builder.toArray(new Object[0]));
	}

	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(Messenger.formatting(tr("execute"), COLOR_ACTION));
		list.add(Messenger.getSpaceText());
		BaseText eventName = this.blockAction.getBlock() instanceof PistonBlock ?
				Messenger.formatting(PistonBlockEventType.fromId(blockAction.getType()).toText(), COLOR_TARGET) :
				Messenger.formatting(tr("blockevent"), COLOR_TARGET);
		list.add(Messenger.fancy(eventName, getMessageExtraMessengerHoverText(this.blockAction), null));
		if (this.getEventType() == EventType.ACTION_END)
		{
			list.add(Messenger.getSpaceText());
			list.add(Messenger.formatting(tr("ended"), COLOR_RESULT));
		}
		if (this.returnValue != null)
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

	public static class FailInfo
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

		public BaseText toText()
		{
			switch (this.reason)
			{
				case BLOCK_CHANGED:
					return Messenger.c(
							this.event.tr("fail_info.block_changed"),
							"w : ",
							Messenger.block(this.event.blockAction.getBlock()),
							"g  -> ",
							Messenger.block(this.actualBlock)
					);
				case EVENT_FAIL:
				default:
					return this.event.tr("fail_info.event_fail");
			}
		}
	}

	public enum FailReason
	{
		BLOCK_CHANGED,
		EVENT_FAIL
	}
}

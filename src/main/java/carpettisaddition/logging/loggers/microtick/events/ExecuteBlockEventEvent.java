package carpettisaddition.logging.loggers.microtick.events;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import carpettisaddition.logging.loggers.microtick.types.PistonBlockEventType;
import carpettisaddition.logging.loggers.microtick.utils.MicroTickUtil;
import carpettisaddition.utils.Util;
import com.google.common.collect.Lists;
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

	public ExecuteBlockEventEvent(EventType eventType, BlockAction blockAction, Boolean returnValue)
	{
		super(eventType, "execute_block_event");
		this.blockAction = blockAction;
		this.returnValue = returnValue;
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
			builder.append(String.format(" (%s)", Direction.byId(eventParam)));
		}
		return builder.toString();
	}

	@Override
	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(this.getEnclosedTranslatedBlockNameHeaderText(blockAction.getBlock()));
		list.add("c " + this.tr("Execute"));
		if (this.blockAction.getBlock() instanceof PistonBlock)
		{
			list.add(Util.getSpaceText());
			list.add("c " + PistonBlockEventType.byId(blockAction.getType()));
		}
		else
		{
			list.add(Util.getSpaceText());
			list.add("c " + this.tr("BlockEvent"));
		}
		list.add(getMessageExtraMessengerHoverText(blockAction));
		if (returnValue != null)
		{
			list.add(Util.getSpaceText());
			list.add(MicroTickUtil.getSuccessText(this.returnValue));
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
		if (quitEvent instanceof ExecuteBlockEventEvent)
		{
			super.mergeQuitEvent(quitEvent);
			this.returnValue = ((ExecuteBlockEventEvent)quitEvent).returnValue;
		}
	}
}

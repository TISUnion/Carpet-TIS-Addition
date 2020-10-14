package carpettisaddition.logging.loggers.microtick.events;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.MicroTickUtil;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import carpettisaddition.logging.loggers.microtick.types.PistonBlockEventType;
import com.google.common.collect.Lists;
import net.minecraft.server.world.BlockAction;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;

public class ExecuteBlockEventEvent extends BaseEvent
{
	private final BlockAction blockAction;
	private final Boolean returnValue;

	public ExecuteBlockEventEvent(World world, EventType eventType, BlockAction blockAction, Boolean returnValue)
	{
		super(world, eventType, "execute_block_event");
		this.blockAction = blockAction;
		this.returnValue = returnValue;
	}

	public static String getMessageExtra(BlockAction blockAction)
	{
		int eventID = blockAction.getType();
		int eventParam = blockAction.getData();
		return String.format("^w eventID: %d (%s)\neventParam: %d (%s)",
				eventID, PistonBlockEventType.getById(eventID), eventParam, Direction.byId(eventParam));
	}

	@Override
	public Text toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(MicroTickUtil.getTranslatedName(blockAction.getBlock()));
		list.add("q  Executed");
		list.add(String.format("c  %s", PistonBlockEventType.getById(blockAction.getType())));
		list.add(getMessageExtra(blockAction));
		if (returnValue != null)
		{
			list.add(String.format("%s  %s", MicroTickUtil.getBooleanColor(returnValue), returnValue ? "Succeed" : "Failed"));
		}
		return Messenger.c(list.toArray(new Object[0]));
	}
}

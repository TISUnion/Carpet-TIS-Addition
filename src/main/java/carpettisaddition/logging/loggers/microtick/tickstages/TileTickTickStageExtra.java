package carpettisaddition.logging.loggers.microtick.tickstages;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.utils.MicroTickUtil;
import carpettisaddition.utils.Util;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ScheduledTick;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;

import java.util.List;

public class TileTickTickStageExtra extends TickStageExtraBase
{
	private final World world;
	private final ScheduledTick<?> nextTickListEntry;
	private final int order;

	public TileTickTickStageExtra(World world, ScheduledTick<?> nextTickListEntry, int order)
	{
		this.world = world;
		this.nextTickListEntry = nextTickListEntry;
		this.order = order;
	}

	@Override
	public BaseText toText()
	{
		BlockPos pos = this.nextTickListEntry.pos;
		TickPriority priority = this.nextTickListEntry.priority;
		Object target = this.nextTickListEntry.getObject();
		List<Object> list = Lists.newArrayList();
		if (target instanceof Block)
		{
			list.add(String.format("w %s: ", MicroTickLoggerManager.tr("Block")));
			list.add(MicroTickUtil.getTranslatedText((Block)target));
			list.add("w \n");
		}
		list.add(String.format("w %s: %d\n", MicroTickLoggerManager.tr("Order"), this.order));
		list.add(String.format("w %s: %d (%s)\n", MicroTickLoggerManager.tr("Priority"), priority.getIndex(), priority));
		list.add(String.format("w %s: [%d, %d, %d]", MicroTickLoggerManager.tr("Position"), pos.getX(), pos.getY(), pos.getZ()));
		return Messenger.c(list.toArray(new Object[0]));
	}

	@Override
	public ClickEvent getClickEvent()
	{
		return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Util.getTeleportCommand(this.nextTickListEntry.pos, this.world.getRegistryKey()));
	}
}

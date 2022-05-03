package carpettisaddition.logging.loggers.microtiming.tickphase.substages;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.TextUtil;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ScheduledTick;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;

import java.util.List;

public class TileTickSubStage extends AbstractSubStage
{
	private final World world;
	private final ScheduledTick<?> nextTickListEntry;
	private final int order;

	public TileTickSubStage(World world, ScheduledTick<?> nextTickListEntry, int order)
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
			list.add(Messenger.c(MicroTimingLoggerManager.tr("common.block"), "w : ", Messenger.block((Block)target)));
		}
		else if (target instanceof Fluid)
		{
			list.add(Messenger.c(MicroTimingLoggerManager.tr("common.fluid"), "w : ", Messenger.fluid((Fluid)target)));
		}
		list.add(Messenger.newLine());

		list.add(Messenger.c(MicroTimingLoggerManager.tr("common.order"), String.format("w : %d\n", this.order)));
		list.add(Messenger.c(MicroTimingLoggerManager.tr("common.priority"), String.format("w : %d (%s)\n", priority.getIndex(), priority)));
		list.add(Messenger.c(MicroTimingLoggerManager.tr("common.position"), String.format("w : %s", TextUtil.coord(pos))));

		return Messenger.c(list.toArray(new Object[0]));
	}

	@Override
	public ClickEvent getClickEvent()
	{
		return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.tp(this.nextTickListEntry.pos, DimensionWrapper.of(this.world)));
	}
}

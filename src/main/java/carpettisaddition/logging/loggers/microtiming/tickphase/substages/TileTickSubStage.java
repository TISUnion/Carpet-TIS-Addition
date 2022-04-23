package carpettisaddition.logging.loggers.microtiming.tickphase.substages;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.utils.DimensionWrapper;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.tick.OrderedTick;

import java.util.List;

public class TileTickSubStage extends AbstractSubStage
{
	private final World world;
	private final OrderedTick<?> nextTickListEntry;
	private final int order;

	public TileTickSubStage(World world, OrderedTick<?> nextTickListEntry, int order)
	{
		this.world = world;
		this.nextTickListEntry = nextTickListEntry;
		this.order = order;
	}

	@Override
	public MutableText toText()
	{
		BlockPos pos = this.nextTickListEntry.pos();
		TickPriority priority = this.nextTickListEntry.priority();
		Object target = this.nextTickListEntry.type();
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
		return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.tp(this.nextTickListEntry.pos(), DimensionWrapper.of(this.world)));
	}
}

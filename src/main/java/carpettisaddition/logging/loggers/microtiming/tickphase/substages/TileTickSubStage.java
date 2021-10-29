package carpettisaddition.logging.loggers.microtiming.tickphase.substages;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.class_6760;
import net.minecraft.fluid.Fluid;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;

import java.util.List;

public class TileTickSubStage extends AbstractSubStage
{
	private final World world;
	private final class_6760<?> nextTickListEntry;
	private final int order;

	public TileTickSubStage(World world, class_6760<?> nextTickListEntry, int order)
	{
		this.world = world;
		this.nextTickListEntry = nextTickListEntry;
		this.order = order;
	}

	@Override
	public BaseText toText()
	{
		BlockPos pos = this.nextTickListEntry.pos();
		TickPriority priority = this.nextTickListEntry.priority();
		Object target = this.nextTickListEntry.type();
		List<Object> list = Lists.newArrayList();
		if (target instanceof Block)
		{
			list.add(String.format("w %s: ", MicroTimingLoggerManager.tr("Block")));
			list.add(TextUtil.getBlockName((Block)target));
			list.add("w \n");
		}
		else if (target instanceof Fluid)
		{
			list.add(String.format("w %s: ", MicroTimingLoggerManager.tr("Fluid")));
			list.add(TextUtil.getFluidName((Fluid)target));
			list.add("w \n");
		}
		list.add(String.format("w %s: %d\n", MicroTimingLoggerManager.tr("Order"), this.order));
		list.add(String.format("w %s: %d (%s)\n", MicroTimingLoggerManager.tr("Priority"), priority.getIndex(), priority));
		list.add(String.format("w %s: %s", MicroTimingLoggerManager.tr("Position"), TextUtil.getCoordinateString(pos)));
		return Messenger.c(list.toArray(new Object[0]));
	}

	@Override
	public ClickEvent getClickEvent()
	{
		return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.getTeleportCommand(this.nextTickListEntry.pos(), this.world.getRegistryKey()));
	}
}

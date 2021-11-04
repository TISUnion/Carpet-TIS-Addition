package carpettisaddition.logging.loggers.microtiming.tickphase.substages;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.utils.DimensionWrapper;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.TextUtil;
import net.minecraft.server.world.BlockEvent;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.world.World;

public class BlockEventSubStage extends AbstractSubStage
{
	private final World world;
	private final BlockEvent blockEventData;
	private final int order;
	private final int depth;

	public BlockEventSubStage(World world, BlockEvent blockEventData, int order, int depth)
	{
		this.world = world;
		this.blockEventData = blockEventData;
		this.order = order;
		this.depth = depth;
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				MicroTimingLoggerManager.tr("common.block"), "w : ", Messenger.block(this.blockEventData.getBlock()), Messenger.newLine(),
				MicroTimingLoggerManager.tr("common.order"), String.format("w : %d\n", this.order),
				MicroTimingLoggerManager.tr("common.depth"), String.format("w : %d\n", this.depth),
				MicroTimingLoggerManager.tr("common.position"), String.format("w : %s", TextUtil.coord(this.blockEventData.getPos()))
		);
	}

	@Override
	public ClickEvent getClickEvent()
	{
		return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.tp(this.blockEventData.getPos(), DimensionWrapper.of(this.world)));
	}
}

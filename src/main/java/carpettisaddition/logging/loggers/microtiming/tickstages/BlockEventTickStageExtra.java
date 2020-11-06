package carpettisaddition.logging.loggers.microtiming.tickstages;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.utils.TextUtil;
import net.minecraft.server.world.BlockAction;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEventTickStageExtra extends TickStageExtraBase
{
	private final World world;
	private final BlockAction blockEventData;
	private final int order;
	private final int depth;

	public BlockEventTickStageExtra(World world, BlockAction blockEventData, int order, int depth)
	{
		this.world = world;
		this.blockEventData = blockEventData;
		this.order = order;
		this.depth = depth;
	}

	@Override
	public BaseText toText()
	{
		BlockPos pos = this.blockEventData.getPos();
		return Messenger.c(
				String.format("w %s: ", MicroTimingLoggerManager.tr("Block")),
				MicroTimingUtil.getTranslatedText(this.blockEventData.getBlock()),
				String.format("w \n%s: %d", MicroTimingLoggerManager.tr("Order"), this.order),
				String.format("w \n%s: %d", MicroTimingLoggerManager.tr("Depth"), this.depth),
				String.format("w \n%s: [%d, %d, %d]", MicroTimingLoggerManager.tr("Position"), pos.getX(), pos.getY(), pos.getZ())
		);
	}

	@Override
	public ClickEvent getClickEvent()
	{
		return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.getTeleportCommand(this.blockEventData.getPos(), this.world.getDimension().getType()));
	}
}

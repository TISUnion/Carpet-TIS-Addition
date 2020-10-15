package carpettisaddition.logging.loggers.microtick.tickstages;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.utils.MicroTickUtil;
import carpettisaddition.utils.Util;
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
				String.format("w %s: ", MicroTickLoggerManager.tr("Block")),
				MicroTickUtil.getTranslatedName(this.blockEventData.getBlock()),
				String.format("w \n%s: %d", MicroTickLoggerManager.tr("Order"), this.order),
				String.format("w \n%s: %d", MicroTickLoggerManager.tr("Depth"), this.depth),
				String.format("w \n%s: [%d, %d, %d]", MicroTickLoggerManager.tr("Position"), pos.getX(), pos.getY(), pos.getZ())
		);
	}

	@Override
	public ClickEvent getClickEvent()
	{
		return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Util.getTeleportCommand(this.blockEventData.getPos(), this.world.getDimension().getType()));
	}
}

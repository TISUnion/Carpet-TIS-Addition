package carpettisaddition.logging.loggers.microtick.tickstages;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.utils.MicroTickUtil;
import carpettisaddition.utils.Util;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityTickStageExtra extends TickStageExtraBase
{
	private final World world;
	private final BlockPos pos;
	private final Block block;
	private final int order;

	public TileEntityTickStageExtra(BlockEntity tileEntity, int order)
	{
		this.world = tileEntity.getWorld();
		this.pos = tileEntity.getPos().toImmutable();
		this.block = tileEntity.getCachedState().getBlock();
		this.order = order;
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				String.format("w %s: ", MicroTickLoggerManager.tr("Block")),
				MicroTickUtil.getTranslatedName(this.block),
				String.format("w \n%s: %d", MicroTickLoggerManager.tr("Order"), this.order),
				String.format("w \n%s: [%d, %d, %d]", MicroTickLoggerManager.tr("Position"), this.pos.getX(), this.pos.getY(), this.pos.getZ())
		);
	}

	@Override
	public ClickEvent getClickEvent()
	{
		return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Util.getTeleportCommand(this.pos, this.world.getDimension().getType()));
	}
}

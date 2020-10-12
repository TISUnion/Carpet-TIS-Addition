package carpettisaddition.microtick.tickstages;

import carpet.utils.Messenger;
import carpettisaddition.microtick.MicroTickUtil;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class TileEntityTickStageExtra implements TickStage
{
	private final BlockPos pos;
	private final Block block;
	private final int order;

	public TileEntityTickStageExtra(BlockEntity tileEntity, int order)
	{
		this.pos = tileEntity.getPos().toImmutable();
		this.block = tileEntity.getCachedState().getBlock();
		this.order = order;
	}

	@Override
	public Text toText()
	{
		return Messenger.c(
				"w Block: ",
				MicroTickUtil.getTranslatedName(this.block),
				String.format("w \nOrder: %d", this.order),
				String.format("w \nPosition: [%d, %d, %d]", this.pos.getX(), this.pos.getY(), this.pos.getZ())
		);
	}
}

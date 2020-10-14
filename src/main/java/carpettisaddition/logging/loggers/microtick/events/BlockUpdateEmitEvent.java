package carpettisaddition.logging.loggers.microtick.events;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.MicroTickUtil;
import carpettisaddition.logging.loggers.microtick.types.BlockUpdateType;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import net.minecraft.block.Block;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class BlockUpdateEmitEvent extends BaseEvent
{
	private final BlockUpdateType updateType;
	private final String updateTypeExtraMessage;
	private final Block fromBlock;

	public BlockUpdateEmitEvent(World world, EventType eventType, Block fromBlock, BlockUpdateType blockUpdateType, String updateTypeExtraMessage)
	{
		super(world, eventType, "block_update_emit");
		this.fromBlock = fromBlock;
		this.updateType = blockUpdateType;
		this.updateTypeExtraMessage = updateTypeExtraMessage;
	}

	@Override
	public Text toText()
	{
		return Messenger.c(
				MicroTickUtil.getTranslatedName(this.fromBlock),
				String.format("q  %s", this.eventType),
				String.format("c  %s", this.updateType),
				String.format("^w %s", this.updateTypeExtraMessage)
		);
	}
}

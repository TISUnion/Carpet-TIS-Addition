package carpettisaddition.logging.loggers.microtick.events;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.MicroTickUtil;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import net.minecraft.block.Block;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;

public class ScheduleTileTickEvent extends BaseEvent
{
	private final Block block;
	private final BlockPos pos;
	private final int delay;
	private final TickPriority priority;

	public ScheduleTileTickEvent(World world, Block block, BlockPos pos, int delay, TickPriority priority)
	{
		super(world, EventType.EVENT, "schedule_tile_tick");
		this.block = block;
		this.pos = pos;
		this.delay = delay;
		this.priority = priority;
	}

	@Override
	public Text toText()
	{
		return Messenger.c(
				MicroTickUtil.getTranslatedName(block),
				"q  Scheduled",
				"c  TileTick",
				String.format("^w Delay: %dgt\nPriority: %d (%s)", delay, priority.getIndex(), priority)
		);
	}
}

package carpettisaddition.logging.loggers.microtiming.enums;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.mixins.logger.microtiming.BlockAccessor;
import carpettisaddition.utils.TextUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public enum BlockUpdateType
{
	BLOCK_UPDATE("BlockUpdates", new String[]{"Neighbor Changed", "Neighbor Update"}, Constants.BLOCK_UPDATE_ORDER),
	BLOCK_UPDATE_EXCEPT("BlockUpdates Except", new String[]{"Neighbor Changed Except", "Neighbor Update Except"}, Constants.BLOCK_UPDATE_ORDER),
	STATE_UPDATE("StateUpdates", new String[]{"Post Placement", "Update Shape"}, Constants.STATE_UPDATE_ORDER),
	COMPARATOR_UPDATE("ComparatorUpdates", new String[]{"Block update only to comparators"}, Constants.COMPARATOR_UPDATE_ORDER);

	private final String name;
	private final String[] aka;
	private final Direction[] updateOrder;

	BlockUpdateType(String name, String[] aka, Direction[] updateOrder)
	{
		this.name = name;
		this.aka = aka;
		this.updateOrder = updateOrder;
	}

	private String tr(String text)
	{
		return MicroTimingLoggerManager.tr("block_update_type." + text, text, true);
	}

	@Override
	public String toString()
	{
		return this.tr(this.name);
	}

	public String getUpdateOrderList(Direction skipSide)
	{
		int counter = 0;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(tr("aka"));
		stringBuilder.append(TextUtil.getSpace());
		stringBuilder.append(Joiner.on(", ").join(this.aka));
		stringBuilder.append('\n');
		for (Direction direction : this.updateOrder)
		{
			if (skipSide != direction)
			{
				if (counter > 0)
				{
					stringBuilder.append('\n');
				}
				stringBuilder.append(String.format("%d. %s", (++counter), MicroTimingUtil.getFormattedDirectionString(direction)));
			}
		}
		if (skipSide != null)
		{
			stringBuilder.append(String.format("\n%s: %s", tr("Except"), skipSide));
		}
		return stringBuilder.toString();
	}

	private static class Constants
	{
		/**
		 * See {@link World#updateNeighborsAlways} and {@link World#updateNeighborsExcept}
		 */
		private static final Direction[] BLOCK_UPDATE_ORDER = new Direction[]{Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH};

		/**
		 * See {@link Block#updateNeighborStates}
		 */
		private static final Direction[] STATE_UPDATE_ORDER = BlockAccessor.getFACINGS();

		/**
		 * See {@link World#updateHorizontalAdjacent}
		 */
		private static final Direction[] COMPARATOR_UPDATE_ORDER = Lists.newArrayList(Direction.Type.HORIZONTAL.iterator()).toArray(new Direction[0]);
	}
}

package carpettisaddition.logging.loggers.microtick.types;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.utils.Util;
import com.google.common.base.Joiner;
import net.minecraft.util.math.Direction;

public enum BlockUpdateType
{
	BLOCK_UPDATE("BlockUpdates", new String[]{"Neighbor Changed", "Neighbor Update"}, Constants.BLOCK_UPDATE_ORDER),
	BLOCK_UPDATE_EXCEPT("BlockUpdates Except", new String[]{"Neighbor Changed Except", "Neighbor Update Except"}, Constants.BLOCK_UPDATE_ORDER),
	STATE_UPDATE("StateUpdates", new String[]{"Post Placement", "Update Shape"}, Constants.STATE_UPDATE_ORDER);

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
		return MicroTickLoggerManager.tr("block_update_type." + text, text);
	}

	@Override
	public String toString()
	{
		return tr(this.name);
	}

	public String getUpdateOrderList(Direction skipSide)
	{
		int counter = 0;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(tr("aka"));
		stringBuilder.append(Util.getSpace());
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
				stringBuilder.append(String.format("%d. %s", (++counter), tr(direction.toString())));
			}
		}
		if (skipSide != null)
		{
			stringBuilder.append(String.format("%s: %s\n", tr("Except"), skipSide));
		}
		return stringBuilder.toString();
	}

	static class Constants
	{
		static final Direction[] BLOCK_UPDATE_ORDER = new Direction[]{Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH};
		static final Direction[] STATE_UPDATE_ORDER = new Direction[]{Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP};  // the same as Block.FACINGS
	}
}

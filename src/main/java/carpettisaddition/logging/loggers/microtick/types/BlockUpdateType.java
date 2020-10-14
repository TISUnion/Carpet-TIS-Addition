package carpettisaddition.logging.loggers.microtick.types;

import com.google.common.base.Joiner;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.util.math.Direction;

import java.util.Map;

public enum BlockUpdateType
{
	BLOCK_UPDATE("BlockUpdate", new String[]{"Neighbor Changed", "Neighbor Update"}, Constants.BLOCK_UPDATE_ORDER),
	BLOCK_UPDATE_EXCEPT("BlockUpdate Except", new String[]{"Neighbor Changed Except", "Neighbor Update Except"}, Constants.BLOCK_UPDATE_ORDER),
	STATE_UPDATE("StateUpdate", new String[]{"Post Placement", "Update Shape"}, Constants.STATE_UPDATE_ORDER);

	private final String name;
	private final String[] aka;
	private final Direction[] updateOrder;
	private final Map<Direction, String> updateOrderListCache = new Reference2ObjectArrayMap<>();
	private final String updateOrderListCacheNoSkip;

	BlockUpdateType(String name, String[] aka, Direction[] updateOrder)
	{
		this.name = name;
		this.aka = aka;
		this.updateOrder = updateOrder;
		for (Direction Direction : Direction.values())
		{
			this.updateOrderListCache.put(Direction, this._getUpdateOrderList(Direction));
		}
		this.updateOrderListCacheNoSkip = this._getUpdateOrderList(null);

	}

	@Override
	public String toString()
	{
		return this.name;
	}

	private String _getUpdateOrderList(Direction skipSide)
	{
		int counter = 0;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(String.format("aka %s\n", Joiner.on('\n').join(this.aka)));
		for (Direction Direction : this.updateOrder)
		{
			if (skipSide != Direction)
			{
				if (counter > 0)
				{
					stringBuilder.append('\n');
				}
				stringBuilder.append(String.format("%d. %s", (++counter), Direction));
			}
		}
		if (skipSide != null)
		{
			stringBuilder.append(String.format("Except: %s\n", skipSide));
		}
		return stringBuilder.toString();
	}

	public String getUpdateOrderList(Direction skipSide)
	{
		return skipSide == null ? this.updateOrderListCacheNoSkip : this.updateOrderListCache.get(skipSide);
	}

	static class Constants
	{
		static final Direction[] BLOCK_UPDATE_ORDER = new Direction[]{Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH};
		static final Direction[] STATE_UPDATE_ORDER = new Direction[]{Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP};  // the same as Block.FACINGS
	}
}

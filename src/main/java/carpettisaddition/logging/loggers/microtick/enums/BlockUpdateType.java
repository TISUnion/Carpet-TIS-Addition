package carpettisaddition.logging.loggers.microtick.enums;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.util.math.Direction;

import java.util.Map;

public enum BlockUpdateType
{
	NEIGHBOR_CHANGED("NeighborChanged", "Block Update", Constants.NC_UPDATE_ORDER),
	NEIGHBOR_CHANGED_EXCEPT("NeighborChanged Except", "Block Update Except", Constants.NC_UPDATE_ORDER),
	POST_PLACEMENT("PostPlacement", "State Update", Constants.PP_UPDATE_ORDER);

	private final String name, aka;
	private final Direction[] updateOrder;
	private final Map<Direction, String> updateOrderListCache = new Reference2ObjectArrayMap<>();
	private final String updateOrderListCacheNoSkip;

	BlockUpdateType(String name, String aka, Direction[] updateOrder)
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
		stringBuilder.append(String.format("aka %s\n", this.aka));
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
		static final Direction[] NC_UPDATE_ORDER = new Direction[]{Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH};
		static final Direction[] PP_UPDATE_ORDER = new Direction[]{Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP};  // the same as Block.FACINGS
	}
}

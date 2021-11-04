package carpettisaddition.logging.loggers.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class EmitBlockUpdateRedstoneDustEvent extends EmitBlockUpdateEvent
{
	private final List<BlockPos> updateOrder;
	private final BlockPos pos;

	public EmitBlockUpdateRedstoneDustEvent(EventType eventType, Block block, String methodName, BlockPos pos, Collection<BlockPos> updateOrder)
	{
		super(eventType, block, methodName);
		this.updateOrder = updateOrder != null ? Lists.newArrayList(updateOrder) : null;
		this.pos = pos.toImmutable();
	}

	@Override
	protected BaseText getUpdatesTextHoverText()
	{
		BaseText hover = super.getUpdatesTextHoverText();
		if (this.updateOrder != null)
		{
			List<Object> list = Lists.newArrayList();
			for (int i = 0; i < this.updateOrder.size(); i++)
			{
				list.add("w \n");
				BlockPos target = this.updateOrder.get(i);
				Vec3i vec = target.subtract(this.pos);
				Direction direction = Direction.fromVector(vec.getX(), vec.getY(), vec.getZ());
				list.add(String.format("w %d. ", i + 1));
				list.add(Messenger.coord("w", target));
				BaseText extra = null;
				if (direction != null)
				{
					extra = MicroTimingUtil.getFormattedDirectionText(direction);
				}
				if (target.equals(this.pos))
				{
					extra = tr("self");
				}
				if (extra != null)
				{
					list.add("w  ");
					list.add(extra);
				}
			}
			hover.append(Messenger.c(list.toArray(new Object[0])));
		}
		return hover;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof EmitBlockUpdateRedstoneDustEvent)) return false;
		if (!super.equals(o)) return false;
		EmitBlockUpdateRedstoneDustEvent that = (EmitBlockUpdateRedstoneDustEvent) o;
		return Objects.equals(updateOrder, that.updateOrder) &&
				Objects.equals(pos, that.pos);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), updateOrder, pos);
	}
}

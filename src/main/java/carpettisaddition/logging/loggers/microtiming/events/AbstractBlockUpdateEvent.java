package carpettisaddition.logging.loggers.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import net.minecraft.block.Block;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class AbstractBlockUpdateEvent extends BaseEvent
{
	protected final Block sourceBlock;
	protected final BlockUpdateType blockUpdateType;
	protected final Direction exceptSide;
	@Nullable
	private BaseText updateTypeExtraMessageCache;

	public AbstractBlockUpdateEvent(EventType eventType, String translateKey, Block sourceBlock, BlockUpdateType blockUpdateType, Direction exceptSide)
	{
		super(eventType, translateKey, sourceBlock);
		this.sourceBlock = sourceBlock;
		this.blockUpdateType = blockUpdateType;
		this.exceptSide = exceptSide;
	}

	protected BaseText getUpdateTypeExtraMessage()
	{
		if (this.updateTypeExtraMessageCache == null)
		{
			this.updateTypeExtraMessageCache = this.blockUpdateType.getUpdateOrderList(this.exceptSide);
		}
		return this.updateTypeExtraMessageCache;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof AbstractBlockUpdateEvent)) return false;
		if (!super.equals(o)) return false;
		AbstractBlockUpdateEvent that = (AbstractBlockUpdateEvent) o;
		return blockUpdateType == that.blockUpdateType &&
				Objects.equals(this.getUpdateTypeExtraMessage(), that.getUpdateTypeExtraMessage()) &&
				Objects.equals(sourceBlock, that.sourceBlock);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), sourceBlock, blockUpdateType, exceptSide);
	}
}

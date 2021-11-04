package carpettisaddition.commands.lifetime.spawning;

import carpettisaddition.utils.DimensionWrapper;
import carpettisaddition.utils.Messenger;
import net.minecraft.text.BaseText;

import java.util.Objects;

public class TransDimensionSpawningReason extends SpawningReason
{
	private final DimensionWrapper oldDimension;

	public TransDimensionSpawningReason(DimensionWrapper oldDimension)
	{
		this.oldDimension = Objects.requireNonNull(oldDimension);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TransDimensionSpawningReason that = (TransDimensionSpawningReason) o;
		return Objects.equals(this.oldDimension, that.oldDimension);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.oldDimension);
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				tr("trans_dimension"),
				"g  (",
				Messenger.formatting(tr("trans_dimension.from", Messenger.dimension(this.oldDimension)), "g"),
				"g )"
		);
	}
}

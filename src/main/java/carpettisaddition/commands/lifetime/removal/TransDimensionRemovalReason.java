package carpettisaddition.commands.lifetime.removal;

import carpet.utils.Messenger;
import carpettisaddition.utils.TextUtil;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;
import net.minecraft.world.dimension.DimensionType;

import java.util.Objects;

public class TransDimensionRemovalReason extends RemovalReason
{
	private final DimensionType newDimension;

	public TransDimensionRemovalReason(DimensionType newDimension)
	{
		this.newDimension = Objects.requireNonNull(newDimension);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TransDimensionRemovalReason that = (TransDimensionRemovalReason) o;
		return Objects.equals(this.newDimension, that.newDimension);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.newDimension);
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				"w " + this.tr("trans_dimension", "Trans-dimension"),
				"g  (" + this.tr("trans_dimension.to", "to"),
				TextUtil.getSpaceText(),
				TextUtil.attachFormatting(TextUtil.getDimensionNameText(this.newDimension), Formatting.GRAY),
				"g )"
		);
	}
}

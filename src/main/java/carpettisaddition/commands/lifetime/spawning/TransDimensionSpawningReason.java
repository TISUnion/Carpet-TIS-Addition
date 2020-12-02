package carpettisaddition.commands.lifetime.spawning;

import carpet.utils.Messenger;
import carpettisaddition.utils.TextUtil;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Objects;

public class TransDimensionSpawningReason extends SpawningReason
{
	private final RegistryKey<World> oldDimension;

	public TransDimensionSpawningReason(RegistryKey<World> oldDimension)
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
				"w " + this.tr("trans_dimension", "Trans-dimension"),
				"g  (" + this.tr("trans_dimension.from", "from"),
				TextUtil.getSpaceText(),
				TextUtil.attachFormatting(TextUtil.getDimensionNameText(this.oldDimension), Formatting.GRAY),
				"g )"
		);
	}
}

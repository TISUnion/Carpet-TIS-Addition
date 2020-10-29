package carpettisaddition.mixins.rule.redstoneDustRandomUpdateOrder;

import carpettisaddition.CarpetTISAdditionSettings;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.*;


@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneWireBlockMixin extends Block
{
	private final Random random = new Random();

	public RedstoneWireBlockMixin(Settings settings)
	{
		super(settings);
	}

	@Redirect(
			method = "update",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/Set;iterator()Ljava/util/Iterator;"
			)
	)
	private Iterator<BlockPos> letsMakeTheOrderUnpredictable(Set<BlockPos> set)
	{
		if (CarpetTISAdditionSettings.redstoneDustRandomUpdateOrder)
		{
			List<BlockPos> list = Lists.newArrayList(set);
			Collections.shuffle(list, this.random);
			return list.iterator();
		}
		return set.iterator();
	}
}

package carpettisaddition.mixins.rule.largeBarrel.compat.malilib;

import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntityType.class)
public abstract class BlockEntityTypeMixin
{
	// the related target codes are ported so we just need to apply modification there
	// see carpettisaddition.helpers.rule.largeBarrel.DoubleBlockProperties.getBlockEntity
}

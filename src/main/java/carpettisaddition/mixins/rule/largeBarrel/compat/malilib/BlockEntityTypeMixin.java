package carpettisaddition.mixins.rule.largeBarrel.compat.malilib;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;

@Restriction(require = @Condition(ModIds.malilib))
@Mixin(BlockEntityType.class)
public abstract class BlockEntityTypeMixin
{
	// the related target codes are ported so we just need to apply modification there
	// see carpettisaddition.helpers.rule.largeBarrel.DoubleBlockProperties.getBlockEntity
}

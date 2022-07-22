package carpettisaddition.mixins.rule.largeBarrel.compat.malilib;

import carpettisaddition.utils.ModIds;
import carpettisaddition.utils.compat.DummyClass;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

@Restriction(require = {
		@Condition(ModIds.malilib),
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.15")
})
@Mixin(DummyClass.class)
public abstract class BlockEntityTypeMixin
{
	// for mc < 1.14
	// the related target codes are ported so we just need to apply modification there
	// see carpettisaddition.helpers.rule.largeBarrel.DoubleBlockProperties.getBlockEntity
}

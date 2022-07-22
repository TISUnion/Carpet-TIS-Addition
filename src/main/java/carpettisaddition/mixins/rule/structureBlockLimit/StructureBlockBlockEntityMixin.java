package carpettisaddition.mixins.rule.structureBlockLimit;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.16"))
@Mixin(StructureBlockBlockEntity.class)
public abstract class StructureBlockBlockEntityMixin
{
	@ModifyConstant(
			method = "fromTag",
			require = 3,
			constant = @Constant(intValue = -32)
	)
	private int structureBlockLimitNegative(int value)
	{
		return -CarpetTISAdditionSettings.structureBlockLimit;
	}

	@ModifyConstant(
			method = "fromTag",
			require = 6,
			constant = @Constant(intValue = 32)
	)
	private int structureBlockLimitPositive(int value)
	{
		return CarpetTISAdditionSettings.structureBlockLimit;
	}
}

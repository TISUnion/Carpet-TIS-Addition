package carpettisaddition.mixins;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;


@Mixin(StructureBlockBlockEntity.class)
public abstract class StructureBlockBlockEntity_structureBlockLimitMixin
{
	@ModifyConstant(
			method = "fromTag",
			require = 3,
			constant = @Constant(intValue = -48)
	)
	private int structureBlockLimitNegative(int value)
	{
		return -CarpetTISAdditionSettings.structureBlockLimit;
	}

	@ModifyConstant(
			method = "fromTag",
			require = 6,
			constant = @Constant(intValue = 48)
	)
	private int structureBlockLimitPositive(int value)
	{
		return CarpetTISAdditionSettings.structureBlockLimit;
	}
}

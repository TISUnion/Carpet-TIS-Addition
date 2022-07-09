package carpettisaddition.mixins.rule.structureBlockOutlineDistance;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.16"))
@Mixin(StructureBlockBlockEntity.class)
public abstract class StructureBlockBlockEntityMixin
		//#if MC < 11600
		extends BlockEntity
		//#endif
{
	//#if MC < 11600
	public StructureBlockBlockEntityMixin(BlockEntityType<?> type)
	{
		super(type);
	}

	@Override
	public double getSquaredRenderDistance()
	{
		return CarpetTISAdditionSettings.structureBlockOutlineDistance * CarpetTISAdditionSettings.structureBlockOutlineDistance;
	}
	//#endif
}

package carpettisaddition.mixins.rule.structureBlockOutlineDistance;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StructureBlockBlockEntity.class)
public abstract class StructureBlockBlockEntityMixin extends BlockEntity
{
	public StructureBlockBlockEntityMixin(BlockEntityType<?> type)
	{
		super(type);
	}

	@Override
	public double getSquaredRenderDistance()
	{
		return CarpetTISAdditionSettings.structureBlockOutlineDistance * CarpetTISAdditionSettings.structureBlockOutlineDistance;
	}
}

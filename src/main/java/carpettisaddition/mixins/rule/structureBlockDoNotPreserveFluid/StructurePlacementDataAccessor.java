package carpettisaddition.mixins.rule.structureBlockDoNotPreserveFluid;

import net.minecraft.structure.StructurePlacementData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructurePlacementData.class)
public interface StructurePlacementDataAccessor
{
	@Accessor
	void setPlaceFluids(boolean value);
}

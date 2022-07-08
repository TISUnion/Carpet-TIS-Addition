package carpettisaddition.mixins.rule.structureBlockDoNotPreserveFluid;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.structure.StructurePlacementData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(StructureBlockBlockEntity.class)
public abstract class StructureBlockBlockEntityMixin
{
	@ModifyArg(
			//#if MC >= 11500
			method = "place",
			//#else
			//$$ method = "loadStructure(Z)Z",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/structure/Structure;place(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/StructurePlacementData;)V"
			)
	)
	private StructurePlacementData structureBlockDoNotPreserveFluid_setPlaceFluids(StructurePlacementData structurePlacementData)
	{
		if (CarpetTISAdditionSettings.structureBlockDoNotPreserveFluid)
		{
			((StructurePlacementDataAccessor)structurePlacementData).setPlaceFluids(false);
		}
		return structurePlacementData;
	}
}

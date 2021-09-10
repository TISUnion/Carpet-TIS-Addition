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
			method = "place",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/structure/Structure;place(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/StructurePlacementData;Ljava/util/Random;)V"
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

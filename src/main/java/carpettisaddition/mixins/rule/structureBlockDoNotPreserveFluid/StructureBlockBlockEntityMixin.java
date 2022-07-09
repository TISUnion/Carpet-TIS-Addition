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
					//#if MC >= 11900
					//$$ target = "Lnet/minecraft/structure/StructureTemplate;place(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/StructurePlacementData;Lnet/minecraft/util/math/random/Random;I)Z"
					//#elseif MC >= 11700
					//$$ target = "Lnet/minecraft/structure/Structure;place(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/StructurePlacementData;Ljava/util/Random;I)Z"
					//#elseif MC >= 11600
					//$$ target = "Lnet/minecraft/structure/Structure;place(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/StructurePlacementData;Ljava/util/Random;)V"
					//#else
					target = "Lnet/minecraft/structure/Structure;place(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/StructurePlacementData;)V"
					//#endif
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

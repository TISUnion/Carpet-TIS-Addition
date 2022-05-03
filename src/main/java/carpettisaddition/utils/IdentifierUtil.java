package carpettisaddition.utils;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class IdentifierUtil
{
	public static Identifier id(Block block) {return Registry.BLOCK.getId(block);}
	public static Identifier id(Fluid fluid) {return Registry.FLUID.getId(fluid);}
	public static Identifier id(EntityType<?> entityType) {return Registry.ENTITY_TYPE.getId(entityType);}
	public static Identifier id(BlockEntityType<?> blockEntityType) {return Registry.BLOCK_ENTITY_TYPE.getId(blockEntityType);}
}

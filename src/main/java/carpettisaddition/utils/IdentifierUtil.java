package carpettisaddition.utils;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class IdentifierUtil
{
	public static Identifier id(Block block) {return Registry.BLOCK.getId(block);}
	public static Identifier id(Fluid fluid) {return Registry.FLUID.getId(fluid);}
}

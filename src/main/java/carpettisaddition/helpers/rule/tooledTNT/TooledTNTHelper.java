package carpettisaddition.helpers.rule.tooledTNT;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.explosion.Explosion;

public class TooledTNTHelper
{
	/**
	 * Attach the causing entity's main hand item onto the loot table builder
	 *
	 * See also {@link net.minecraft.block.Block#getDroppedStacks(BlockState, ServerWorld, BlockPos, BlockEntity, Entity, ItemStack)}
	 * for loot table building for regular entity block mining
	 */
	public static ItemStack getMainHandItemOfCausingEntity(Explosion explosion)
	{
		if (CarpetTISAdditionSettings.tooledTNT)
		{
			LivingEntity causingEntity = explosion.getCausingEntity();
			if (causingEntity != null)
			{
				return causingEntity.getMainHandStack();
			}
		}
		// vanilla
		return ItemStack.EMPTY;
	}
}

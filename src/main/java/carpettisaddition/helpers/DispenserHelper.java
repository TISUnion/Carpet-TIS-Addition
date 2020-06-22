package carpettisaddition.helpers;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;


public class DispenserHelper
{
	public static class FireDragonBreathDispenserBehaviour extends FallibleItemDispenserBehavior implements DispenserBehavior
	{
		@Override
		protected ItemStack dispenseSilently(BlockPointer source, ItemStack stack)
		{
			if (CarpetTISAdditionSettings.dispensersFireDragonBreath)
			{
				Direction sourceFace = source.getBlockState().get(DispenserBlock.FACING);
				World world = source.getWorld();
				BlockPos blockpos = source.getBlockPos().offset(sourceFace);

				AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(world, blockpos.getX() + 0.5, blockpos.getY() + 0.5, blockpos.getZ() + 0.5);
				areaEffectCloudEntity.setOwner(null);
				areaEffectCloudEntity.setParticleType(ParticleTypes.DRAGON_BREATH);
				areaEffectCloudEntity.setRadius(3.0F);
				areaEffectCloudEntity.setDuration(600);
				areaEffectCloudEntity.setRadiusGrowth((7.0F - areaEffectCloudEntity.getRadius()) / (float)areaEffectCloudEntity.getDuration());
				areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1));
				world.syncGlobalEvent(2007, blockpos, areaEffectCloudEntity.getColor());
				world.spawnEntity(areaEffectCloudEntity);

				stack.decrement(1);
				return stack;
			}
			else
			{
				return super.dispenseSilently(source, stack);
			}
		}
	}
}

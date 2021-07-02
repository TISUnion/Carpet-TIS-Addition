package carpettisaddition.helpers.rule.dispensersFireDragonBreath;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class FireDragonBreathDispenserBehaviour extends ItemDispenserBehavior
{
	public static final FireDragonBreathDispenserBehaviour INSTANCE = new FireDragonBreathDispenserBehaviour();

	@Override
	protected ItemStack dispenseSilently(BlockPointer source, ItemStack stack)
	{
		Direction sourceFace = source.getBlockState().get(DispenserBlock.FACING);
		World world = source.getWorld();
		BlockPos blockpos = source.getPos().offset(sourceFace);

		// Vanilla copy of DragonFireballEntity#onCollision
		AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(world, blockpos.getX() + 0.5, blockpos.getY() + 0.5, blockpos.getZ() + 0.5);
		areaEffectCloudEntity.setOwner(null);  // it doesn't have an entity owner
		areaEffectCloudEntity.setParticleType(ParticleTypes.DRAGON_BREATH);
		areaEffectCloudEntity.setRadius(3.0F);
		areaEffectCloudEntity.setDuration(600);
		areaEffectCloudEntity.setRadiusGrowth((7.0F - areaEffectCloudEntity.getRadius()) / (float)areaEffectCloudEntity.getDuration());
		areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1));
		// use event 2007 from ThrownPotionEntity.java#onCollision instead of event 2006 from DragonFireballEntity#onCollision
		world.syncGlobalEvent(2007, blockpos, areaEffectCloudEntity.getColor());
		world.spawnEntity(areaEffectCloudEntity);
		// Vanilla copy ends

		stack.decrement(1);
		return stack;
	}
}

package carpettisaddition.mixins.option.renewableDragonEgg;

import carpettisaddition.CarpetTISAdditionSettings;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DragonEggBlock;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;
import java.util.Random;


@Mixin(DragonEggBlock.class)
public abstract class DragonEggBlockMixin extends Block
{
	public DragonEggBlockMixin(Settings settings) {
		super(settings);
	}

	@Intrinsic
	@Override
	public boolean hasRandomTicks(BlockState state) {
		return CarpetTISAdditionSettings.renewableDragonEgg;
	}

	@SuppressWarnings("deprecation")
	@Intrinsic
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		if (CarpetTISAdditionSettings.renewableDragonEgg && random.nextInt(64) == 0)
		{
			List<AreaEffectCloudEntity> list = world.getEntitiesByClass(AreaEffectCloudEntity.class, new Box(pos, pos.add(1, 1, 1)), (entity) -> {return entity != null && entity.isAlive();});
			List<AreaEffectCloudEntity> dragonBreath = Lists.newArrayList();
			for (AreaEffectCloudEntity areaEffectCloudEntity : list)
				if (areaEffectCloudEntity.getParticleType() == ParticleTypes.DRAGON_BREATH)
					dragonBreath.add(areaEffectCloudEntity);
			if (!dragonBreath.isEmpty())
			{
				AreaEffectCloudEntity areaEffectCloudEntity = dragonBreath.get(random.nextInt(dragonBreath.size()));
				// copied from vanilla DragonEggBlock.teleport
				// but with only 1 try
				BlockPos blockPos = pos.add(random.nextInt(16) - random.nextInt(16), random.nextInt(8) - random.nextInt(8), random.nextInt(16) - random.nextInt(16));
				if (world.getBlockState(blockPos).isAir())
				{
					if (world.isClient)
						for (int j = 0; j < 128; ++j)
						{
							double d = random.nextDouble();
							float f = (random.nextFloat() - 0.5F) * 0.2F;
							float g = (random.nextFloat() - 0.5F) * 0.2F;
							float h = (random.nextFloat() - 0.5F) * 0.2F;
							double e = MathHelper.lerp(d, (double) blockPos.getX(), (double) pos.getX()) + (random.nextDouble() - 0.5D) + 0.5D;
							double k = MathHelper.lerp(d, (double) blockPos.getY(), (double) pos.getY()) + random.nextDouble() - 0.5D;
							double l = MathHelper.lerp(d, (double) blockPos.getZ(), (double) pos.getZ()) + (random.nextDouble() - 0.5D) + 0.5D;
							world.addParticle(ParticleTypes.PORTAL, e, k, l, (double) f, (double) g, (double) h);
						}
					else
					{
						world.setBlockState(blockPos, state, 2);
						areaEffectCloudEntity.setRadius(areaEffectCloudEntity.getRadius() * 0.2F);
					}
				}
			}
		}
	}
}

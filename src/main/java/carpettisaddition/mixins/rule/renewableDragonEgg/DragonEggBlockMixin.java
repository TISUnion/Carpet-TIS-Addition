/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpettisaddition.mixins.rule.renewableDragonEgg;

import carpettisaddition.CarpetTISAdditionSettings;
import com.google.common.collect.Lists;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.DragonEggBlock;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

//#if MC >= 11900
//$$ import net.minecraft.util.RandomSource;
//#else
import java.util.Random;
//#endif

//#if MC < 11500
//$$ import net.minecraft.world.level.Level;
//#endif

@Mixin(DragonEggBlock.class)
public abstract class DragonEggBlockMixin extends Block
{
	public DragonEggBlockMixin(Properties settings) {
		super(settings);
	}

	@Intrinsic
	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return CarpetTISAdditionSettings.renewableDragonEgg;
	}

	@SuppressWarnings("deprecation")
	@Intrinsic
	@Override
	//#if MC >= 11900
	//$$ public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random)
	//#elseif MC >= 11500
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random)
	//#else
	//$$ public void randomTick(BlockState state, Level world, BlockPos pos, Random random)
	//#endif
	{
		renewableDragonEggImpl(state, world, pos, random);
	}

	private void renewableDragonEggImpl(
			BlockState state, Level world, BlockPos pos,
			//#if MC >= 11900
			//$$ RandomSource random
			//#else
			Random random
			//#endif
	)
	{
		if (CarpetTISAdditionSettings.renewableDragonEgg && random.nextInt(64) == 0)
		{
			AABB box =
					//#if MC >= 12003
					//$$ Box.enclosing
					//#else
					new AABB
					//#endif
							(pos, pos.offset(1, 1, 1));
			List<AreaEffectCloud> list = world.getEntitiesOfClass(AreaEffectCloud.class, box, (entity) -> {return entity != null && entity.isAlive();});
			List<AreaEffectCloud> dragonBreath = Lists.newArrayList();
			for (AreaEffectCloud areaEffectCloudEntity : list)
				if (areaEffectCloudEntity.getParticle() == ParticleTypes.DRAGON_BREATH)
					dragonBreath.add(areaEffectCloudEntity);
			if (!dragonBreath.isEmpty())
			{
				AreaEffectCloud areaEffectCloudEntity = dragonBreath.get(random.nextInt(dragonBreath.size()));
				// copied from vanilla DragonEggBlock.teleport
				// but with only 1 try
				BlockPos blockPos = pos.offset(random.nextInt(16) - random.nextInt(16), random.nextInt(8) - random.nextInt(8), random.nextInt(16) - random.nextInt(16));
				if (world.getBlockState(blockPos).isAir())
				{
					if (world.isClientSide())
						for (int j = 0; j < 128; ++j)
						{
							double d = random.nextDouble();
							float f = (random.nextFloat() - 0.5F) * 0.2F;
							float g = (random.nextFloat() - 0.5F) * 0.2F;
							float h = (random.nextFloat() - 0.5F) * 0.2F;
							double e = Mth.lerp(d, (double) blockPos.getX(), (double) pos.getX()) + (random.nextDouble() - 0.5D) + 0.5D;
							double k = Mth.lerp(d, (double) blockPos.getY(), (double) pos.getY()) + random.nextDouble() - 0.5D;
							double l = Mth.lerp(d, (double) blockPos.getZ(), (double) pos.getZ()) + (random.nextDouble() - 0.5D) + 0.5D;
							world.addParticle(ParticleTypes.PORTAL, e, k, l, (double) f, (double) g, (double) h);
						}
					else
					{
						world.setBlock(blockPos, state, 2);
						areaEffectCloudEntity.setRadius(areaEffectCloudEntity.getRadius() * 0.2F);
					}
				}
			}
		}
	}
}

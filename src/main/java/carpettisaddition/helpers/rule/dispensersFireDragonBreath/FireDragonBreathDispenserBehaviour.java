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

//#if MC >= 12109
//$$ import net.minecraft.class_11978;
//#endif

//#if MC >= 12104
//$$ import java.util.Collections;
//#endif

//#if MC >= 12005
//$$ import net.minecraft.potion.Potions;
//$$ import net.minecraft.component.type.PotionContentsComponent;
//#endif

public class FireDragonBreathDispenserBehaviour extends ItemDispenserBehavior
{
	public static final FireDragonBreathDispenserBehaviour INSTANCE = new FireDragonBreathDispenserBehaviour();

	@Override
	protected ItemStack dispenseSilently(BlockPointer source, ItemStack stack)
	{
		Direction sourceFace = source.getBlockState().get(DispenserBlock.FACING);
		World world = source.getWorld();
		BlockPos blockpos = source.getBlockPos().offset(sourceFace);

		// Vanilla copy of DragonFireballEntity#onCollision
		AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(world, blockpos.getX() + 0.5, blockpos.getY() + 0.5, blockpos.getZ() + 0.5);
		areaEffectCloudEntity.setOwner(null);  // it doesn't have an entity owner
		areaEffectCloudEntity.setParticleType(
				//#if MC >= 1.21.9
				//$$ class_11978.method_74410(ParticleTypes.DRAGON_BREATH, 1.0F)
				//#else
				ParticleTypes.DRAGON_BREATH
				//#endif
		);
		areaEffectCloudEntity.setRadius(3.0F);
		areaEffectCloudEntity.setDuration(600);
		areaEffectCloudEntity.setRadiusGrowth((7.0F - areaEffectCloudEntity.getRadius()) / (float)areaEffectCloudEntity.getDuration());
		StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1);
		areaEffectCloudEntity.addEffect(statusEffectInstance);
		// use event 2007 from ThrownPotionEntity.java#onCollision instead of event 2006 from DragonFireballEntity#onCollision
		world.playLevelEvent(
				2007, blockpos,
				//#if MC >= 12104
				//$$ PotionContentsComponent.mixColors(Collections.singleton(statusEffectInstance)).orElse(0xFF385DC6)  // see also PotionContentsComponent.getColor
				//#elseif MC >= 12005
				//$$ PotionContentsComponent.getColor(Potions.HARMING)
				//#else
				areaEffectCloudEntity.getColor()
				//#endif
		);
		world.spawnEntity(areaEffectCloudEntity);
		// Vanilla copy ends

		stack.decrement(1);
		return stack;
	}
}

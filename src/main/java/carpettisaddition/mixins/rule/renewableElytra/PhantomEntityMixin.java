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

package carpettisaddition.mixins.rule.renewableElytra;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 12102
//$$ import net.minecraft.server.world.ServerWorld;
//#endif

@Mixin(PhantomEntity.class)
public abstract class PhantomEntityMixin extends FlyingEntity
{
	public PhantomEntityMixin(EntityType<? extends PhantomEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@Intrinsic
	@Override
	protected void dropLoot(
			//#if MC >= 12102
			//$$ ServerWorld world,
			//#endif
			DamageSource source, boolean causedByPlayer
	)
	{
		super.dropLoot(
				//#if MC >= 12102
				//$$ world,
				//#endif
				source, causedByPlayer
		);
		if (CarpetTISAdditionSettings.renewableElytra > 0.0D)
		{
			if (source.getAttacker() instanceof ShulkerEntity && this.random.nextDouble() < CarpetTISAdditionSettings.renewableElytra)
			{
				this.dropStack(
						//#if MC >= 12102
						//$$ world,
						//#endif
						new ItemStack(Items.ELYTRA)
				);
			}
		}
	}
}

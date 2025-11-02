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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 12102
//$$ import net.minecraft.server.level.ServerLevel;
//#endif

@Mixin(Phantom.class)
public abstract class PhantomEntityMixin extends Mob
{
	public PhantomEntityMixin(EntityType<? extends Phantom> entityType, Level world)
	{
		super(entityType, world);
	}

	@Intrinsic
	@Override
	protected void dropFromLootTable(
			//#if MC >= 12102
			//$$ ServerLevel world,
			//#endif
			DamageSource source, boolean causedByPlayer
	)
	{
		super.dropFromLootTable(
				//#if MC >= 12102
				//$$ world,
				//#endif
				source, causedByPlayer
		);
		if (CarpetTISAdditionSettings.renewableElytra > 0.0D)
		{
			if (source.getEntity() instanceof Shulker && this.random.nextDouble() < CarpetTISAdditionSettings.renewableElytra)
			{
				this.spawnAtLocation(
						//#if MC >= 12102
						//$$ world,
						//#endif
						new ItemStack(Items.ELYTRA)
				);
			}
		}
	}
}

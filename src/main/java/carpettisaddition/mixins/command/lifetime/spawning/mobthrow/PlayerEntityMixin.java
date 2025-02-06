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

package carpettisaddition.mixins.command.lifetime.spawning.mobthrow;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.MobThrowSpawningReason;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 12105
//$$ import net.minecraft.entity.LivingEntity;
//#else
import net.minecraft.entity.player.PlayerEntity;
//#endif

@Mixin(
		//#if MC >= 12105
		//$$ LivingEntity.class
		//#else
		PlayerEntity.class
		//#endif
)
public abstract class PlayerEntityMixin
{
	@ModifyReturnValue(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("RETURN"))
	private ItemEntity lifetimeTracker_recordSpawning_mobThrow_player(ItemEntity itemEntity)
	{
		if (itemEntity != null)
		{
			Entity self = (Entity)(Object)this;
			((LifetimeTrackerTarget)itemEntity).recordSpawning(new MobThrowSpawningReason(self.getType()));
		}
		return itemEntity;
	}
}

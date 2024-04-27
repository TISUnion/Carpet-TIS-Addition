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

package carpettisaddition.mixins.command.lifetime.removal.mobpickup;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.removal.MobPickupRemovalReason;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin
{
	@Inject(
			method = "onPlayerCollision",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					target = "Lnet/minecraft/entity/ItemEntity;discard()V"
					//#else
					//$$ target = "Lnet/minecraft/entity/ItemEntity;remove()V"
					//#endif
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void lifetimeTracker_recordRemoval_mobPickup_playerPickupItem(PlayerEntity player, CallbackInfo ci, ItemStack itemStack, Item item, int i)
	{
		int stackCount = itemStack.getCount();
		itemStack.setCount(i);
		((LifetimeTrackerTarget)this).recordRemoval(new MobPickupRemovalReason(player.getType()));
		itemStack.setCount(stackCount);
	}
}

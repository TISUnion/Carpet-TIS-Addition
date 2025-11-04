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

package carpettisaddition.mixins.command.lifetime.removal.hopper;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.removal.LiteralRemovalReason;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin
{
	@Inject(
			method = "addItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/entity/item/ItemEntity;)Z",
			at = @At(
					//#if MC >= 12002
					//$$ // record before being set to EMPTY
					//$$ value = "FIELD",
					//$$ target = "Lnet/minecraft/world/item/ItemStack;EMPTY:Lnet/minecraft/world/item/ItemStack;"
					//#else

					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/world/entity/item/ItemEntity;discard()V"
					//#else
					target = "Lnet/minecraft/world/entity/item/ItemEntity;remove()V"
					//#endif
					//#endif
			)
	)
	private static void lifetimeTracker_recordRemoval_hopper(Container inventory, ItemEntity itemEntity, CallbackInfoReturnable<Boolean> cir)
	{
		if (
				//#if MC >= 11700
				//$$ !itemEntity.isRemoved()
				//#else
				!itemEntity.removed
				//#endif
		)
		{
			((LifetimeTrackerTarget)itemEntity).recordRemoval$TISCM(LiteralRemovalReason.HOPPER);
		}
	}
}

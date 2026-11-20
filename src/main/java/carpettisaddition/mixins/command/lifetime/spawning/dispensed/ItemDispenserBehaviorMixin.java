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

package carpettisaddition.mixins.command.lifetime.spawning.dispensed;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//#if MC >= 26.4
//$$ import net.minecraft.core.dispenser.DropItemDispenseBehavior;
//#else
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
//#endif

//#if MC >= 26.4
//$$ @Mixin(DropItemDispenseBehavior.class)
//#else
@Mixin(DefaultDispenseItemBehavior.class)
//#endif
public abstract class ItemDispenserBehaviorMixin
{
	@ModifyArg(
			//#if MC >= 26.4
			//$$ method = "spawnItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/core/Direction;Lnet/minecraft/core/Position;)V",
			//#else
			method = "spawnItem",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
			)
	)
	private static Entity lifetimeTracker_recordSpawning_dispensed_item(Entity itemEntity)
	{
		((LifetimeTrackerTarget)itemEntity).recordSpawning$TISCM(LiteralSpawningReason.DISPENSED);
		return itemEntity;
	}
}

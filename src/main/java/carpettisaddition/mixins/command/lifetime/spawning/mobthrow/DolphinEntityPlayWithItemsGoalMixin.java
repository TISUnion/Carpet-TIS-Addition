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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.animal.Dolphin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "net.minecraft.world.entity.animal.Dolphin$PlayWithItemsGoal")
public abstract class DolphinEntityPlayWithItemsGoalMixin
{
	@SuppressWarnings("target")
	@Shadow @Final
	Dolphin field_6757;

	@ModifyVariable(
			method = "drop",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
			)
	)
	private ItemEntity lifetimeTracker_recordSpawning_mobThrow_dolphin(ItemEntity itemEntity)
	{
		((LifetimeTrackerTarget)itemEntity).recordSpawning$TISCM(new MobThrowSpawningReason(this.field_6757.getType()));
		return itemEntity;
	}
}

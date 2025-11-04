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

package carpettisaddition.mixins.command.lifetime.spawning.summon;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CarvedPumpkinBlock.class)
public abstract class CarvedPumpkinBlockMixin
{
	@ModifyArg(
			//#if MC >= 11903
			//$$ method = "spawnGolemInWorld",
			//#else
			method = "trySpawnGolem",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
			)
			//#if MC < 11903
			, require = 2
			//#endif
	)
	//#if MC >= 11903
	//$$ static
	//#endif
	private Entity lifetimeTracker_recordSpawning_summon_pumpkinGolem(Entity entity)
	{
		((LifetimeTrackerTarget)entity).recordSpawning$TISCM(LiteralSpawningReason.SUMMON);
		return entity;
	}
}

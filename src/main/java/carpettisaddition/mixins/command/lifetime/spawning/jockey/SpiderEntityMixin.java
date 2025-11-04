/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.mixins.command.lifetime.spawning.jockey;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Spider.class)
public abstract class SpiderEntityMixin
{
	@ModifyReceiver(
			method = "finalizeSpawn",
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.21.9
					//$$ target = "Lnet/minecraft/world/entity/monster/Skeleton;startRiding(Lnet/minecraft/world/entity/Entity;ZZ)Z"
					//#else
					target = "Lnet/minecraft/world/entity/monster/Skeleton;startRiding(Lnet/minecraft/world/entity/Entity;)Z"
					//#endif
			)
	)
	private Skeleton lifetimeTracker_recordSpawning_jockey_spider(
			Skeleton skeleton, Entity spider
			//#if MC >= 1.21.9
			//$$ , boolean b1, boolean b2
			//#endif
	)
	{
		((LifetimeTrackerTarget)skeleton).recordSpawning$TISCM(LiteralSpawningReason.JOCKEY);
		return skeleton;
	}
}

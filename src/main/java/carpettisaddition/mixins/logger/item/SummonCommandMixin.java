/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.mixins.logger.item;

import carpettisaddition.logging.loggers.entity.ItemLogger;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.commands.SummonCommand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SummonCommand.class)
public abstract class SummonCommandMixin
{
	@Inject(
			method = "spawnEntity",
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.20
					//$$ target = "Lnet/minecraft/commands/CommandSourceStack;sendSuccess(Ljava/util/function/Supplier;Z)V",
					//#else
					target = "Lnet/minecraft/commands/CommandSourceStack;sendSuccess(Lnet/minecraft/network/chat/Component;Z)V",
					//#endif
					//#if MC >= 1.16
					//$$ ordinal = 0
					//#else
					ordinal = 1
					//#endif
			)
	)
	private static void itemLogger_onSummoned(CallbackInfoReturnable<Integer> cir, @Local Entity entity)
	{
		if (entity instanceof ItemEntity)
		{
			ItemLogger.getInstance().onEntityCreated((ItemEntity)entity);
		}
	}
}

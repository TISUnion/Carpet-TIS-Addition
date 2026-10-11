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

package carpettisaddition.mixins.carpet.shape;

import carpet.script.external.Vanilla;
import carpettisaddition.helpers.carpet.shape.ShapeUtil;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * mc >= 1.19.4: subproject 1.19.4                  <--------
 * mc <  1.19.4: subproject 1.15.2 (main project)
 */
@Mixin(Vanilla.class)
public abstract class VanillaMixin
{
	@ModifyVariable(method = "sendScarpetShapesDataToPlayer", at = @At("HEAD"), argsOnly = true, remap = false)
	private static Tag prepareShapesForPlayer(Tag data, @Local(argsOnly = true) ServerPlayer player)
	{
		return ShapeUtil.prepareShapesForPlayer(player, (ListTag)data);
	}
}

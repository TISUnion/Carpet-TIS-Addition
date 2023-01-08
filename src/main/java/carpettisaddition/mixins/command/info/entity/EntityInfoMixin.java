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

package carpettisaddition.mixins.command.info.entity;

//#if MC >= 11700
//$$ import carpettisaddition.utils.compat.carpet.EntityInfo;
//#else
import carpet.utils.EntityInfo;
//#endif

import carpettisaddition.commands.info.entity.EntityInfoPorting;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.Entity;
import net.minecraft.text.BaseText;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.17"))
@Mixin(EntityInfo.class)
public abstract class EntityInfoMixin
{
	@Inject(method = "entityInfo", at = @At("HEAD"), cancellable = true, remap = false)
	private static void makeItWorkable(Entity e, World source_world, CallbackInfoReturnable<List<BaseText>> cir)
	{
		cir.setReturnValue(EntityInfoPorting.entityInfo(e, source_world));
	}
}

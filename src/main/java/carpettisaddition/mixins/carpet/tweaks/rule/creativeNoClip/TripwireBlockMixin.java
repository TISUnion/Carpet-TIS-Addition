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

package carpettisaddition.mixins.carpet.tweaks.rule.creativeNoClip;

//#if MC >= 11500
import carpet.CarpetSettings;
//#else
//$$ import carpettisaddition.utils.compat.carpet.CarpetSettings;
//#endif

import carpettisaddition.helpers.carpet.tweaks.rule.creativeNoClip.CreativeNoClipHelper;
import carpettisaddition.utils.CollectionUtils;
import net.minecraft.world.level.block.TripWireBlock;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(TripWireBlock.class)
public abstract class TripwireBlockMixin
{
	@ModifyVariable(
			//#if MC >= 12102
			//$$ method = "updatePowered(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Ljava/util/List;)V",
			//$$ at = @At("HEAD"),
			//$$ argsOnly = true
			//#else
			method = "checkPressed",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;isEmpty()Z"
			)
			//#endif
	)
	private List<? extends Entity> dontDetectCreativeNoClipPlayers_tripwire(List<? extends Entity> entities)
	{
		if (CarpetSettings.creativeNoClip)
		{
			entities = CollectionUtils.copyAndRemoveIf(entities, CreativeNoClipHelper::isNoClipPlayer);
		}
		return entities;
	}
}

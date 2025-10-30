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

package carpettisaddition.mixins.rule.blockPlacementIgnoreEntity;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BlockPlaceContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin
{
	@Shadow protected abstract boolean mustSurvive();

	@Inject(method = "canPlace", at = @At(value = "HEAD"), cancellable = true)
	void skipCollisionCheck(BlockPlaceContext context, BlockState state, CallbackInfoReturnable<Boolean> cir)
	{
		if (CarpetTISAdditionSettings.blockPlacementIgnoreEntity)
		{
			Player player = context.getPlayer();
			if (player != null && player.isCreative())
			{
				// partially vanilla copy (removed entity collision check)
				cir.setReturnValue(!this.mustSurvive() || state.canSurvive(context.getLevel(), context.getClickedPos()));
			}
		}
	}
}

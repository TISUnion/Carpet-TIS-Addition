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

package carpettisaddition.mixins.logger.microtiming.marker;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 11600
//$$ import net.minecraft.block.AbstractBlock;
//#else
import net.minecraft.block.BlockState;
//#endif

//#if MC >= 11500
import net.minecraft.util.ActionResult;
//#endif

@Mixin(
		//#if MC >= 11600
		//$$ AbstractBlock.AbstractBlockState.class
		//#else
		BlockState.class
		//#endif
)
public abstract class BlockStateMixin
{
	@Inject(
			//#if MC >= 11500
			method = "onUse",
			//#else
			//$$ method = "activate",
			//#endif
			at = @At("HEAD"),
			cancellable = true
	)
	private void onUseOnBlock$MicroTimingLoggerMarker(
			World world, PlayerEntity player,
			//#if MC < 12005
			Hand hand,
			//#endif
			BlockHitResult hit,
			//#if MC >= 11500
			CallbackInfoReturnable<ActionResult> cir
			//#else
			//$$ CallbackInfoReturnable<Boolean> cir
			//#endif
	)
	{
		//#if MC >= 12005
		//$$ // It's always the main hand
		//$$ var hand = Hand.MAIN_HAND;
		//#endif

		// client side is allowed here so the client wont desync
		boolean accepted = MicroTimingLoggerManager.onPlayerRightClick(player, hand, hit.getBlockPos());
		if (accepted)
		{
			cir.setReturnValue(
					//#if MC >= 11500
					ActionResult.CONSUME
					//#else
					//$$ true
					//#endif
			);
		}
	}
}

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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 11600
//$$ import net.minecraft.world.level.block.state.BlockBehaviour;
//#else
import net.minecraft.world.level.block.state.BlockState;
//#endif

//#if MC >= 11500
import net.minecraft.world.InteractionResult;
//#endif

@Mixin(
		//#if MC >= 11600
		//$$ BlockBehaviour.BlockStateBase.class
		//#else
		BlockState.class
		//#endif
)
public abstract class BlockStateMixin
{
	@Inject(
			method = "use",
			at = @At("HEAD"),
			cancellable = true
	)
	private void onUseOnBlock$MicroTimingLoggerMarker(
			Level world, Player player,
			//#if MC < 12005
			InteractionHand hand,
			//#endif
			BlockHitResult hit,
			//#if MC >= 11500
			CallbackInfoReturnable<InteractionResult> cir
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
					InteractionResult.CONSUME
					//#else
					//$$ true
					//#endif
			);
		}
	}
}

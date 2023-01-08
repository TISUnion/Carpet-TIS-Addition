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

package carpettisaddition.mixins.rule.creativeOpenContainerForcibly;

import carpettisaddition.helpers.rule.creativeOpenContainerForcibly.CreativeOpenContainerForciblyHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin
{
	private static final ThreadLocal<Boolean> ignoreChestBlockedCheck = ThreadLocal.withInitial(() -> false);

	@Inject(
			//#if MC >= 11500
			method = "onUse",
			//#else
			//$$ method = "activate",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/block/ChestBlock;createScreenHandlerFactory(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/screen/NamedScreenHandlerFactory;"
					//#else
					target = "Lnet/minecraft/block/ChestBlock;createContainerFactory(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/container/NameableContainerFactory;"
					//#endif
			)
	)
	private void noCollideOrCreative(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir)
	{
		ignoreChestBlockedCheck.set(CreativeOpenContainerForciblyHelper.canOpenForcibly(player));
	}

	@Inject(
			//#if MC >= 11500
			method = "onUse",
			//#else
			//$$ method = "activate",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/block/ChestBlock;createScreenHandlerFactory(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/screen/NamedScreenHandlerFactory;",
					//#else
					target = "Lnet/minecraft/block/ChestBlock;createContainerFactory(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/container/NameableContainerFactory;",
					//#endif
					shift = At.Shift.AFTER
			)
	)
	private void noCollideOrCreativeReset(CallbackInfoReturnable<ActionResult> cir)
	{
		ignoreChestBlockedCheck.set(false);
	}

	// never try to target ChestBlock#getBlockEntitySource, yarn will not be happy with that at server-side runtime, no intermediary warning
	// see #17
	@Inject(method = "isChestBlocked", at = @At("HEAD"), cancellable = true)
	private static void believeMeTheChestIsNotBlocked(CallbackInfoReturnable<Boolean> cir)
	{
		if (ignoreChestBlockedCheck.get())
		{
			cir.setReturnValue(false);
		}
	}
}

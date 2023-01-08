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

package carpettisaddition.mixins.logger.turtleegg;

import carpettisaddition.logging.loggers.turtleegg.TurtleEggLogger;
import net.minecraft.block.BlockState;
import net.minecraft.block.TurtleEggBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TurtleEggBlock.class)
public abstract class TurtleEggBlockMixin
{
	private final ThreadLocal<Entity> eggBreakingEntity = ThreadLocal.withInitial(() -> null);

	@Inject(method = "breakEgg", at = @At("HEAD"))
	private void onEggBrokenTurtleEggLogger(World world, BlockPos pos, BlockState state, CallbackInfo ci)
	{
		if (TurtleEggLogger.getInstance().isActivated())
		{
			TurtleEggLogger.getInstance().onBreakingEgg(world, pos, state, this.eggBreakingEntity.get());
		}
	}

	@Inject(method = "tryBreakEgg", at = @At("HEAD"))
	private void recordEntityTurtleEggLogger(
			//#if MC >= 11700
			//$$ World world, BlockState blockState, BlockPos blockPos, Entity entity, int i,
			//#else
			World world, BlockPos pos, Entity entity, int inverseChance,
			//#endif
			CallbackInfo ci
	)
	{
		if (TurtleEggLogger.getInstance().isActivated())
		{
			this.eggBreakingEntity.set(entity);
		}
	}

	@Inject(method = "afterBreak", at = @At("HEAD"))
	private void recordEntityTurtleEggLogger(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack, CallbackInfo ci)
	{
		if (TurtleEggLogger.getInstance().isActivated())
		{
			this.eggBreakingEntity.set(player);
		}
	}
}

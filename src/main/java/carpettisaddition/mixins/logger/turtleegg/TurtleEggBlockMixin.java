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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TurtleEggBlock.class)
public abstract class TurtleEggBlockMixin
{
	@Unique
	private final ThreadLocal<Entity> eggBreakingEntity = ThreadLocal.withInitial(() -> null);

	@Inject(method = "decreaseEggs", at = @At("HEAD"))
	private void onEggBrokenTurtleEggLogger(Level world, BlockPos pos, BlockState state, CallbackInfo ci)
	{
		if (TurtleEggLogger.getInstance().isActivated())
		{
			TurtleEggLogger.getInstance().onBreakingEgg(world, pos, state, this.eggBreakingEntity.get());
		}
	}

	@Inject(method = "destroyEgg", at = @At("HEAD"))
	private void recordEntityTurtleEggLogger(
			//#if MC >= 11700
			//$$ Level world, BlockState blockState, BlockPos blockPos, Entity entity, int i,
			//#else
			Level world, BlockPos pos, Entity entity, int inverseChance,
			//#endif
			CallbackInfo ci
	)
	{
		if (TurtleEggLogger.getInstance().isActivated())
		{
			this.eggBreakingEntity.set(entity);
		}
	}

	@Inject(method = "playerDestroy", at = @At("HEAD"))
	private void recordEntityTurtleEggLogger(Level world, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack, CallbackInfo ci)
	{
		if (TurtleEggLogger.getInstance().isActivated())
		{
			this.eggBreakingEntity.set(player);
		}
	}
}

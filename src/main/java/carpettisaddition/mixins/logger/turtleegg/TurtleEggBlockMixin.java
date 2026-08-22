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
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 26.3
//$$ import net.minecraft.server.level.ServerPlayer;
//#else
import net.minecraft.world.entity.player.Player;
//#endif

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
	private void recordEntityTurtleEggLogger(CallbackInfo ci, @Local(argsOnly = true) Entity entity)
	{
		if (TurtleEggLogger.getInstance().isActivated())
		{
			this.eggBreakingEntity.set(entity);
		}
	}

	@Inject(method = "playerDestroy", at = @At("HEAD"))
	private void recordEntityTurtleEggLogger(
			CallbackInfo ci,
			//#if MC >= 26.3
			//$$ @Local(argsOnly = true) ServerPlayer player
			//#else
			@Local(argsOnly = true) Player player
			//#endif
	)
	{
		if (TurtleEggLogger.getInstance().isActivated())
		{
			this.eggBreakingEntity.set(player);
		}
	}
}

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

package carpettisaddition.mixins.logger.microtiming.events.blockupdate;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 11600
//$$ import net.minecraft.block.AbstractBlock;
//#endif

/**
 * Traditional block / state update stuffs, before mc 1.19
 */
@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.19"))
@Mixin(
		//#if MC >= 11600
		//$$ AbstractBlock.AbstractBlockState.class
		//#else
		Block.class
		//#endif
)
public abstract class BlockMixin
{
	@Inject(
			//#if MC >= 11600
			//$$ method = "updateNeighbors(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;II)V",
			//#else
			method = "updateNeighbourShapes",
			//#endif
			at = @At("HEAD")
	)
	private void startStateUpdate(
			//#if MC >= 11600
			//$$ WorldAccess world, BlockPos pos, int flags, int maxUpdateDepth,
			//#else
			BlockState state, LevelAccessor world, BlockPos pos, int flags,
			//#endif
			CallbackInfo ci
	)
	{
		if (world instanceof Level)
		{
			MicroTimingLoggerManager.onBlockUpdate((Level)world, pos, world.getBlockState(pos).getBlock(), BlockUpdateType.STATE_UPDATE, null, EventType.ACTION_START);
		}
	}

	@Inject(
			//#if MC >= 11600
			//$$ method = "updateNeighbors(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;II)V",
			//#else
			method = "updateNeighbourShapes",
			//#endif
			at = @At("RETURN")
	)
	private void endStateUpdate(
			//#if MC >= 11600
			//$$ WorldAccess world, BlockPos pos, int flags, int maxUpdateDepth,
			//#else
			BlockState state, LevelAccessor world, BlockPos pos, int flags,
			//#endif
			CallbackInfo ci
	)
	{
		if (world instanceof Level)
		{
			MicroTimingLoggerManager.onBlockUpdate((Level)world, pos, world.getBlockState(pos).getBlock(), BlockUpdateType.STATE_UPDATE, null, EventType.ACTION_END);
		}
	}
}

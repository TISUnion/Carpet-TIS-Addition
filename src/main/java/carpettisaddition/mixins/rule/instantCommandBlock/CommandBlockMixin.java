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

package carpettisaddition.mixins.rule.instantCommandBlock;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.instantCommandBlock.ICommandBlockExecutor;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CommandBlock;
import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 11900
//#disable-remap
//$$ import net.minecraft.util.math.random.Random;
//#enable-remap
//#else
import java.util.Random;
//#endif

@Mixin(CommandBlock.class)
public abstract class CommandBlockMixin
{
	@Shadow public abstract void
	//#if MC >= 11500
	//#disable-remap
	scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random);
	//#enable-remap
	//#else
	//$$ onScheduledTick(BlockState state, World world, BlockPos pos, Random random);
	//#endif

	@Inject(
			//#if MC >= 12200
			//$$ method = "update",
			//#else
			method = "neighborUpdate",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11800
					//$$ target = "Lnet/minecraft/world/World;createAndScheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"
					//#else
					target = "Lnet/minecraft/world/TickScheduler;schedule(Lnet/minecraft/util/math/BlockPos;Ljava/lang/Object;I)V"
					//#endif
			),
			cancellable = true
	)
	private void justExecuteRightNow(
			CallbackInfo ci,
			//#if MC < 12200
			@Local(argsOnly = true) BlockState state,
			//#endif
			@Local(argsOnly = true) World world,
			@Local(argsOnly = true, ordinal = 0) BlockPos pos,
			@Local(
					//#if MC >= 12200
					//$$ argsOnly = true
					//#endif
			)
			CommandBlockBlockEntity commandBlockBlockEntity
	)
	{
		if (CarpetTISAdditionSettings.instantCommandBlock)
		{
			if (world instanceof ServerWorld && commandBlockBlockEntity.getCommandBlockType() == CommandBlockBlockEntity.Type.REDSTONE)
			{
				//#if MC >= 12200
				//$$ BlockState state = world.getBlockState(pos);
				//$$ if (!(state.getBlock() instanceof CommandBlock))
				//$$ {
				//$$ 	return;
				//$$ }
				//#endif

				ServerWorld serverWorld = (ServerWorld)world;
				Block blockBelow = world.getBlockState(pos.down()).getBlock();
				if (blockBelow == Blocks.REDSTONE_ORE)
				{
					ICommandBlockExecutor icbe = (ICommandBlockExecutor)commandBlockBlockEntity.getCommandExecutor();
					icbe.setIgnoreWorldTimeCheck(true);

					//#if MC >= 11500
					this.scheduledTick
					//#else
					//$$ this.onScheduledTick
					//#endif
							(state, serverWorld, pos, serverWorld.getRandom());

					icbe.setIgnoreWorldTimeCheck(false);
					ci.cancel();
				}
			}
		}
	}
}

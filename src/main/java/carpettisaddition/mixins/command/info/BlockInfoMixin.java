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

package carpettisaddition.mixins.command.info;

import carpet.utils.BlockInfo;
import carpettisaddition.commands.info.InfoCommandExtension;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BlockInfo.class)
public abstract class BlockInfoMixin
{
	/**
	 * in mc < 1.14, the world arg is a World,
	 *in mc >= 1.15, the world arg is a ServerWorld,
	 * so we use a @Coerce for simplest compatibility
	 **/
	@Inject(method = "blockInfo", at = @At("TAIL"), remap = false)
	private static void showMoreBlockInfo(BlockPos pos, @Coerce World world, CallbackInfoReturnable<List<BaseText>> cir)
	{
		cir.getReturnValue().addAll(InfoCommandExtension.getInstance().showMoreBlockInfo(pos, world));
	}
}

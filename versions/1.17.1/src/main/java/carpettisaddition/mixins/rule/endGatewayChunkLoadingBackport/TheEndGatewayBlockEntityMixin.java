/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.endGatewayChunkLoadingBackport;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * mc1.14   ~ mc1.16.5: subproject 1.15.2 (main project)
 * mc1.17.1 ~ mc1.20.6: subproject 1.17.1        <--------
 * mc1.21+            : subproject 1.21.1
 */
@Mixin(TheEndGatewayBlockEntity.class)
public abstract class TheEndGatewayBlockEntityMixin extends TheEndPortalBlockEntity
{
	protected TheEndGatewayBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState)
	{
		super(blockEntityType, blockPos, blockState);
	}

	@Inject(
			method = "teleportEntity",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/Entity;teleportToWithTicket(DDD)V"
			)
	)
	private static void endGatewayChunkLoadingBackport_impl(
			CallbackInfo ci,
			@Local(argsOnly = true) Level level,
			@Local(ordinal = 1) BlockPos exitPos
	)
	{
		if (CarpetTISAdditionSettings.endGatewayChunkLoadingBackport)
		{
			if (level instanceof ServerLevel)
			{
				((ServerLevel)level).getChunkSource().addRegionTicket(TicketType.PORTAL, new ChunkPos(exitPos), 3, exitPos);
			}
		}
	}
}

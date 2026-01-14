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

package carpettisaddition.mixins.rule.endPortalChunkLoadingBackport;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.portal.PortalInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * mc1.14   ~ mc1.15.2: subproject 1.15.2 (main project)
 * mc1.16.5 ~ mc1.20.4: subproject 1.16.5        <--------
 * mc1.20.5+          : subproject 1.20.6
 */
@Mixin(Entity.class)
public abstract class EntityMixin
{
	/**
	 * The same as what 24w03a did to {@link net.minecraft.world.entity.Entity#findDimensionEntryPoint}
	 */
	@Inject(
			method = "findDimensionEntryPoint",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/portal/PortalInfo;<init>(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;FF)V"
			)
	)
	private void endPortalChunkLoadingBackport_impl(
			CallbackInfoReturnable<PortalInfo> cir,
			@Local(argsOnly = true) ServerLevel serverLevel,
			@Local BlockPos targetPos
	)
	{
		if (CarpetTISAdditionSettings.endPortalChunkLoadingBackport)
		{
			serverLevel.getChunkSource().addRegionTicket(TicketType.PORTAL, new ChunkPos(targetPos), 3, targetPos);
		}
	}
}

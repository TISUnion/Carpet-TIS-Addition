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

package carpettisaddition.mixins.carpet.tweaks.command.playerActionEnhanced;

//#if MC > 11903
//$$ import carpet.script.utils.Tracer;
//#else
import carpet.helpers.Tracer;
//#endif
import carpettisaddition.helpers.carpet.playerActionEnhanced.IEntityPlayerActionPackActionTypeUse;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "carpet.helpers.EntityPlayerActionPack$ActionType$1")
public class EntityPlayerActionPackActionTypeUseMixin implements IEntityPlayerActionPackActionTypeUse
{
	private float tickPart = 1.0f;

	@Redirect(
		method = "execute",
		at = @At(
			value = "INVOKE",
			target = "Lcarpet/helpers/EntityPlayerActionPack;getTarget(Lnet/minecraft/server/network/ServerPlayerEntity;)Lnet/minecraft/util/hit/HitResult;",
			remap = false
		),
		remap = false
	)
	private HitResult doRayTrace(ServerPlayerEntity player)
	{
		double reach = player.interactionManager.isCreative() ? 5 : 4.5f;
		return Tracer.rayTrace(player, tickPart, reach, false);
	}

	@Override
	public void setTickPart(float f)
	{
		tickPart = f;
	}
}

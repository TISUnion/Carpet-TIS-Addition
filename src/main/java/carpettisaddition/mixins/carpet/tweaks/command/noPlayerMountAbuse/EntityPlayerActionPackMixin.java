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

package carpettisaddition.mixins.carpet.tweaks.command.noPlayerMountAbuse;

import carpet.helpers.EntityPlayerActionPack;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(EntityPlayerActionPack.class)
public abstract class EntityPlayerActionPackMixin
{
	@Shadow(remap = false) @Final private ServerPlayerEntity player;

	@Inject(
			method = "mount",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;size()I"
			),
			locals = LocalCapture.CAPTURE_FAILHARD,
			remap = false
	)
	private void pleaseDoSomePermissionCheck(boolean onlyRideables, CallbackInfoReturnable<EntityPlayerActionPack> cir, List<Entity> entities)
	{
		if (!onlyRideables)  // mount anything
		{
			MinecraftServer minecraftServer = this.player.getServer();
			if (minecraftServer != null)
			{
				if (!minecraftServer.getPlayerManager().isOperator(this.player.getGameProfile()))  // not op
				{
					// hey that's illegal
					Messenger.tell(this.player, Messenger.formatting(new Translator("misc").tr("player_mount_anything_permission_denied"), "r"));
					entities.clear();
				}
			}
		}
	}
}

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
import carpettisaddition.utils.PlayerUtils;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EntityPlayerActionPack.class)
public abstract class EntityPlayerActionPackMixin
{
	@Shadow(remap = false) @Final private ServerPlayer player;

	@Inject(
			method = "mount",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;size()I"
			),
			remap = false
	)
	private void pleaseDoSomePermissionCheck(boolean onlyRideables, CallbackInfoReturnable<EntityPlayerActionPack> cir, @Local List<Entity> entities)
	{
		if (!onlyRideables)  // mount anything
		{
			MinecraftServer minecraftServer = PlayerUtils.getServerFromPlayer(this.player);
			if (minecraftServer != null)
			{
				if (!PlayerUtils.isOperator(minecraftServer, this.player))  // not op
				{
					// hey that's illegal
					Messenger.tell(this.player, Messenger.formatting(new Translator("misc").tr("player_mount_anything_permission_denied"), "r"));
					entities.clear();
				}
			}
		}
	}
}

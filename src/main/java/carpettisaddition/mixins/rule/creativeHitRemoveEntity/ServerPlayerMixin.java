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

package carpettisaddition.mixins.rule.creativeHitRemoveEntity;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.creativeHitRemoveEntity.CreativeHitRemoveEntityHelper;
import carpettisaddition.utils.EntityUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		//#if MC >= 26.1
		//$$ Player.class
		//#else
		ServerPlayer.class
		//#endif
)
public abstract class ServerPlayerMixin
{
	@Inject(
			method = "attack",
			at = @At(
					//#if MC >= 26.1
					//$$ value = "HEAD"
					//#else
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/player/Player;attack(Lnet/minecraft/world/entity/Entity;)V"
					//#endif
			),
			cancellable = true
	)
	private void creativeHitRemoveEntity_impl(Entity victim, CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.creativeHitRemoveEntity)
		{
			Player self = (Player)(Object)this;
			Level level = EntityUtils.getEntityWorld(self);
			if (!level.isClientSide() && self instanceof ServerPlayer && EntityUtils.isCreativePlayer(self))
			{
				CreativeHitRemoveEntityHelper.removeEntityHit((ServerPlayer)self, victim);
				ci.cancel();
			}
		}
	}
}

/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.mixins.carpet.tweaks.command.fakePlayerRejoin;

import carpet.patches.EntityPlayerMPFake;
import carpettisaddition.helpers.carpet.tweaks.command.fakePlayerRejoin.FakePlayerRejoinHelper;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 11600
//$$ import net.minecraft.server.level.ServerLevel;
//#endif

@Mixin(EntityPlayerMPFake.class)
public abstract class EntityPlayerMPFake_DisableTeleportMixin
{
	@WrapWithCondition(
			//#if MC >= 26.1
			//$$ method = "lambda$createFake$0",
			//#elseif MC >= 12002
			//$$ method = "lambda$createFake$2",
			//#else
			method = "createFake",
			//#endif
			at = @At(
					value = "FIELD",
					target = "Lcarpet/patches/EntityPlayerMPFake;fixStartingPosition:Ljava/lang/Runnable;",
					remap = false
			)
	)
	private static boolean fakePlayerRejoin_disableFixerOnRejoin(EntityPlayerMPFake instance, Runnable newValue)
	{
		return !FakePlayerRejoinHelper.isRejoin.get();
	}

	@WrapWithCondition(
			//#if MC >= 26.1
			//$$ method = "lambda$createFake$0",
			//#elseif MC >= 12002
			//$$ method = "lambda$createFake$2",
			//#else
			method = "createFake",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 12102
					//$$ target = "Lcarpet/patches/EntityPlayerMPFake;teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FFZ)Z"
					//#elseif MC >= 11600
					//$$ target = "Lcarpet/patches/EntityPlayerMPFake;teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V"
					//#else
					target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;teleport(DDDFF)V"
					//#endif
			)
	)
	private static boolean fakePlayerRejoin_dontRequestTeleport(
			//#if MC >= 12102
			//$$ EntityPlayerMPFake instance, ServerLevel serverWorld, double x, double y, double z, java.util.Set<?> set, float yaw, float pitch, boolean resetCamera
			//#elseif MC >= 11600
			//$$ EntityPlayerMPFake instance, ServerLevel serverWorld, double x, double y, double z, float yaw, float pitch
			//#else
			ServerGamePacketListenerImpl instance, double x, double y, double z, float yaw, float pitch
			//#endif
	)
	{
		return !FakePlayerRejoinHelper.isRejoin.get();
	}
}

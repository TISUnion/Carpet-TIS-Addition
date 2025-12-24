/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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
import carpettisaddition.utils.compat.DimensionWrapper;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

//#if MC >= 1.20.2
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#else
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

//#if MC >= 1.16
//$$ import net.minecraft.resources.ResourceKey;
//$$ import net.minecraft.world.level.Level;
//#else
import net.minecraft.world.level.dimension.DimensionType;
//#endif

/**
 * mc1.14 ~ mc1.21.5: subproject 1.15.2 (main project)        <--------
 * mc1.21.9+        : subproject 1.21.10
 */
@Mixin(EntityPlayerMPFake.class)
public abstract class EntityPlayerMPFake_FixDimensionMixin
{
	/**
	 * In mc <= 1.21.9, a {@link net.minecraft.server.level.ServerPlayer} is firstly constructed with any dimension,
	 * and then its NBT data will be loaded and its real dimension will be properly set inside:
	 *   - (mc < 1.16): during calling {@link net.minecraft.server.players.PlayerList#load} inside  {@link net.minecraft.server.players.PlayerList#placeNewPlayer}
	 *   - (1.16 <= mc < 1.21.9): read directly from NBT in  {@link net.minecraft.server.players.PlayerList#placeNewPlayer}
	 * <p>
	 * In MC >= 25w31a (1.21.9 snapshot), a player's data is loaded properly inside {@link net.minecraft.server.network.config.PrepareSpawnTask#start},
	 * and carpet mimic that with {@link carpet.patches.EntityPlayerMPFake#loadPlayerData} (requiring fabric-carpet >= 1.21.10-1.4.188+v251016).
	 * Also see the mixin {@link #fakePlayerRejoin_setDimensionFromLoadedData} below
	 * <p>
	 * Anyway, we just need to use the loaded player's dimension to replace all dimension / world variables in the createFake function
	 */
	@Inject(
			//#if MC >= 1.20.2
			//$$ method = "lambda$createFake$2",
			//#else
			method = "createFake",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.21.10
					//$$ target = "Lcarpet/patches/EntityPlayerMPFake;loadPlayerData(Lcarpet/patches/EntityPlayerMPFake;)V",
					//$$ remap = false,
					//#elseif MC >= 1.20.2
					//$$ target = "Lnet/minecraft/server/players/PlayerList;placeNewPlayer(Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/network/CommonListenerCookie;)V",
					//#else
					target = "Lnet/minecraft/server/players/PlayerList;placeNewPlayer(Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;)V",
					//#endif
					shift = At.Shift.AFTER
			)
	)
	private static void fakePlayerRejoin_adjustDimension(
			//#if MC >= 1.20.2
			//$$ CallbackInfo ci,
			//#else
			CallbackInfoReturnable<EntityPlayerMPFake> cir,
			//#endif

			@Local EntityPlayerMPFake player,
			@Local(argsOnly = true) MinecraftServer server,

			//#if MC >= 1.16
			//$$ @Local(argsOnly = true) LocalRef<ResourceKey<Level>> targetDimension,
			//#else
			@Local(argsOnly = true) LocalRef<DimensionType> targetDimension,
			//#endif

			//#if MC >= 1.20.2
			//$$ @Local(argsOnly = true) LocalRef<ServerLevel> targetWorld
			//#else
			@Local LocalRef<ServerLevel> targetWorld
			//#endif
	)
	{
		if (FakePlayerRejoinHelper.isRejoin.get())
		{
			//#if MC >= 1.16
			//$$ ResourceKey<Level> playerDimension = DimensionWrapper.of(player).getValue();
			//#else
			DimensionType playerDimension = DimensionWrapper.of(player).getValue();
			//#endif

			targetDimension.set(playerDimension);
			targetWorld.set(server.getLevel(playerDimension));
		}
	}
}

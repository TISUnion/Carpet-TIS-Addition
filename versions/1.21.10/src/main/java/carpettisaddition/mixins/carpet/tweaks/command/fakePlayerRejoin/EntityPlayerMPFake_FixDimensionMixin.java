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
import carpettisaddition.CarpetTISAdditionMod;
import carpettisaddition.helpers.carpet.tweaks.command.fakePlayerRejoin.FakePlayerRejoinHelper;
import carpettisaddition.helpers.carpet.tweaks.command.fakePlayerRejoin.ValueInputHolder;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.authlib.GameProfile;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * mc1.14 ~ mc1.21.5: subproject 1.15.2 (main project)
 * mc1.21.9+        : subproject 1.21.10        <--------
 */
@Mixin(EntityPlayerMPFake.class)
public abstract class EntityPlayerMPFake_FixDimensionMixin
{
	/**
	 * The callback inside the {@link carpet.patches.EntityPlayerMPFake#createFake} method always runs in the server thread,
	 * so no ThreadLocal needed here
	 */
	@Unique
	@Nullable
	private static ValueInputHolder loadedPlayerData$TISCM = null;

	/**
	 * In MC >= 25w31a (1.21.9 snapshot), a player's data is loaded properly inside {@link net.minecraft.server.network.config.PrepareSpawnTask#start},
	 * and carpet mimic that with {@link carpet.patches.EntityPlayerMPFake#loadPlayerData} (requiring fabric-carpet >= 1.21.10-1.4.188+v251016).
	 * <p>
	 * Surprisingly and sadly, carpet reads the player's data **after** constructing the {@link carpet.patches.EntityPlayerMPFake} object
	 * (and it's even after {@link net.minecraft.server.players.PlayerList#placeNewPlayer}),
	 * making carpet's {@link carpet.patches.EntityPlayerMPFake#loadPlayerData} too late for our place to acquire what dimension the player was
	 * <p>
	 * So we need to:
	 * 1. Perform our own player-data-reading before the {@link carpet.patches.EntityPlayerMPFake} is constructed
	 * 2. Reuse our {@link net.minecraft.world.level.storage.ValueInput} in carpet's {@link carpet.patches.EntityPlayerMPFake#loadPlayerData},
	 *    ensuring data consistency
	 * <p>
	 * Anyway, we just need to use the loaded player's dimension to replace all dimension / world variables in the createFake function
	 * <p>
	 * Reference: {@link net.minecraft.server.network.config.PrepareSpawnTask#start}
	 */
	@SuppressWarnings("deprecation")  // it's used in vanilla Minecraft..
	@ModifyArg(
			//#if MC >= 26.1
			//$$ method = "lambda$createFake$0",
			//#elseif MC >= 1.20.2
			method = "lambda$createFake$2",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lcarpet/patches/EntityPlayerMPFake;<init>(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/server/level/ServerLevel;Lcom/mojang/authlib/GameProfile;Lnet/minecraft/server/level/ClientInformation;Z)V"
			)

	)
	private static ServerLevel fakePlayerRejoin_setDimensionFromLoadedData(
			ServerLevel playerWorld,
			@Local(ordinal = 2) GameProfile current,
			@Share("rejoinSnapper") LocalRef<Consumer<EntityPlayerMPFake>> rejoinSnapperRef
	)
	{
		if (FakePlayerRejoinHelper.isRejoin.get())
		{
			var server = playerWorld.getServer();

			ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(CarpetTISAdditionMod.LOGGER);
			boolean needClose = true;
			try
			{
				var valueInputOptional = server.getPlayerList().
						loadPlayerData(new NameAndId(current)).
						map(nbt -> TagValueInput.create(scopedCollector, server.registryAccess(), nbt));
				if (valueInputOptional.isPresent())
				{
					var savedPosition = valueInputOptional.flatMap(valueInput -> valueInput.read(ServerPlayer.SavedPosition.MAP_CODEC));
					var loadedPosition = savedPosition.flatMap(ServerPlayer.SavedPosition::position);
					var loadedRotation = savedPosition.flatMap(ServerPlayer.SavedPosition::rotation);
					var loadedWorld = savedPosition.
							flatMap(ServerPlayer.SavedPosition::dimension).
							flatMap(dim -> Optional.ofNullable(server.getLevel(dim)));
					if (loadedPosition.isPresent() && loadedRotation.isPresent() && loadedWorld.isPresent())
					{
						playerWorld = loadedWorld.get();
						loadedPlayerData$TISCM = new ValueInputHolder(valueInputOptional.get(), scopedCollector);
						rejoinSnapperRef.set(epf -> epf.snapTo(loadedPosition.get(), loadedRotation.get().x, loadedRotation.get().y));
						needClose = false;
					}
				}
			}
			finally
			{
				if (needClose)
				{
					scopedCollector.close();
				}
			}
		}
		return playerWorld;
	}

	@Inject(
			//#if MC >= 26.1
			//$$ method = "lambda$createFake$0",
			//#elseif MC >= 1.20.2
			method = "lambda$createFake$2",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/players/PlayerList;placeNewPlayer(Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/network/CommonListenerCookie;)V"
			)
	)
	private static void fakePlayerRejoin_adjustDimensionAndUseTheRejoinPositionFixer(
			CallbackInfo ci,
			@Local EntityPlayerMPFake player,
			@Local(argsOnly = true) MinecraftServer server,
			@Local(argsOnly = true) LocalRef<ResourceKey<Level>> targetDimension,
			@Local(argsOnly = true) LocalRef<ServerLevel> targetWorld,
			@Share("rejoinSnapper") LocalRef<Consumer<EntityPlayerMPFake>> rejoinSnapperRef
	)
	{
		if (FakePlayerRejoinHelper.isRejoin.get())
		{
			ResourceKey<Level> playerDimension = DimensionWrapper.of(player).getValue();

			targetDimension.set(playerDimension);
			targetWorld.set(server.getLevel(playerDimension));

			var rejoinSnapper = rejoinSnapperRef.get();
			if (rejoinSnapper != null)
			{
				player.fixStartingPosition = () -> rejoinSnapper.accept(player);
			}
		}
	}

	@WrapOperation(
			method = "loadPlayerData",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/players/PlayerList;loadPlayerData(Lnet/minecraft/server/players/NameAndId;)Ljava/util/Optional;"
			)
	)
	private static Optional<ValueInput> fakePlayerRejoin_useOutValueInput(PlayerList instance, NameAndId nameAndId, Operation<Optional<ValueInput>> original)
	{
		if (loadedPlayerData$TISCM != null)
		{
			return Optional.of(loadedPlayerData$TISCM.valueInput());
		}
		return original.call(instance, nameAndId);
	}

	@Inject(
			//#if MC >= 26.1
			//$$ method = "lambda$createFake$0",
			//#elseif MC >= 1.20.2
			method = "lambda$createFake$2",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lcarpet/patches/EntityPlayerMPFake;loadPlayerData(Lcarpet/patches/EntityPlayerMPFake;)V",
					shift = At.Shift.AFTER
			)
	)
	private static void fakePlayerRejoin_closeValueOutputCloser(CallbackInfo ci) throws Exception
	{
		if (loadedPlayerData$TISCM != null)
		{
			loadedPlayerData$TISCM.close();
			loadedPlayerData$TISCM = null;
		}
	}
}

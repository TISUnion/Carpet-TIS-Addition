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

package carpettisaddition.mixins.rule.fakePlayerRemoteSpawning;

import carpet.commands.PlayerCommand;
import carpet.utils.Messenger;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.CarpetModUtil;
import carpettisaddition.utils.CommandUtils;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 11600
//$$ import net.minecraft.util.registry.RegistryKey;
//$$ import net.minecraft.world.World;
//#else
import net.minecraft.world.level.dimension.DimensionType;
//#endif

@Mixin(PlayerCommand.class)
public abstract class PlayerCommandMixin
{
	@Inject(
			method = "spawn",
			at = @At(
					value = "INVOKE",
					//#if MC >= 12003
					//$$ target = "Lcarpet/patches/EntityPlayerMPFake;createFake(Ljava/lang/String;Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/util/math/Vec3d;DDLnet/minecraft/registry/RegistryKey;Lnet/minecraft/world/GameMode;Z)Z"
					//#elseif MC >= 12002
					//$$ target = "Lcarpet/patches/EntityPlayerMPFake;createFake(Ljava/lang/String;Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/util/math/Vec3d;DDLnet/minecraft/registry/RegistryKey;Lnet/minecraft/world/GameMode;ZLjava/lang/Runnable;)V"
					//#elseif MC >= 12000
					//$$ target = "Lcarpet/patches/EntityPlayerMPFake;createFake(Ljava/lang/String;Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/util/math/Vec3d;DDLnet/minecraft/registry/RegistryKey;Lnet/minecraft/world/GameMode;Z)Lcarpet/patches/EntityPlayerMPFake;"
					//#elseif MC >= 11600
					//$$ target = "Lcarpet/patches/EntityPlayerMPFake;createFake(Ljava/lang/String;Lnet/minecraft/server/MinecraftServer;DDDDDLnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/world/GameMode;Z)Lcarpet/patches/EntityPlayerMPFake;"
					//#else
					target = "Lcarpet/patches/EntityPlayerMPFake;createFake(Ljava/lang/String;Lnet/minecraft/server/MinecraftServer;DDDDDLnet/minecraft/world/level/dimension/DimensionType;Lnet/minecraft/world/level/GameType;)Lcarpet/patches/EntityPlayerMPFake;"
					//#endif
			),
			cancellable = true
	)
	private static void fakePlayerRemoteSpawning_permissionCheck(
			CommandContext<CommandSourceStack> context, CallbackInfoReturnable<Integer> cir,
			@Local(ordinal = 0) CommandSourceStack source,
			@Local(ordinal = 0) Vec3 botPos,
			//#if MC >= 11600
			//$$ @Local(ordinal = 0) RegistryKey<World> dim
			//#else
			@Local(ordinal = 0) DimensionType dim
			//#endif
	)
	{
		if (CarpetModUtil.hasEnoughPermission(source, CarpetTISAdditionSettings.fakePlayerRemoteSpawning))
		{
			return;
		}

		final int MAX_ALLOWED_REMOTE_RANGE = 16;
		boolean allowRemoteSpawn = CommandUtils.canCheat(source) || CommandUtils.isCreativePlayer(source);
		if (!allowRemoteSpawn)
		{
			Vec3 sourcePos = source.getPosition();
			DimensionWrapper sourceDimension = DimensionWrapper.of(source.getLevel());
			DimensionWrapper botDimension = DimensionWrapper.of(dim);

			// only allow remote bot spawning iff. the bot pos is closed enough to the player
			if (!sourceDimension.equals(botDimension) || botPos.distanceTo(sourcePos) >= MAX_ALLOWED_REMOTE_RANGE)
			{
				Messenger.m(source, "rb Remote player spawning is not allowed");
				cir.setReturnValue(0);
			}
		}
	}
}

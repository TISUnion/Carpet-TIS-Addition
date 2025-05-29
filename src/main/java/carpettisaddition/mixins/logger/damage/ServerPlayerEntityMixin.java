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

package carpettisaddition.mixins.logger.damage;

import carpettisaddition.logging.loggers.damage.DamageLogger;
import carpettisaddition.logging.loggers.damage.interfaces.DamageLoggerTarget;
import carpettisaddition.logging.loggers.damage.modifyreasons.ModifyReason;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 11900
//$$ import net.minecraft.network.encryption.PlayerPublicKey;
//$$ import org.jetbrains.annotations.Nullable;
//#endif

//#if MC >= 11600
//$$ import net.minecraft.util.math.BlockPos;
//#endif

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity
{
	//#if MC >= 12106
	//$$ public ServerPlayerEntityMixin(World world, GameProfile gameProfile)
	//$$ {
	//$$ 	super(world, gameProfile);
	//$$ }
	//#elseif MC >= 11903
	//$$ public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile)
	//$$ {
	//$$ 	super(world, pos, yaw, gameProfile);
	//$$ }
	//#elseif MC >= 11900
	//$$ public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile, @Nullable PlayerPublicKey playerPublicKey)
	//$$ {
	//$$ 	super(world, pos, yaw, profile, playerPublicKey);
	//$$ }
	//#elseif MC >= 11600
	//$$ public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile)
	//$$ {
	//$$ 	super(world, pos, yaw, profile);
	//$$ }
	//#else
	public ServerPlayerEntityMixin(World world, GameProfile profile)
	{
		super(world, profile);
	}
	//#endif

	// at the start of player damage calculation
	@Inject(
			//#disable-remap
			method = "damage",
			//#enable-remap
			at = @At(
					value = "INVOKE",
					//#if MC >= 12104
					//$$ target = "Lnet/minecraft/entity/damage/DamageSource;getAttacker()Lnet/minecraft/entity/Entity;"
					//#else
					target = "Lnet/minecraft/server/MinecraftServer;isDedicated()Z"
					//#endif
			)
	)
	private void onDamageStarted(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) DamageSource source, @Local(argsOnly = true) float amount)
	{
		DamageLogger.create(this, source, amount);
	}

	//#if MC < 12104
	@Inject(
			//#disable-remap
			method = "damage",
			//#enable-remap
			slice = @Slice(
					from = @At(
							//#if MC >= 11900
							//$$ value = "FIELD",
							//$$ target = "Lnet/minecraft/registry/tag/DamageTypeTags;IS_FALL:Lnet/minecraft/registry/tag/TagKey;"
							//#else
							value = "CONSTANT",
							args = "stringValue=fall"
							//#endif
					)
			),
			at = @At(value = "RETURN", ordinal = 0, shift = At.Shift.BEFORE) // before onDamageEnded in LivingEntityAndPlayerEntityMixins$DamageMixin
	)
	private void onRespawnProtectionCancelledDamage(CallbackInfoReturnable<Boolean> cir)
	{
		((DamageLoggerTarget)this).getDamageTracker().ifPresent(tracker -> tracker.modifyDamage(
				0.0F, ModifyReason.RESPAWN_PROTECTION
		));
	}
	//#endif

	@Inject(
			//#disable-remap
			method = "damage",
			//#enable-remap
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/server/network/ServerPlayerEntity;shouldDamagePlayer(Lnet/minecraft/entity/player/PlayerEntity;)Z",
							ordinal = 0
					)
			),
			at = {
					// before onDamageEnded in LivingEntityAndPlayerEntityMixins$DamageMixin
					@At(value = "RETURN", ordinal = 0, shift = At.Shift.BEFORE),
					@At(value = "RETURN", ordinal = 1, shift = At.Shift.BEFORE)
			}
	)
	private void onPVPDisabledCancelledDamage(CallbackInfoReturnable<Boolean> cir)
	{
		((DamageLoggerTarget)this).getDamageTracker().ifPresent(tracker -> tracker.modifyDamage(
				0.0F, ModifyReason.PVP_DISABLED
		));
	}
}

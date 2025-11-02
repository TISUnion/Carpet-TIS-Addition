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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 11900
//$$ import net.minecraft.world.entity.player.ProfilePublicKey;
//$$ import org.jetbrains.annotations.Nullable;
//#endif

//#if MC >= 11600
//$$ import net.minecraft.core.BlockPos;
//#endif

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends Player
{
	//#if MC >= 12106
	//$$ public ServerPlayerEntityMixin(Level world, GameProfile gameProfile)
	//$$ {
	//$$ 	super(world, gameProfile);
	//$$ }
	//#elseif MC >= 11903
	//$$ public ServerPlayerEntityMixin(Level world, BlockPos pos, float yaw, GameProfile gameProfile)
	//$$ {
	//$$ 	super(world, pos, yaw, gameProfile);
	//$$ }
	//#elseif MC >= 11900
	//$$ public ServerPlayerEntityMixin(Level world, BlockPos pos, float yaw, GameProfile profile, @Nullable ProfilePublicKey playerPublicKey)
	//$$ {
	//$$ 	super(world, pos, yaw, profile, playerPublicKey);
	//$$ }
	//#elseif MC >= 11600
	//$$ public ServerPlayerEntityMixin(Level world, BlockPos pos, float yaw, GameProfile profile)
	//$$ {
	//$$ 	super(world, pos, yaw, profile);
	//$$ }
	//#else
	public ServerPlayerEntityMixin(Level world, GameProfile profile)
	{
		super(world, profile);
	}
	//#endif

	// at the start of player damage calculation
	@Inject(
			//#disable-remap
			method = "hurt",
			//#enable-remap
			at = @At(
					value = "INVOKE",
					//#if MC >= 12104
					//$$ target = "Lnet/minecraft/world/damagesource/DamageSource;getEntity()Lnet/minecraft/world/entity/Entity;"
					//#else
					target = "Lnet/minecraft/server/MinecraftServer;isDedicatedServer()Z"
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
			method = "hurt",
			//#enable-remap
			slice = @Slice(
					from = @At(
							//#if MC >= 11900
							//$$ value = "FIELD",
							//$$ target = "Lnet/minecraft/tags/DamageTypeTags;IS_FALL:Lnet/minecraft/tags/TagKey;"
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
			method = "hurt",
			//#enable-remap
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/server/level/ServerPlayer;canHarmPlayer(Lnet/minecraft/world/entity/player/Player;)Z",
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

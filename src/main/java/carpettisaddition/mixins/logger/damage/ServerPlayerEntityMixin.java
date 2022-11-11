package carpettisaddition.mixins.logger.damage;

import carpettisaddition.logging.loggers.damage.DamageLogger;
import carpettisaddition.logging.loggers.damage.interfaces.DamageLoggerTarget;
import carpettisaddition.logging.loggers.damage.modifyreasons.ModifyReason;
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
	//#if MC >= 11903
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
			method = "damage",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/MinecraftServer;isDedicated()Z"
			)
	)
	private void onDamageStarted(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		DamageLogger.create(this, source, amount);
	}

	@Inject(
			method = "damage",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "stringValue=fall"
					)
			),
			at = @At(value = "RETURN", ordinal = 0, shift = At.Shift.BEFORE) // before onDamageEnded in LivingEntityAndPlayerEntityMixins$DamageMixin
	)
	private void onRespawnProtectionCancelledDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		((DamageLoggerTarget)this).getDamageTracker().ifPresent(tracker -> tracker.modifyDamage(
				0.0F, ModifyReason.RESPAWN_PROTECTION
		));
	}

	@Inject(
			method = "damage",
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
	private void onPVPDisabledCancelledDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		((DamageLoggerTarget)this).getDamageTracker().ifPresent(tracker -> tracker.modifyDamage(
				0.0F, ModifyReason.PVP_DISABLED
		));
	}
}

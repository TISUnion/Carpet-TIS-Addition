package carpettisaddition.mixins.logger.damage;

import carpettisaddition.logging.loggers.damage.DamageLogger;
import carpettisaddition.logging.loggers.damage.interfaces.ILivingEntity;
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

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity
{
	public ServerPlayerEntityMixin(World world, GameProfile profile)
	{
		super(world, profile);
	}

	// at the start of player damage calculation
	@Inject(
			method = "damage",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/MinecraftServer;isDedicated()Z"
			)
	)
	void onDamageStarted(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
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
	void onRespawnProtectionCancelledDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		((ILivingEntity)this).getDamageLogger().ifPresent(damageLogger -> damageLogger.modifyDamage(
				0.0F, ModifyReason.RESPAWN_PROTECTION
		));
	}

	@SuppressWarnings("MixinAnnotationTarget")
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
	void onPVPDisabledCancelledDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		((ILivingEntity)this).getDamageLogger().ifPresent(damageLogger -> damageLogger.modifyDamage(
				0.0F, ModifyReason.PVP_DISABLED
		));
	}
}

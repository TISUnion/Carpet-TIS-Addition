package carpettisaddition.mixins;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(ServerWorld.class)
public class ServerWorld_blockActionBroadcastRangeMixin
{
	// blockEventPacketRange
	@Redirect(method = "sendBlockActions", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;sendToAround(Lnet/minecraft/entity/player/PlayerEntity;DDDDLnet/minecraft/world/dimension/DimensionType;Lnet/minecraft/network/Packet;)V"))
	private void sendToAroundWithinRange(PlayerManager getPlayerManager, /*Nullable*/ PlayerEntity player, double x, double y, double z, double d, DimensionType dimension, Packet<?> packet)
	{
		getPlayerManager.sendToAround(player, x, y, z, CarpetTISAdditionSettings.blockEventPacketRange, dimension, packet);
	}
}

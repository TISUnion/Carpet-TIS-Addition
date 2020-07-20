package carpettisaddition.mixins.option;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(ServerWorld.class)
public abstract class ServerWorld_blockEventPacketRangeMixin
{
	// blockEventPacketRange
	@Redirect(
			method = "processSyncedBlockEvents",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/PlayerManager;sendToAround(Lnet/minecraft/entity/player/PlayerEntity;DDDDLnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/network/Packet;)V"
			)
	)
	private void sendToAroundWithinRange(PlayerManager getPlayerManager, /*Nullable*/ PlayerEntity player, double x, double y, double z, double d, RegistryKey<World> dimension, Packet<?> packet)
	{
		getPlayerManager.sendToAround(player, x, y, z, CarpetTISAdditionSettings.blockEventPacketRange, dimension, packet);
	}
}

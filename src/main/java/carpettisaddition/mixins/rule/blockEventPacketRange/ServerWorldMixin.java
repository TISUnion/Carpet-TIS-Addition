package carpettisaddition.mixins.rule.blockEventPacketRange;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

// Inject with lower priority, so it's invoked after g4mespeed's @ModifyArg
@Mixin(value = ServerWorld.class, priority = 2000)
public abstract class ServerWorldMixin
{
	@ModifyArg(
			//#if MC >= 11600
			//$$ method = "processSyncedBlockEvents",
			//#else
			method = "sendBlockActions",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/server/PlayerManager;sendToAround(Lnet/minecraft/entity/player/PlayerEntity;DDDDLnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/network/Packet;)V"
					//#else
					target = "Lnet/minecraft/server/PlayerManager;sendToAround(Lnet/minecraft/entity/player/PlayerEntity;DDDDLnet/minecraft/world/dimension/DimensionType;Lnet/minecraft/network/Packet;)V"
					//#endif
			),
			index = 4
	)
	private double modifyBlockEventPacketRange(double range)
	{
		// Modify only when the value of the rule has changed
		if (CarpetTISAdditionSettings.blockEventPacketRange != CarpetTISAdditionSettings.VANILLA_BLOCK_EVENT_PACKET_RANGE)
		{
			range = CarpetTISAdditionSettings.blockEventPacketRange;
		}
		return range;
	}
}

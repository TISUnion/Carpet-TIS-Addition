package carpettisaddition.mixins.rule.structureBlockLimit;

import carpet.CarpetSettings;
import net.minecraft.network.packet.c2s.play.UpdateStructureBlockC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

// Keep these for previous Carpet TIS Addition rule structureBlockLimit support
@Mixin(UpdateStructureBlockC2SPacket.class)
public abstract class UpdateStructureBlockC2SPacketMixin
{
	@ModifyConstant(
			method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V",
			require = 3,
			constant = @Constant(intValue = -48)
	)
	private int structureBlockLimitNegative(int value)
	{
		return -CarpetSettings.structureBlockLimit;
	}

	@ModifyConstant(
			method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V",
			require = 6,
			constant = @Constant(intValue = 48)
	)
	private int structureBlockLimitPositive(int value)
	{
		return CarpetSettings.structureBlockLimit;
	}
}

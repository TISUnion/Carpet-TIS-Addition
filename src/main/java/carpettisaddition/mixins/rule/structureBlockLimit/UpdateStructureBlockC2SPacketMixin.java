package carpettisaddition.mixins.rule.structureBlockLimit;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.network.packet.c2s.play.UpdateStructureBlockC2SPacket;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(UpdateStructureBlockC2SPacket.class)
public abstract class UpdateStructureBlockC2SPacketMixin
{
	@ModifyConstant(
			method = "read",
			require = 3,
			constant = @Constant(intValue = -32)
	)
	private int structureBlockLimitNegative(int value)
	{
		return -CarpetTISAdditionSettings.structureBlockLimit;
	}

	@ModifyConstant(
			method = "read",
			require = 6,
			constant = @Constant(intValue = 32)
	)
	private int structureBlockLimitPositive(int value)
	{
		return CarpetTISAdditionSettings.structureBlockLimit;
	}

	/*
	 * ----------------------------------
	 *   Fabric Carpet 1.4.25+ Protocol
	 * ----------------------------------
	 */

	@Shadow private BlockPos offset;

	@Shadow private BlockPos size;

	@Inject(method = "read", at = @At("TAIL"))
	private void structureBlockLimitsRead(PacketByteBuf buf, CallbackInfo ci)
	{
		if (buf.readableBytes() == 6 * 4)
		{
			this.offset = new BlockPos(
					MathHelper.clamp(buf.readInt(), -CarpetTISAdditionSettings.structureBlockLimit, CarpetTISAdditionSettings.structureBlockLimit),
					MathHelper.clamp(buf.readInt(), -CarpetTISAdditionSettings.structureBlockLimit, CarpetTISAdditionSettings.structureBlockLimit),
					MathHelper.clamp(buf.readInt(), -CarpetTISAdditionSettings.structureBlockLimit, CarpetTISAdditionSettings.structureBlockLimit)
			);
			this.size = new BlockPos(
					MathHelper.clamp(buf.readInt(), 0, CarpetTISAdditionSettings.structureBlockLimit),
					MathHelper.clamp(buf.readInt(), 0, CarpetTISAdditionSettings.structureBlockLimit),
					MathHelper.clamp(buf.readInt(), 0, CarpetTISAdditionSettings.structureBlockLimit)
			);
		}
	}

	@Inject(
			method = "write",
			at = @At("TAIL")
	)
	private void structureBlockLimitsWrite(PacketByteBuf buf, CallbackInfo ci)
	{
		// client method, only applicable if with carpet is on the server, or running locally
		if (CarpetTISAdditionSettings.structureBlockLimit >= 128)
		{
			buf.writeInt(this.offset.getX());
			buf.writeInt(this.offset.getY());
			buf.writeInt(this.offset.getZ());
			buf.writeInt(this.size.getX());
			buf.writeInt(this.size.getY());
			buf.writeInt(this.size.getZ());
		}
	}
}

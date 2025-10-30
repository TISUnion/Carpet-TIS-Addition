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

package carpettisaddition.mixins.rule.structureBlockLimit;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.16"))
@Mixin(ServerboundSetStructureBlockPacket.class)
public abstract class UpdateStructureBlockC2SPacketMixin
{
	@ModifyExpressionValue(
			method = "read",
			require = 3,
			at = @At(
					value = "CONSTANT",
					args = "intValue=-32"
			)
	)
	private int structureBlockLimitNegative(int value)
	{
		return -CarpetTISAdditionSettings.structureBlockLimit;
	}

	@ModifyExpressionValue(
			method = "read",
			require = 6,
			at = @At(
					value = "CONSTANT",
					args = "intValue=32"
			)
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
	private void structureBlockLimitsRead(FriendlyByteBuf buf, CallbackInfo ci)
	{
		if (buf.readableBytes() == 6 * 4)
		{
			this.offset = new BlockPos(
					Mth.clamp(buf.readInt(), -CarpetTISAdditionSettings.structureBlockLimit, CarpetTISAdditionSettings.structureBlockLimit),
					Mth.clamp(buf.readInt(), -CarpetTISAdditionSettings.structureBlockLimit, CarpetTISAdditionSettings.structureBlockLimit),
					Mth.clamp(buf.readInt(), -CarpetTISAdditionSettings.structureBlockLimit, CarpetTISAdditionSettings.structureBlockLimit)
			);
			this.size = new BlockPos(
					Mth.clamp(buf.readInt(), 0, CarpetTISAdditionSettings.structureBlockLimit),
					Mth.clamp(buf.readInt(), 0, CarpetTISAdditionSettings.structureBlockLimit),
					Mth.clamp(buf.readInt(), 0, CarpetTISAdditionSettings.structureBlockLimit)
			);
		}
	}

	@Inject(
			method = "write",
			at = @At("TAIL")
	)
	private void structureBlockLimitsWrite(FriendlyByteBuf buf, CallbackInfo ci)
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

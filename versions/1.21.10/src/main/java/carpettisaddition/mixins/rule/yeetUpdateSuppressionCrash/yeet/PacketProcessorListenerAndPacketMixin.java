/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.yeetUpdateSuppressionCrash.yeet;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.ExceptionCatchLocation;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionYeeter;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.network.PacketProcessor$ListenerAndPacket")
public abstract class PacketProcessorListenerAndPacketMixin
{
	/**
	 * In 23w35a (1.20.2 snapshot), mojang added an if-case to allow OutOfMemoryError breaking through
	 * the packet handing try-catch in {@link net.minecraft.network.protocol.PacketUtils#ensureRunningOnSameThread},
	 * but at that time, there's another try-catch at {@link net.minecraft.util.thread.BlockableEventLoop#doRunTask}
	 * that can still catch everything and log "Error executing task on Server"
	 * <p>
	 * In 25w36a (1.21.9 snapshot), mojang no longer uses MinecraftServer to schedule-and-execute off-thread packet handlers.
	 * Instead, a dedicated {@link net.minecraft.network.PacketProcessor} was introduced for packets' scheduling-and-execution.
	 * Now, there's no more outer try-catch to catch the breaking-through OutOfMemoryError in vanilla.
	 * That's not good. Lets yeet that!
	 */
	@ModifyExpressionValue(
			method = "handle",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/ReportedException;getCause()Ljava/lang/Throwable;"
			)
	)
	private static Throwable dontBreakThroughForSuppressionOOMError(Throwable reportedExceptionCause)
	{
		if (CarpetTISAdditionSettings.yeetUpdateSuppressionCrash && reportedExceptionCause instanceof OutOfMemoryError)
		{
			var use = UpdateSuppressionYeeter.extractInCauses(reportedExceptionCause);
			if (use.isPresent())
			{
				use.get().getSuppressionContext().report(ExceptionCatchLocation.PACKET_HANDLING);
				reportedExceptionCause = new Exception("not an OutOfMemoryError");
			}
		}
		return reportedExceptionCause;
	}
}

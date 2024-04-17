/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.chatMessageLengthLimitUnlocked;

import carpettisaddition.utils.compat.DummyClass;
import org.spongepowered.asm.mixin.Mixin;

/**
 * (mc1.20.5+) See classes
 * - {@link net.minecraft.network.packet.c2s.play.ChatCommandSignedC2SPacket}
 * - {@link net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket}
 *
 * Both of them now does not pass a length limit to PacketByteBuf's read/write string method,
 * so this mixin is useless now
 */
@Mixin(DummyClass.class)
public abstract class CommandExecutionC2SPacketMixin
{
}

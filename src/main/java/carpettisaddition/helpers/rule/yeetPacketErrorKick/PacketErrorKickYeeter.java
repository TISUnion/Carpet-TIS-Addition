/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.helpers.rule.yeetPacketErrorKick;

import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.deobfuscator.StackTracePrinter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class PacketErrorKickYeeter
{
	private static final Translator translator = new Translator("rule.yeetPacketErrorKick");

	public static void report(ServerPlayer player, String packetType, Exception exception)
	{
		String exceptionMessage = Objects.toString(exception.getMessage());
		exceptionMessage = exceptionMessage.substring(0, Math.min(exceptionMessage.length(), 256));
		BaseComponent details = Messenger.joinLines(
				translator.tr("exception_detail.kick_prevented", Messenger.s("yeetPacketErrorKick")),
				translator.tr("exception_detail.packet_type", Messenger.s(packetType)),
				translator.tr("exception_detail.exception_type", Messenger.s(exception.getClass().getSimpleName())),
				translator.tr("exception_detail.exception_message", Messenger.s(exceptionMessage))
		);
		BaseComponent message = Messenger.hover(
				Messenger.formatting(translator.tr("report_message"), ChatFormatting.RED, ChatFormatting.ITALIC),
				details
		);
		BaseComponent symbol = StackTracePrinter.create(exception).deobfuscate().toSymbolText();
		Messenger.tell(player, Messenger.c(message, Messenger.s(" "), symbol));
	}
}

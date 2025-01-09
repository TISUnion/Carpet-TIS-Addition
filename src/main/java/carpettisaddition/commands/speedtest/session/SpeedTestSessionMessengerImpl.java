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

package carpettisaddition.commands.speedtest.session;

import carpettisaddition.commands.speedtest.SpeedTestCommand;
import carpettisaddition.commands.speedtest.TestType;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.Formatting;

import java.util.List;

public class SpeedTestSessionMessengerImpl extends TranslationContext implements SpeedTestSessionMessenger
{
	private final TestType testType;
	private final ServerCommandSource source;

	public SpeedTestSessionMessengerImpl(TestType testType, ServerCommandSource source)
	{
		super(SpeedTestCommand.getInstance().getTranslator().getDerivedTranslator("messenger"));
		this.testType = testType;
		this.source = source;
	}

	private BaseText makeHeader()
	{
		Formatting color;
		switch (this.testType)
		{
			case DOWNLOAD:
				color = Formatting.AQUA;
				break;
			case UPLOAD:
				color = Formatting.LIGHT_PURPLE;
				break;
			case PING:
				color = Formatting.BLUE;
				break;
			default:
				throw new IllegalArgumentException("bad test type: " + this.testType);
		}
		return Messenger.hover(
				Messenger.c(
						Messenger.s("[", Formatting.GRAY),
						Messenger.formatting(this.testType.getNameText(), color),
						Messenger.s("]", Formatting.GRAY)
				),
				tr("header_hover", "/" + SpeedTestCommand.NAME)
		);
	}

	private BaseText makeCancelButton()
	{
		return Messenger.fancy(
				Messenger.s("[X]", Formatting.RED),
				tr("abort_hover"),
				Messenger.ClickEvents.runCommand(String.format("/%s abort", SpeedTestCommand.NAME))
		);
	}

	@Override
	public void sendMessage(BaseText message, boolean withCancelButton)
	{
		List<Object> parts = Lists.newArrayList();
		parts.add(this.makeHeader());
		parts.add(Messenger.s(" "));
		if (withCancelButton)
		{
			parts.add(this.makeCancelButton());
			parts.add(Messenger.s(" "));
		}
		parts.add(message);

		BaseText textToSend = Messenger.c(parts.toArray(new Object[0]));

		this.source.getMinecraftServer().execute(() -> Messenger.tell(this.source, textToSend));
	}
}

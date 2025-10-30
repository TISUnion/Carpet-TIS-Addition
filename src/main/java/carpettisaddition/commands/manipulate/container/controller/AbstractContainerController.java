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

package carpettisaddition.commands.manipulate.container.controller;

import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.manipulate.ManipulateCommand;
import carpettisaddition.commands.manipulate.container.ContainerManipulator;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.BaseComponent;

import static net.minecraft.commands.Commands.literal;

public abstract class AbstractContainerController extends TranslationContext
{
	protected static final Translator basicTranslator = ContainerManipulator.getInstance().getTranslator();
	protected final String commandPrefix;

	public AbstractContainerController(String translationName)
	{
		super(basicTranslator.getDerivedTranslator(translationName));
		this.commandPrefix = translationName.replace("_", "");
	}

	protected BaseComponent getName()
	{
		return tr("name");
	}

	public ArgumentBuilder<CommandSourceStack, ?> getCommandNode(CommandTreeContext context)
	{
		return literal(this.commandPrefix).executes(c -> this.showHelp(c.getSource()));
	}

	protected int showHelp(CommandSourceStack source)
	{
		Messenger.tell(source, basicTranslator.tr("help", this.getName()));
		return 1;
	}
}

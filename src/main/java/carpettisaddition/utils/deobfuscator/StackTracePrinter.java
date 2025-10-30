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

package carpettisaddition.utils.deobfuscator;

import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;

import java.util.regex.Pattern;

import static java.lang.Integer.min;

public class StackTracePrinter
{
	private static final int MAX_COPY_STACK_TRACE_SIZE = 64;
	private static final int MAX_HOVER_STACK_TRACE_SIZE = 16;
	private static final Translator translator = StackTraceDeobfuscator.translator;

	private StackTraceElement[] stackTrace;
	private String ignorePackagePath;

	private StackTracePrinter()
	{
		this.stackTrace = Thread.currentThread().getStackTrace();
	}

	public static StackTracePrinter create()
	{
		return new StackTracePrinter();
	}

	public static BaseComponent makeSymbol(Class<?> ignoreClass)
	{
		return create().ignore(ignoreClass).deobfuscate().toSymbolText();
	}

	// all StackTraceElements before any StackTraceElement with package starts with given path will be ignored
	public StackTracePrinter ignore(String ignorePackagePath)
	{
		this.ignorePackagePath = ignorePackagePath;
		return this;
	}

	public StackTracePrinter ignore(Class<?> ignoreClass)
	{
		return this.ignore(ignoreClass.getPackage().getName());
	}

	public StackTracePrinter deobfuscate()
	{
		this.stackTrace = StackTraceDeobfuscator.deobfuscateStackTrace(this.stackTrace, this.ignorePackagePath);
		return this;
	}

	private String createClipboardString()
	{
		int num = min(this.stackTrace.length, MAX_COPY_STACK_TRACE_SIZE);

		StringBuilder builder = new StringBuilder();
		builder.append(translator.tr("deobfuscated_stack_trace").getString());
		builder.append(String.format(" (%s)", StackTraceDeobfuscator.MAPPING_VERSION));

		for (int i = 0; i < num; i++)
		{
			builder.append("\n");
			builder.append(this.stackTrace[i].toString());
		}

		int restLineCount = this.stackTrace.length - num;
		if (restLineCount > 0)
		{
			builder.append("\n");
			builder.append(translator.tr("n_more_lines", restLineCount).getString());
		}

		return builder.toString();
	}

	private static final Pattern MIXIN_METHOD_NAME_UID_PATTERN = Pattern.compile("^[a-z]{3}[0-9a-f]{3}$");

	// https://github.com/search?q=repo%3ALlamaLad7%2FMixinExtras+getUniqueMethodName&type=code
	private static final Pattern MIXIN_EXTRAS_METHOD_PATTERN_1 = Pattern.compile("^mixinextras\\$bridge\\$.+");
	private static final Pattern MIXIN_EXTRAS_METHOD_PATTERN_2 = Pattern.compile(".+\\$mixinextras\\$wrapped$");

	private static boolean shouldObscureMethod(String methodName)
	{
		return MIXIN_EXTRAS_METHOD_PATTERN_1.matcher(methodName).matches() || MIXIN_EXTRAS_METHOD_PATTERN_2.matcher(methodName).matches();
	}

	private BaseComponent createHoverText()
	{
		BaseComponent text = translator.tr("deobfuscated_stack_trace_hover");
		int num = min(this.stackTrace.length, MAX_HOVER_STACK_TRACE_SIZE);

		for (int i = 0; i < num; i++)
		{
			StackTraceElement ste = this.stackTrace[i];
			BaseComponent line = Messenger.s("");

			String className = ste.getClassName();
			String[] classNameParts = className.split("\\.");
			if (classNameParts.length >= 1)
			{
				line.append(Messenger.s(classNameParts[classNameParts.length - 1], ChatFormatting.WHITE));
				line.append(Messenger.s(".", ChatFormatting.DARK_GRAY));
			}

			// Fabric mixin handler method name: "$".join([prefix, classUID + methodUID, modId, methodName])
			// The "modId" is introduced in fabric mixin 0.12.0
			// Example method names:
			// - handler$bah000$$modifiedRunLoop
			// - handler$bah000$carpet$modifiedRunLoop
			// - wrapOperation$dag000$yeetUpdateSuppressionCrash_implOnTickWorlds
			// - wrapOperation$dag000$carpet-tis-addition$yeetUpdateSuppressionCrash_implOnTickWorlds
			line.append(Util.make(() -> {
				String methodName = ste.getMethodName();
				if (shouldObscureMethod(methodName))
				{
					return Messenger.s(methodName, ChatFormatting.DARK_GRAY);
				}
				String[] methodNameParts = methodName.split("\\$");
				if (methodNameParts.length == 3 || methodNameParts.length == 4)
				{
					String prefix = methodNameParts[0];
					String uid = methodNameParts[1];
					String modId = methodNameParts.length == 4 ? methodNameParts[2] : "";
					String originName = methodNameParts[methodNameParts.length - 1];
					if (MIXIN_METHOD_NAME_UID_PATTERN.matcher(uid).matches())
					{
						return Messenger.join(
								Messenger.s("$", ChatFormatting.DARK_GRAY),
								Messenger.s(prefix, ChatFormatting.DARK_GRAY),
								Messenger.s(uid, ChatFormatting.DARK_GRAY),
								Messenger.s(modId, ChatFormatting.GOLD),
								Messenger.s(originName, ChatFormatting.YELLOW)
						);
					}
				}
				return Messenger.s(methodName, ChatFormatting.YELLOW);
			}));
			line.append(Messenger.s("()", ChatFormatting.GRAY));

			text.append(Messenger.newLine());
			text.append(line);
		}

		int restLineCount = this.stackTrace.length - num;
		if (restLineCount > 0)
		{
			text.append(Messenger.newLine());
			text.append(Messenger.formatting(translator.tr("n_more_lines", restLineCount), ChatFormatting.GRAY));
		}

		return text;
	}

	// a $ symbol with hover text showing the stack trace
	public BaseComponent toSymbolText()
	{
		return Messenger.fancy(
				"f",
				Messenger.s("$"),
				this.createHoverText(),

				// no COPY_TO_CLIPBOARD in 1.14
				//#if MC >= 11500
				Messenger.ClickEvents.copyToClipBoard(this.createClipboardString())
				//#else
				//$$ null
				//#endif
		);
	}
}

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
import com.google.common.base.Joiner;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;

import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.min;

public class StackTracePrinter
{
	private static final int DEFAULT_MAX_STACK_TRACE_SIZE = 64;
	private static final Translator translator = StackTraceDeobfuscator.translator;

	private StackTraceElement[] stackTrace;
	private int maxStackTraceSize;
	private String ignorePackagePath;

	private StackTracePrinter()
	{
		this.stackTrace = Thread.currentThread().getStackTrace();
		this.maxStackTraceSize = DEFAULT_MAX_STACK_TRACE_SIZE;
	}

	public static StackTracePrinter create()
	{
		return new StackTracePrinter();
	}

	public static BaseText makeSymbol(Class<?> ignoreClass)
	{
		return create().ignore(ignoreClass).deobfuscate().toSymbolText();
	}

	// limits the maximum display line
	public StackTracePrinter limit(int maxStackTraceSize)
	{
		this.maxStackTraceSize = maxStackTraceSize;
		return this;
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

	public StackTraceElement[] toStackTraceElements()
	{
		return this.stackTrace;
	}

	@SuppressWarnings("DeprecatedIsStillUsed")
	@Deprecated
	public BaseText toBaseText()
	{
		List<StackTraceElement> list = Arrays.asList(this.stackTrace).subList(0, min(this.stackTrace.length, this.maxStackTraceSize));
		int restLineCount = this.stackTrace.length - this.maxStackTraceSize;
		BaseText text = Messenger.c(translator.tr("deobfuscated_stack_trace"), Messenger.s(String.format(" (%s)\n", StackTraceDeobfuscator.MAPPING_VERSION)));
		text.append(Messenger.s(Joiner.on("\n").join(list)));
		if (restLineCount > 0)
		{
			text.append(Messenger.c("w \n", translator.tr("n_more_lines", restLineCount)));
		}
		return text;
	}

	// a $ symbol with hover text showing the stack trace
	public BaseText toSymbolText()
	{
		BaseText baseText = this.toBaseText();
		return Messenger.fancy(
				"f",
				Messenger.s("$"),
				baseText,

				// no COPY_TO_CLIPBOARD in 1.14
				//#if MC >= 11500
				new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, baseText.getString())
				//#else
				//$$ null
				//#endif
		);
	}
}

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

package carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpettisaddition.logging.loggers.microtiming.MicroTimingAccess;
import carpettisaddition.logging.loggers.microtiming.tickphase.TickPhase;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.base.Suppliers;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class UpdateSuppressionContext
{
	private static final Translator tr = new Translator("rule.yeetUpdateSuppressionCrash");
	private final Supplier<BaseText> textHolder;
	private final Throwable cause;

	public UpdateSuppressionContext(Throwable cause, @Nullable World world, BlockPos pos)
	{
		this.cause = cause;
		DimensionWrapper dimension = world != null ? DimensionWrapper.of(world) : null;
		TickPhase tickPhase = world != null ? MicroTimingAccess.getTickPhase(world) : MicroTimingAccess.getTickPhase();
		this.textHolder = Suppliers.memoize(() -> {
			String hover = cause.getClass().getSimpleName() + ": " + cause.getMessage();
			return Messenger.fancy(
					tr.tr("exception_detail",
							dimension != null ? Messenger.coord(pos, dimension) : Messenger.coord(pos),
							tickPhase.toText()
					),
					Messenger.s(hover),
					//#if MC >= 11500
					new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, hover)
					//#else
					//$$ null
					//#endif
			);
		});
	}

	public static void noop()
	{
	}

	public BaseText getMessageText()
	{
		return this.textHolder.get();
	}

	public String getMessage()
	{
		return this.getMessageText().getString();
	}

	public Throwable getCause()
	{
		return this.cause;
	}

	public void report()
	{
		BaseText message = Messenger.formatting(
				tr.tr("report_message", this.getMessageText()),
				Formatting.RED, Formatting.ITALIC
		);

		// fabric carpet 1.4.49 introduces rule updateSuppressionCrashFix with related logger
		// we reuse the logger for message subscribing
		Logger logger = LoggerRegistry.getLogger("updateSuppressedCrashes");
		if (logger != null)
		{
			logger.log(() -> new BaseText[]{message});
			Messenger.sendToConsole(message);
		}
		else
		{
			Messenger.broadcast(message);
		}
	}
}

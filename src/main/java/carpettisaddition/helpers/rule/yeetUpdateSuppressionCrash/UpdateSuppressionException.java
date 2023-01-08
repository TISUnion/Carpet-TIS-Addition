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
import carpettisaddition.utils.ModIds;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.base.Suppliers;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.17"))
public class UpdateSuppressionException extends RuntimeException
{
	private static final Translator tr = new Translator("rule.yeetUpdateSuppressionCrash");
	private final Supplier<BaseText> textHolder;

	public UpdateSuppressionException(World world, BlockPos pos)
	{
		DimensionWrapper dimension = DimensionWrapper.of(world);
		TickPhase tickPhase = MicroTimingAccess.getTickPhase((ServerWorld)world);
		this.textHolder = Suppliers.memoize(() ->  tr.tr("exception_detail",
				Messenger.coord(pos, dimension),
				tickPhase.toText()
		));
	}

	public BaseText getMessageText()
	{
		return this.textHolder.get();
	}

	@Override
	public String getMessage()
	{
		return this.getMessageText().getString();
	}

	@Override
	public String toString()
	{
		return this.getMessage();
	}

	public static void noop()
	{
		// load this class in advanced
		// to prevent NoClassDefFoundError due to stack overflow again when loading this class
	}

	public static void report(UpdateSuppressionException exception)
	{
		BaseText message = Messenger.formatting(
				tr.tr("report_message", exception.getMessageText()),
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
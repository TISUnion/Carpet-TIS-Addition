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

package carpettisaddition.logging.loggers.scounter;

import carpettisaddition.commands.scounter.SupplierCounterCommand;
import carpettisaddition.logging.loggers.AbstractHUDLogger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.BaseText;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SupplierCounterHUDLogger extends AbstractHUDLogger
{
	private static final SupplierCounterHUDLogger INSTANCE = new SupplierCounterHUDLogger();
	public static final String NAME = "scounter";

	public SupplierCounterHUDLogger()
	{
		super(NAME, false);
	}

	public static SupplierCounterHUDLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	public @Nullable String getDefaultLoggingOption()
	{
		return "";
	}

	@Override
	public @Nullable String[] getSuggestedLoggingOption()
	{
		return SupplierCounterCommand.COLORS.toArray(new String[0]);
	}

	@Override
	public BaseText[] onHudUpdate(String option, PlayerEntity playerEntity)
	{
		List<BaseText> lines = new ArrayList<>();
		Arrays.asList(option.split(",")).forEach(color -> {
			SupplierCounterCommand.getInstance().getCounter(color).ifPresent(counter -> {
				lines.add(counter.reportBrief(false));
			});
		});
		return lines.toArray(new BaseText[0]);
	}
}

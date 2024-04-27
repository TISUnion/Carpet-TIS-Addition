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

package carpettisaddition.logging.compat;

import carpet.logging.HUDLogger;

import java.lang.reflect.Field;

/**
 * Used in mc 1.14.4 where carpet doesn't provide logging support for carpet extensions
 */
public class ExtensionHUDLogger extends HUDLogger implements IExtensionLogger
{
	private final Field acceleratorField;

	public ExtensionHUDLogger(Field acceleratorField, String logName, String def, String[] options)
	{
		super(
				//#if MC >= 11500
				acceleratorField,
				//#endif

				logName, def, options

				//#if MC >= 11700
				, false
				//#endif
		);
		this.acceleratorField = acceleratorField;
	}

	@Override
	public Field getAcceleratorField()
	{
		return acceleratorField;
	}
}
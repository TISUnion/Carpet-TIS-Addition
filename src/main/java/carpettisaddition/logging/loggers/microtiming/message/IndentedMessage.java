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

package carpettisaddition.logging.loggers.microtiming.message;

import net.minecraft.text.BaseText;

public class IndentedMessage
{
	private final MicroTimingMessage message;
	private final int indentation;

	public IndentedMessage(MicroTimingMessage message, int indentation)
	{
		this.message = message;
		this.indentation = indentation;
	}

	public MicroTimingMessage getMessage()
	{
		return this.message;
	}

	public int getIndentation()
	{
		return indentation;
	}

	public BaseText toText()
	{
		return this.message.toText(this.indentation, this.indentation == 0);
	}
}

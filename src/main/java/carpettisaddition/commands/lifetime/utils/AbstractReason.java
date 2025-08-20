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

package carpettisaddition.commands.lifetime.utils;

import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.translations.TranslationContext;
import com.google.gson.JsonObject;
import net.minecraft.text.BaseText;

public abstract class AbstractReason extends TranslationContext
{
	public AbstractReason(String reasonType)
	{
		super(LifeTimeTracker.getInstance().getTranslator().getDerivedTranslator(reasonType));
	}

	public abstract BaseText toText();

	public abstract String getRecordId();
	public abstract JsonObject getRecordData();
}

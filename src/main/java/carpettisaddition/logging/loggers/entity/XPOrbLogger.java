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

package carpettisaddition.logging.loggers.entity;

import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.utils.Messenger;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.network.chat.BaseComponent;

//#if MC >= 11700
//$$ import carpettisaddition.utils.EntityUtils;
//#endif

public class XPOrbLogger extends EntityLogger<ExperienceOrb>
{
	private static final XPOrbLogger INSTANCE = new XPOrbLogger();

	public XPOrbLogger()
	{
		super("xporb");
	}

	public static XPOrbLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	protected BaseComponent getNameTextHoverText(ExperienceOrb xp)
	{
		//#if MC >= 11700
		//$$ int amount = xp.getExperienceAmount();
		//$$ int count = EntityUtils.getXpOrbPickingCount(xp);
		//$$ long total = (long)amount * count;
		//$$ String amountStr = String.format("w : %dxp * %d = %d", amount, count, total);
		//#else
		String amountStr = String.format("w : %d", xp.getValue());
		//#endif

		return Messenger.c(tr("xp_amount"), amountStr);
	}

	@Override
	protected boolean getAcceleratorBoolean()
	{
		return TISAdditionLoggerRegistry.__xporb;
	}
}

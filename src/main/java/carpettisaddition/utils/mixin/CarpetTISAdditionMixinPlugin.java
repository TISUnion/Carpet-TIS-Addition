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

package carpettisaddition.utils.mixin;

import me.fallenbreath.conditionalmixin.api.mixin.RestrictiveMixinConfigPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;

public class CarpetTISAdditionMixinPlugin extends RestrictiveMixinConfigPlugin
{
	private final Logger LOGGER = LogManager.getLogger();

	@Override
	protected void onRestrictionCheckFailed(String mixinClassName, String reason)
	{
		LOGGER.debug("[TISCM] Disabled mixin {} due to {}", mixinClassName, reason);
	}

	@Override
	public String getRefMapperConfig()
	{
		return null;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets)
	{
	}

	@Override
	public List<String> getMixins()
	{
		return null;
	}
}

/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package carpettisaddition.commands.lifetime.removal;

import carpettisaddition.utils.Messenger;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.BaseText;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class DespawnImmediatelyRemovalReason extends RemovalReason
{
	@Nullable
	private final String damageSourceName;

	public DespawnImmediatelyRemovalReason(@Nullable DamageSource damageSource)
	{
		this.damageSourceName = damageSource != null ? damageSource.getName() : null;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null || getClass() != o.getClass()) return false;
		DespawnImmediatelyRemovalReason that = (DespawnImmediatelyRemovalReason)o;
		return Objects.equals(damageSourceName, that.damageSourceName);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(damageSourceName);
	}

	@Override
	public BaseText toText()
	{
		BaseText text = tr("despawn.immediately");
		if (this.damageSourceName == null)
		{
			return text;
		}

		return Messenger.c(
				text,
				"g  (",
				Messenger.fancy(
						null,
						Messenger.s(this.damageSourceName),
						tr("death.damage_source"),
						null
				),
				"g )"
		);
	}
}

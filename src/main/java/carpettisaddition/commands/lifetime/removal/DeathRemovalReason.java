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

package carpettisaddition.commands.lifetime.removal;

import carpettisaddition.utils.Messenger;
import com.google.gson.JsonObject;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.BaseText;

import java.util.Objects;

public class DeathRemovalReason extends RemovalReason
{
	private final String damageSourceName;

	public DeathRemovalReason(DamageSource damageSource)
	{
		this.damageSourceName = damageSource.getName();
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof DeathRemovalReason)) return false;
		DeathRemovalReason that = (DeathRemovalReason) o;
		return Objects.equals(this.damageSourceName, that.damageSourceName);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.damageSourceName);
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				tr("death"),
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

	@Override
	public String getRecordId()
	{
		return "death";
	}

	@Override
	public JsonObject getRecordData()
	{
		JsonObject data = new JsonObject();
		data.addProperty("damageSource", this.damageSourceName);
		return data;
	}
}

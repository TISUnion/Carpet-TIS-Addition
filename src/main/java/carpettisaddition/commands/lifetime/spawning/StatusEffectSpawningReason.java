/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.commands.lifetime.spawning;

import com.google.gson.JsonObject;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

import java.util.Objects;
import java.util.Optional;

public class StatusEffectSpawningReason extends SpawningReason
{
	private final MobEffect effect;

	public StatusEffectSpawningReason(MobEffect effect)
	{
		this.effect = effect;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StatusEffectSpawningReason that = (StatusEffectSpawningReason)o;
		return Objects.equals(effect, that.effect);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(effect);
	}

	@Override
	public BaseComponent toText()
	{
		return tr(
				"status_effect",
				//#if MC >= 11500
				this.effect.getDisplayName()
				//#else
				//$$ this.effect.method_5560()
				//#endif
		);
	}

	@Override
	public String getRecordId()
	{
		return "status_effect";
	}

	@Override
	public JsonObject getRecordData()
	{
		JsonObject data = new JsonObject();
		String s = Optional.ofNullable(Registry.MOB_EFFECT.getKey(this.effect)).map(ResourceLocation::toString).orElse(null);
		data.addProperty("effect", s);
		return data;
	}
}

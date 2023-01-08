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

package carpettisaddition.logging.loggers.damage;

import carpettisaddition.utils.entityfilter.EntityFilter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class OptionParser
{
	private final Target fromTarget;
	private final Target toTarget;
	private final boolean matchesAny;
	private final boolean biDirection;

	/**
	 * Regular options:
	 * "->me": damages dealt to me
	 * "->creeper": damages dealt to creeper entities
	 * "vex->": damages dealt from vex
	 * "zombie": damages from / to zombie
	 * "me->zombie": damages from me to zombie
	 * "me<->zombie": damages between me and zombie
	 *
	 * Entity selector options:
	 * "->@e[distance=..20]": this works too, but requires permission level 2 like vanilla
	 * "Steve": works if Steve is online
	 * "some-uuid-string": just like a entity selector in command
	 */
	public OptionParser(String option)
	{
		String[] parts;
		parts = option.split("<->", -1);
		if (parts.length > 1)  // a<->b, a<->, <->a
		{
			this.fromTarget = Target.of(parts[0]);
			this.toTarget = Target.of(parts[1]);
			this.matchesAny = false;
			this.biDirection = true;
			return;
		}

		this.biDirection = false;
		parts = option.split("->", -1);
		if (parts.length > 1)  // a->b, ->a, a->
		{
			this.fromTarget = Target.of(parts[0]);
			this.toTarget = Target.of(parts[1]);
			this.matchesAny = false;
		}
		else  // a
		{
			this.fromTarget = this.toTarget = Target.of(option);
			this.matchesAny = true;
		}
	}

	public boolean accepts(PlayerEntity player, @Nullable Entity from, @Nullable Entity to)
	{
		if (this.biDirection)
		{
			return
					(this.fromTarget.matches(player, from) && this.toTarget.matches(player, to)) ||
					(this.fromTarget.matches(player, to) && this.toTarget.matches(player, from));
		}
		else
		{
			boolean fromMatches = this.fromTarget.matches(player, from);
			boolean toMatches = this.toTarget.matches(player, to);
			return this.matchesAny ? (fromMatches || toMatches) : (fromMatches && toMatches);
		}
	}

	@FunctionalInterface
	private interface Target
	{
		Target WILDCARD = (player, entity) -> true;

		static Target of(String option)
		{
			return option.isEmpty() || "*".equals(option) ? WILDCARD : new StringTarget(option);
		}

		boolean matches(PlayerEntity player, @Nullable Entity entity);
	}

	private static class StringTarget implements Target
	{
		private final String targetString;
		@Nullable
		private final EntityType<?> entityType;

		private StringTarget(String targetString)
		{
			this.targetString = targetString;

			EntityType<?> entityType;
			try
			{
				entityType = Registry.ENTITY_TYPE.getOrEmpty(new Identifier(this.targetString)).orElse(null);
			}
			catch (InvalidIdentifierException e)
			{
				entityType = null;
			}
			this.entityType = entityType;
		}

		@Override
		public boolean matches(PlayerEntity player, @Nullable Entity entity)
		{
			switch (this.targetString)
			{
				case "all":
					return true;
				case "me":
					return entity == player;
				case "players":
					return entity instanceof PlayerEntity;
				default:
					if (this.entityType != null)
					{
						return entity != null && entity.getType() == this.entityType;
					}
					return EntityFilter.createOptional(player, this.targetString).
							map(filter -> filter.test(entity)).
							orElse(false);
			}
		}
	}
}

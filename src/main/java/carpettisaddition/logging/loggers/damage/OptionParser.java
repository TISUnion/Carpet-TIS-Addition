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

import carpettisaddition.utils.IdentifierUtils;
import carpettisaddition.utils.entityfilter.EntityFilter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class OptionParser
{
	private final Target fromTarget;
	private final Target toTarget;
	private final boolean matchesAny;
	private final boolean biDirection;

	/**
	 * Regular options:
	 *   "->me":
	 *     damages dealt to me
	 *     target is me
	 *   "->creeper":
	 *     damages dealt to creeper entities
	 *     target is creeper
	 *   "vex->":
	 *     damages dealt from vex
	 *     source is vex
	 *   "zombie":
	 *     damages from / to zombie
	 *     source is zombie, or target is zombie
	 *   "me->zombie":
	 *     damages from me to zombie
	 *     source is me, target is zombie
	 *   "me<->zombie":
	 *     damages between me and zombie
	 *     source is me, target is zombie, or source is zombie, target is me
	 * Source / Target syntax (try parsing from top to down):
	 *   Hardcoded:
	 *     "", "*", "all": Matches all
	 *     "me": Matches the subscriber itself
	 *     "players: Matches player
	 *   Entity type (matches given type of entities):
	 *     "cat": Matches cat. Same as below
	 *     "minecraft:cat"
	 *     "entity_type/cat"
	 *   Damage name:
	 *     "hotFloor": Matches if damage msg ID is hotFloor (i.e. damage type "minecraft:hot_floor")
	 *     "damage_name/hotFloor"
	 *   Damage type (available in mc1.19.4+, matches source only):
	 *     "hot_floor": Matches if damage type is "minecraft:hot_floor"
	 *     "minecraft:hot_floor"
	 *     "damage_type/hot_floor"
	 *   Entity selector:
	 *     "@e[distance=..20]": this works too, but requires permission level 2 like vanilla
	 *     "Steve": works if player Steve is online
	 *     "some-uuid-string": just like an entity selector in command
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

	public boolean accepts(Player player, DamageContext ctx)
	{
		if (this.biDirection)
		{
			return
					(this.fromTarget.matchFrom(player, ctx.source, ctx.from) && this.toTarget.matchTo(player, ctx.source, ctx.to)) ||
					(this.fromTarget.matchTo(player, ctx.source, ctx.to) && this.toTarget.matchFrom(player, ctx.source, ctx.from));
		}
		else
		{
			boolean fromMatches = this.fromTarget.matchFrom(player, ctx.source, ctx.from);
			boolean toMatches = this.toTarget.matchTo(player, ctx.source, ctx.to);
			return this.matchesAny ? (fromMatches || toMatches) : (fromMatches && toMatches);
		}
	}

	private interface Target
	{
		boolean matchFrom(Player player, DamageSource source, @Nullable Entity from);

		boolean matchTo(Player player, DamageSource source, @Nullable Entity to);

		static Target of(String option)
		{
			switch (option)
			{
				case "":
				case "*":
				case "all":
					return (SimpleTarget)(player, entity) -> true;
				case "me":
					return (SimpleTarget)(player, entity) -> entity == player;
				case "players":
					return (SimpleTarget)(player, entity) -> entity instanceof Player;
			}

			// <catalogue>/<identifier>
			// <catalogue>/<identifier>
			// entity_type/minecraft:cat
			// damage_type/minecraft:out_of_world
			// damage_name/xxx

			ResourceLocation catalogue;
			ResourceLocation identifier;
			String namePart;
			String[] parts = option.split("/", -1);
			if (parts.length == 1)
			{
				catalogue = null;
				identifier = ResourceLocation.tryParse(parts[0]);
				namePart = parts[0];
			}
			else if (parts.length == 2)
			{
				catalogue = ResourceLocation.tryParse(parts[0]);
				identifier = ResourceLocation.tryParse(parts[1]);
				namePart = parts[1];
			}
			else
			{
				catalogue = identifier = null;
				namePart = option;
			}

			if ((catalogue == null || catalogue.equals(IdentifierUtils.ofVanilla("entity_type"))) && identifier != null)
			{
				EntityType<?> entityType = Registry.ENTITY_TYPE.getOptional(identifier).orElse(null);
				if (entityType != null)
				{
					return (SimpleTarget)(player, entity) -> entity != null && entity.getType() == entityType;
				}
			}

			return new Target()
			{
				private final SimpleTarget entityFilterTarget = (player, entity) ->
						EntityFilter.createOptional(player, option).
								map(filter -> filter.test(entity)).
								orElse(false);

				@Override
				public boolean matchFrom(Player player, DamageSource source, @Nullable Entity from)
				{
					if (catalogue == null || catalogue.equals(IdentifierUtils.ofVanilla("damage_name")))
					{
						if (Objects.equals(source.getMsgId(), namePart))
						{
							return true;
						}
					}
					//#if MC >= 11904
					//$$ if (catalogue == null || catalogue.equals(IdentifierUtils.ofVanilla("damage_type")))
					//$$ {
					//$$ 	if (identifier != null && Objects.equals(source.getTypeRegistryEntry().getKey().map(k -> k.getValue()).orElse(null), identifier))
					//$$ 	{
					//$$ 		return true;
					//$$ 	}
					//$$ }
					//#endif
					return this.entityFilterTarget.matchFrom(player, source, from);
				}

				@Override
				public boolean matchTo(Player player, DamageSource source, @Nullable Entity to)
				{
					return this.entityFilterTarget.matchTo(player, source, to);
				}
			};
		}
	}

	/**
	 * Does not care if it's matching from or to entity
	 */
	@FunctionalInterface
	private interface SimpleTarget extends Target
	{
		boolean matchEntity(Player player, @Nullable Entity entity);

		@Override
		default boolean matchFrom(Player player, DamageSource source, @Nullable Entity from)
		{
			return this.matchEntity(player, from);
		}

		@Override
		default boolean matchTo(Player player, DamageSource source, @Nullable Entity to)
		{
			return this.matchEntity(player, to);
		}
	}
}

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

	/**
	 * Regular options:
	 * "->me": damages dealt to me
	 * "->creeper": damages dealt to creeper entities
	 * "vex->": damages dealt from vex
	 * "zombie": damages from / to zombie
	 * "me->zombie": damages from me to zombie
	 *
	 * Entity selector options:
	 * "->@e[distance=..20]": this works too, but requires permission level 2 like vanilla
	 * "Steve": works if Steve is online
	 * "some-uuid-string": just like a entity selector in command
	 */
	public OptionParser(String option)
	{
		String[] parts = option.split("->", -1);
		if (parts.length > 1)
		{
			this.fromTarget = Target.of(parts[0]);
			this.toTarget = Target.of(parts[1]);
			this.matchesAny = false;
		}
		else
		{
			this.fromTarget = this.toTarget = Target.of(option);
			this.matchesAny = true;
		}
	}

	public boolean accepts(PlayerEntity player, Entity from, Entity to)
	{
		boolean fromMatches = this.fromTarget.matches(player, from);
		boolean toMatches = this.toTarget.matches(player, to);
		return this.matchesAny ? (fromMatches || toMatches) : (fromMatches && toMatches);
	}

	@FunctionalInterface
	private interface Target
	{
		Target WILDCARD = (player, entity) -> true;

		static Target of(String option)
		{
			return option.isEmpty() ? WILDCARD : new StringTarget(option);
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

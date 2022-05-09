package carpettisaddition.logging.loggers.damage;

import carpettisaddition.utils.StringUtil;
import carpettisaddition.utils.entityfilter.EntityFilter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class OptionParser
{
	private final boolean fromFlag;
	private final boolean toFlag;
	private final String entityString;
	@Nullable
	private final EntityType<?> entityType;

	/**
	 * Regular options:
	 * "->me": damages dealt to me
	 * "->creeper": damages dealt to creeper entities
	 * "vex->": damages dealt from vex
	 * "<-players": damages dealt players
	 * "zombie": damages from / to zombie
	 *
	 * Entity selector options:
	 * "->@e[distance=..20]": this works too, but requires permission level 2 like vanilla
	 * "Steve": works if Steve is online
	 * "some-uuid-string": just like a entity selector in command
	 */
	public OptionParser(String option)
	{
		boolean fromFlag = option.startsWith("<-") || option.endsWith("->");
		boolean toFlag = option.startsWith("->") || option.endsWith("<-");
		if (!fromFlag && !toFlag)
		{
			fromFlag = true;
			toFlag = true;
		}
		this.fromFlag = fromFlag;
		this.toFlag = toFlag;

		option = StringUtil.removePrefix(option, "->", "<-");
		option = StringUtil.removeSuffix(option, "->", "<-");
		this.entityString = option.toLowerCase();

		EntityType<?> entityType;
		try
		{
			entityType = Registry.ENTITY_TYPE.getOrEmpty(new Identifier(this.entityString)).orElse(null);
		}
		catch (InvalidIdentifierException e)
		{
			entityType = null;
		}
		this.entityType = entityType;
	}

	private boolean matches(PlayerEntity player, @Nullable Entity entity)
	{
		Optional<EntityFilter> optionalFilter = EntityFilter.createOptional(player, this.entityString);
		if (optionalFilter.isPresent())
		{
			return optionalFilter.get().test(entity);
		}
		switch (this.entityString)
		{
			case "all":
				return true;
			case "me":
				return entity == player;
			case "players":
				return entity instanceof PlayerEntity;
			default:
				return this.entityType != null && entity != null && entity.getType() == this.entityType;
		}
	}

	public boolean accepts(PlayerEntity player, Entity from, Entity to)
	{
		return (this.fromFlag && this.matches(player, from)) || (this.toFlag && this.matches(player, to));
	}
}

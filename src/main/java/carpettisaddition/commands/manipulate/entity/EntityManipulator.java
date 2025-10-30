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

package carpettisaddition.commands.manipulate.entity;

import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.manipulate.AbstractManipulator;
import carpettisaddition.mixins.command.manipulate.entity.MobEntityAccessor;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static com.mojang.brigadier.arguments.DoubleArgumentType.doubleArg;
import static com.mojang.brigadier.arguments.DoubleArgumentType.getDouble;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.minecraft.commands.arguments.ComponentArgument.getComponent;
import static net.minecraft.commands.arguments.ComponentArgument.textComponent;
import static net.minecraft.commands.arguments.EntityArgument.entities;
import static net.minecraft.commands.arguments.EntityArgument.entity;
import static net.minecraft.commands.arguments.EntityArgument.getEntities;
import static net.minecraft.commands.arguments.EntityArgument.getEntity;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class EntityManipulator extends AbstractManipulator
{
	public EntityManipulator()
	{
		super("entity");
	}

	@Override
	public void buildSubCommand(CommandTreeContext.Node context)
	{
		context.node.
				then(argument("target", entities()).
						then(literal("rename").
								then(literal("clear").
										executes(c -> clearRename(c.getSource(), getEntities(c, "target")))
								).
								then(argument("name", textComponent(
												//#if MC >= 12005
												//$$ context.commandBuildContext
												//#endif
										)).
										executes(c -> rename(c.getSource(), getEntities(c, "target"), getComponent(c, "name")))
								)
						).
						then(literal("persistent").
								executes(c -> queryPersistentState(c.getSource(), getEntities(c, "target"))).
								then(argument("state", bool()).
										executes(c -> setPersistent(c.getSource(), getEntities(c, "target"), getBool(c, "state")))
								)
						).
						then(literal("mount").
								then(argument("vehicle", entity()).
										executes(c -> mount(c.getSource(), getEntities(c, "target"), getEntity(c, "vehicle")))
								)
						).
						then(literal("dismount").
								executes(c -> dismount(c.getSource(), getEntities(c, "target")))
						).
						then(literal("velocity").
								executes(c -> queryVelocity(c.getSource(), getEntities(c, "target"))).
								then(literal("add").
										then(argument("x", doubleArg()).then(argument("y", doubleArg()).then(argument("z", doubleArg()).
												executes(
														c -> addVelocity(
																c.getSource(),
																getEntities(c, "target"),
																new Vec3(
																		getDouble(c, "x"),
																		getDouble(c, "y"),
																		getDouble(c, "z")
																))
												)
										)))
								).
								then(literal("set").
										then(argument("x", word()).then(argument("y", word()).then(argument("z", word()).
												executes(
														c -> setVelocity(
																c.getSource(),
																getEntities(c, "target"),
																new Vec3(
																		s2d(getString(c, "x")),
																		s2d(getString(c, "y")),
																		s2d(getString(c, "z"))
																))
												)
										)))
								)
						)
				);
	}

	private static double s2d(String s) throws CommandSyntaxException
	{
		switch (s.toLowerCase())
		{
			case "nan":
				s = "NaN";
				break;
			case "inf":
			case "infinity":
				s = "Infinity";
				break;
			case "-inf":
			case "-infinity":
				s = "-Infinity";
				break;
		}
		try
		{
			return Double.parseDouble(s);
		}
		catch (NumberFormatException e)
		{
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidDouble().create(s);
		}
	}

	@SuppressWarnings({"unchecked", "SameParameterValue"})
	private static <T> List<T> get(Collection<? extends Entity> entities, Class<T> clazz)
	{
		return entities.stream().
				map(e -> clazz.isAssignableFrom(e.getClass()) ? (T)e : null).
				filter(Objects::nonNull).
				collect(Collectors.toList());
	}

	private <T extends Entity> BaseComponent entitiesText(Collection<T> entities)
	{
		final int LIMIT = 15;
		BaseComponent hover = Messenger.join(
		Messenger.s("\n"),
		entities.stream().
				limit(LIMIT).
				map(e -> Messenger.format("%1$s (%2$s)", Messenger.entityType(e), e.getStringUUID())).
				toArray(BaseComponent[]::new)
		);
		if (entities.size() > LIMIT)
		{
			hover.append("\n...");
		}
		return Messenger.fancy(tr("entity_message", entities.size()), hover, null);
	}

	private int rename(CommandSourceStack source, Collection<? extends Entity> target, Component name)
	{
		target.forEach(e -> e.setCustomName(name));
		Messenger.tell(source, tr("rename.renamed", entitiesText(target), name));
		return target.size();
	}

	private int clearRename(CommandSourceStack source, Collection<? extends Entity> target)
	{
		target.forEach(e -> e.setCustomName(null));
		Messenger.tell(source, tr("rename.cleared", entitiesText(target)));
		return target.size();
	}

	private int queryPersistentState(CommandSourceStack source, Collection<? extends Entity> entities)
	{
		List<Mob> mobs = get(entities, Mob.class);
		if (mobs.isEmpty())
		{
			Messenger.tell(source, tr("persistent.not_found"));
		}
		else
		{
			Messenger.tell(source, tr("persistent.title"));
			mobs.forEach(e -> Messenger.tell(source, Messenger.format(
					"  %1$s: %2$s",
					Messenger.entity(e),
					Messenger.bool(e.isPersistenceRequired())
			)));
		}
		return mobs.size();
	}

	private int setPersistent(CommandSourceStack source, Collection<? extends Entity> entities, boolean state)
	{
		List<Mob> mobs = get(entities, Mob.class);
		mobs.forEach(e -> ((MobEntityAccessor)e).setPersistent$TISCM(state));
		Messenger.tell(source, tr("persistent.set", entitiesText(mobs), Messenger.bool(state)));
		return mobs.size();
	}

	private int mount(CommandSourceStack source, Collection<? extends Entity> target, Entity vehicle)
	{
		List<? extends Entity> passengers = target.stream().
				filter(e -> !e.equals(vehicle) && e.startRiding(
						vehicle, true
						//#if MC >= 1.21.9
						//$$ , true
						//#endif
				)).
				collect(Collectors.toList());
		Messenger.tell(source, tr("mounted", entitiesText(passengers), Messenger.entity(vehicle)));
		return passengers.size();
	}

	private int dismount(CommandSourceStack source, Collection<? extends Entity> passengers)
	{
		List<? extends Entity> filtered = passengers.stream().
				filter(Entity::isPassenger).
				collect(Collectors.toList());
		filtered.forEach(Entity::stopRiding);
		Messenger.tell(source, tr("dismounted", entitiesText(filtered)));
		return filtered.size();
	}

	private int queryVelocity(CommandSourceStack source, Collection<? extends Entity> target)
	{
		Messenger.tell(source, tr("velocity.title"));
		target.forEach(e -> {
			Messenger.tell(source, Messenger.format("  %1$s: %2$s", Messenger.entity(e), Messenger.vector(e.getDeltaMovement())));
		});
		return target.size();
	}

	private int addVelocity(CommandSourceStack source, Collection<? extends Entity> target, Vec3 delta)
	{
		target.forEach(e -> e.push(delta.x, delta.y, delta.z));
		Messenger.tell(source, tr("velocity.added", entitiesText(target), Messenger.vector(delta)));
		return target.size();
	}

	private int setVelocity(CommandSourceStack source, Collection<? extends Entity> target, Vec3 velocity)
	{
		target.forEach(e -> e.setDeltaMovement(velocity));
		Messenger.tell(source, tr("velocity.set", entitiesText(target), Messenger.vector(velocity)));
		return target.size();
	}
}

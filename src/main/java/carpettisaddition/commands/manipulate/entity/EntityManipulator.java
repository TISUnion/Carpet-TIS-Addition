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
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

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
import static net.minecraft.command.arguments.EntityArgumentType.*;
import static net.minecraft.command.arguments.TextArgumentType.getTextArgument;
import static net.minecraft.command.arguments.TextArgumentType.text;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

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
								then(argument("name", text(
												//#if MC >= 12005
												//$$ context.commandBuildContext
												//#endif
										)).
										executes(c -> rename(c.getSource(), getEntities(c, "target"), getTextArgument(c, "name")))
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
																new Vec3d(
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
																new Vec3d(
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

	private <T extends Entity> BaseText entitiesText(Collection<T> entities)
	{
		final int LIMIT = 15;
		BaseText hover = Messenger.join(
		Messenger.s("\n"),
		entities.stream().
				limit(LIMIT).
				map(e -> Messenger.format("%1$s (%2$s)", Messenger.entityType(e), e.getUuidAsString())).
				toArray(BaseText[]::new)
		);
		if (entities.size() > LIMIT)
		{
			hover.append("\n...");
		}
		return Messenger.fancy(tr("entity_message", entities.size()), hover, null);
	}

	private int rename(ServerCommandSource source, Collection<? extends Entity> target, Text name)
	{
		target.forEach(e -> e.setCustomName(name));
		Messenger.tell(source, tr("rename.renamed", entitiesText(target), name));
		return target.size();
	}

	private int clearRename(ServerCommandSource source, Collection<? extends Entity> target)
	{
		target.forEach(e -> e.setCustomName(null));
		Messenger.tell(source, tr("rename.cleared", entitiesText(target)));
		return target.size();
	}

	private int queryPersistentState(ServerCommandSource source, Collection<? extends Entity> entities)
	{
		List<MobEntity> mobs = get(entities, MobEntity.class);
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
					Messenger.bool(e.isPersistent())
			)));
		}
		return mobs.size();
	}

	private int setPersistent(ServerCommandSource source, Collection<? extends Entity> entities, boolean state)
	{
		List<MobEntity> mobs = get(entities, MobEntity.class);
		mobs.forEach(e -> ((MobEntityAccessor)e).setPersistent$TISCM(state));
		Messenger.tell(source, tr("persistent.set", entitiesText(mobs), Messenger.bool(state)));
		return mobs.size();
	}

	private int mount(ServerCommandSource source, Collection<? extends Entity> target, Entity vehicle)
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

	private int dismount(ServerCommandSource source, Collection<? extends Entity> passengers)
	{
		List<? extends Entity> filtered = passengers.stream().
				filter(Entity::hasVehicle).
				collect(Collectors.toList());
		filtered.forEach(Entity::stopRiding);
		Messenger.tell(source, tr("dismounted", entitiesText(filtered)));
		return filtered.size();
	}

	private int queryVelocity(ServerCommandSource source, Collection<? extends Entity> target)
	{
		Messenger.tell(source, tr("velocity.title"));
		target.forEach(e -> {
			Messenger.tell(source, Messenger.format("  %1$s: %2$s", Messenger.entity(e), Messenger.vector(e.getVelocity())));
		});
		return target.size();
	}

	private int addVelocity(ServerCommandSource source, Collection<? extends Entity> target, Vec3d delta)
	{
		target.forEach(e -> e.addVelocity(delta.x, delta.y, delta.z));
		Messenger.tell(source, tr("velocity.added", entitiesText(target), Messenger.vector(delta)));
		return target.size();
	}

	private int setVelocity(ServerCommandSource source, Collection<? extends Entity> target, Vec3d velocity)
	{
		target.forEach(e -> e.setVelocity(velocity));
		Messenger.tell(source, tr("velocity.set", entitiesText(target), Messenger.vector(velocity)));
		return target.size();
	}
}

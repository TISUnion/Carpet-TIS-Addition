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

package carpettisaddition.utils.command;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class BrigadierCommandBuilder<S> implements CommandBuilder<S>
{
	private final Map<String, Command<S>> commands = Maps.newHashMap();
	private final Map<String, BuilderDefinition<S, RequiredArgumentBuilder<S, ?>>> arguments = Maps.newHashMap();
	private final Map<String, BuilderDefinition<S, LiteralArgumentBuilder<S>>> literals = Maps.newHashMap();
	private List<ArgumentBuilder<S, ?>> buildCache = null;

	private static boolean isArg(String name)
	{
		return name.length() >= 3 && name.charAt(0) == '<' && name.charAt(name.length() - 1) == '>';
	}

	private static boolean isOptional(String name)
	{
		return name.length() >= 3 && name.charAt(0) == '[' && name.charAt(name.length() - 1) == ']';
	}

	private static String makeArg(String name)
	{
		return '<' + name + '>';
	}

	private static String stripArg(String name)
	{
		return isArg(name) ? name.substring(1, name.length() - 1) : name;
	}

	private static String stripOpt(String name)
	{
		return isOptional(name) ? name.substring(1, name.length() - 1) : name;
	}

	@Override
	public void command(String command, Command<S> callback)
	{
		boolean hasOpt = false;
		List<String> segments = Lists.newArrayList();
		Runnable addCommand = () -> this.commands.put(Joiner.on(" ").join(segments), callback);
		for (String s : command.split(" "))
		{
			if (s.isEmpty())
			{
				continue;
			}
			boolean isOpt = isOptional(s);
			if (hasOpt && !isOpt)
			{
				throw new IllegalArgumentException("Cannot have regular segment after optional segments: " + s);
			}
			if (isOpt)
			{
				hasOpt = true;
				if (segments.isEmpty())
				{
					throw new IllegalArgumentException("Cannot have optional segment at the beginning: " + s);
				}
				addCommand.run();
			}
			segments.add(stripOpt(s));
		}
		addCommand.run();
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public <V> BuilderDefinition<S, RequiredArgumentBuilder<S, V>> argument(String argument, BuilderFactory<RequiredArgumentBuilder<S, V>> builderFactory)
	{
		BuilderDefinition<S, RequiredArgumentBuilder<S, V>> definition = new BuilderDefinition<>(builderFactory);
		this.arguments.put(stripArg(argument), (BuilderDefinition)definition);
		this.cleanCache();
		return definition;
	}

	@Override
	public <V> BuilderDefinition<S, RequiredArgumentBuilder<S, V>> argument(String argument, ArgumentType<V> argumentType)
	{
		BuilderFactory<RequiredArgumentBuilder<S, V>> builderFactory = name -> RequiredArgumentBuilder.argument(name, argumentType);
		return this.argument(argument, builderFactory);
	}

	@Override
	public BuilderDefinition<S, LiteralArgumentBuilder<S>> literal(String argument)
	{
		BuilderDefinition<S, LiteralArgumentBuilder<S>> definition = new BuilderDefinition<>(LiteralArgumentBuilder::literal);
		this.literals.put(argument, definition);
		this.cleanCache();
		return definition;
	}

	private static class BuilderHolder<S>
	{
		public final ArgumentBuilder<S, ?> builder;
		public final String name;
		public final List<BuilderHolder<S>> children;

		private BuilderHolder(ArgumentBuilder<S, ?> builder, String name)
		{
			this.builder = builder;
			this.name = name;
			this.children = Lists.newArrayList();
		}
	}

	private BuilderHolder<S> locateOrCreateChild(BuilderHolder<S> parent, String nodeName, @Nullable Command<S> callback)
	{
		BuilderHolder<S> target = null;
		for (BuilderHolder<S> child : parent.children)
		{
			String childName = child.name;
			if (child.builder instanceof RequiredArgumentBuilder)
			{
				childName = makeArg(childName);
			}
			if (nodeName.equals(childName))
			{
				target = child;
				break;
			}
		}

		if (target == null)
		{
			boolean isArg = isArg(nodeName);
			nodeName = stripArg(nodeName);

			BuilderDefinition<S, ?> definition;
			if (isArg)
			{
				definition = this.arguments.get(nodeName);
				if (definition == null)
				{
					throw new IllegalArgumentException("Undefined arg " + nodeName);
				}
			}
			else
			{
				definition = this.literals.getOrDefault(nodeName, new BuilderDefinition<>(LiteralArgumentBuilder::literal));
			}

			target = new BuilderHolder<>(definition.createBuilder(nodeName), nodeName);
			parent.children.add(target);
		}

		if (callback != null)
		{
			target.builder.executes(callback);
		}
		return target;
	}

	private void finalizeBuilders(BuilderHolder<S> holder)
	{
		for (BuilderHolder<S> child : holder.children)
		{
			this.finalizeBuilders(child);
			holder.builder.then(child.builder);
		}
	}

	@Override
	public List<ArgumentBuilder<S, ?>> build()
	{
		if (this.buildCache == null)
		{
			BuilderHolder<S> root = new BuilderHolder<>(null, null);

			// create the builder tree
			this.commands.forEach((command, callback) -> {
				BuilderHolder<S> node = root;
				String[] segments = command.split(" ");
				for (int i = 0; i < segments.length; i++)
				{
					if (segments[i].length() > 0)
					{
						node = this.locateOrCreateChild(node, segments[i], i == segments.length - 1 ? callback : null);
					}
				}
			});

			// finalize the builders
			List<ArgumentBuilder<S, ?>> nodes = Lists.newArrayList();
			for (BuilderHolder<S> node : root.children)
			{
				this.finalizeBuilders(node);
				nodes.add(node.builder);
			}
			this.buildCache = nodes;
		}
		return this.buildCache;
	}

	@Override
	public void cleanCache()
	{
		this.buildCache = null;
	}

	@Override
	public void addChildrenFor(ArgumentBuilder<S, ?> parent)
	{
		this.build().forEach(parent::then);
	}
}

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

import com.google.common.collect.Lists;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.CommandNode;

import java.util.List;
import java.util.function.Predicate;

public class BuilderDefinition<S, T extends ArgumentBuilder<S, ?>>
{
	private final BuilderFactory<T> factory;
	private final List<BuilderProcessor<T>> processors = Lists.newArrayList();

	public BuilderDefinition(BuilderFactory<T> factory)
	{
		this.factory = factory;
	}

	ArgumentBuilder<S, ?> createBuilder(String nodeName)
	{
		T builder = this.factory.create(nodeName);
		for (BuilderProcessor<T> processor : this.processors)
		{
			processor.process(builder);
		}
		return builder;
	}

	public BuilderDefinition<S, T> postProcess(BuilderProcessor<T> postProcessor)
	{
		this.processors.add(postProcessor);
		return this;
	}

	// proxies

	public BuilderDefinition<S, T> then(final ArgumentBuilder<S, ?> argument)
	{
		return this.postProcess(node -> node.then(argument));
	}

	public BuilderDefinition<S, T> then(final CommandNode<S> argument)
	{
		return this.postProcess(node -> node.then(argument));
	}

	public BuilderDefinition<S, T> requires(final Predicate<S> requirement)
	{
		return this.postProcess(node -> node.requires(requirement));
	}

	@SuppressWarnings("unchecked")
	public BuilderDefinition<S, T> suggests(final SuggestionProvider<S> provider)
	{
		return this.postProcess(node -> {
			if (node instanceof RequiredArgumentBuilder)
			{
				((RequiredArgumentBuilder<S, T>)node).suggests(provider);
			}
			else
			{
				throw new IllegalArgumentException("cannot apply suggests on a " + node);
			}
		});
	}
}

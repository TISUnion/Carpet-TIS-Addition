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

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import java.util.List;

public interface CommandBuilder<S>
{
	//---------------------------- Interfaces ----------------------------

	void command(String command, Command<S> callback);

	<V> BuilderDefinition<S, RequiredArgumentBuilder<S, V>> argument(String argument, BuilderFactory<RequiredArgumentBuilder<S, V>> builderFactory);

	<V> BuilderDefinition<S, RequiredArgumentBuilder<S, V>> argument(String argument, ArgumentType<V> argumentType);

	BuilderDefinition<S, LiteralArgumentBuilder<S>> literal(String argument);

	//---------------------------- Outputs ----------------------------

	List<ArgumentBuilder<S, ?>> build();

	void cleanCache();

	void addChildrenFor(ArgumentBuilder<S, ?> parent);
}

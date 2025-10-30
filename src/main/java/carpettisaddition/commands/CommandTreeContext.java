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

package carpettisaddition.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

//#if MC >= 11900
//$$ import net.minecraft.command.CommandRegistryAccess;
//#endif

public abstract class CommandTreeContext
{
	//#if MC >= 11900
	//$$ public final CommandRegistryAccess commandBuildContext;
	//#endif

	/*
	 * ---------------------
	 *       Factories
	 * ---------------------
	 */

	protected CommandTreeContext(
			//#if MC >= 11900
			//$$ CommandRegistryAccess commandBuildContext
			//#endif
	)
	{
		//#if MC >= 11900
		//$$ this.commandBuildContext = commandBuildContext;
		//#endif
	}

	public static Register of(
			CommandDispatcher<CommandSourceStack> dispatcher
			//#if MC >= 11900
			//$$ , CommandRegistryAccess commandBuildContext
			//#endif
	)
	{
		return new Register(
				dispatcher
				//#if MC >= 11900
				//$$ , commandBuildContext
				//#endif
		);
	}

	public static Node of(
			ArgumentBuilder<CommandSourceStack, ?> node
			//#if MC >= 11900
			//$$ , CommandRegistryAccess commandBuildContext
			//#endif
	)
	{
		return new Node(
				node
				//#if MC >= 11900
				//$$ , commandBuildContext
				//#endif
		);
	}

	/**
	 * For mc1.19+
	 * Warning: {@link #commandBuildContext} will be null
	 * <p>
	 * TODO: make an interface with getCommandBuildContext() method.
	 * then make this method returns a impl that getCommandBuildContext() throws
	 */
	public static Node ofNonContext(ArgumentBuilder<CommandSourceStack, ?> node)
	{
		return of(
				node
				//#if MC >= 11900
				//$$ , null
				//#endif
		);
	}

	/*
	 * ---------------------
	 *      Convertors
	 * ---------------------
	 */

	/**
	 * Creates a {@link Node} context based on self's basic information
	 */
	public Node node(ArgumentBuilder<CommandSourceStack, ?> node)
	{
		return of(
				node
				//#if MC >= 11900
				//$$ , commandBuildContext
				//#endif
		);
	}

	public static class Register extends CommandTreeContext
	{
		public final CommandDispatcher<CommandSourceStack> dispatcher;

		private Register(
				CommandDispatcher<CommandSourceStack> dispatcher
				//#if MC >= 11900
				//$$ , CommandRegistryAccess commandBuildContext
				//#endif
		)
		{
			super(
					//#if MC >= 11900
					//$$ commandBuildContext
					//#endif
			);
			this.dispatcher = dispatcher;
		}
	}

	/*
	 * ---------------------
	 * Detailed sub-classes
	 * ---------------------
	 */

	public static class Node extends CommandTreeContext
	{
		public final ArgumentBuilder<CommandSourceStack, ?> node;

		private Node(
				ArgumentBuilder<CommandSourceStack, ?> node
				//#if MC >= 11900
				//$$ , CommandRegistryAccess commandBuildContext
				//#endif
		)
		{
			super(
					//#if MC >= 11900
					//$$ commandBuildContext
					//#endif
			);
			this.node = node;
		}
	}
}

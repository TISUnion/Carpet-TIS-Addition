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

package carpettisaddition.commands.manipulate.container;

import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.manipulate.AbstractManipulator;
import carpettisaddition.commands.manipulate.container.controller.*;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class ContainerManipulator extends AbstractManipulator
{
	private static final ContainerManipulator INSTANCE = new ContainerManipulator();
	private static final List<AbstractContainerController> COLLECTION_CONTROLLERS = ImmutableList.of(
			new EntityListController(),
			new TileEntityListController(),
			new TileTickQueueController(),
			new BlockEventQueueController()
	);

	private ContainerManipulator()
	{
		super("container");
	}

	public static ContainerManipulator getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void buildSubCommand(CommandTreeContext.Node context)
	{
		COLLECTION_CONTROLLERS.forEach(controller -> context.node.then(controller.getCommandNode(context)));
	}
}

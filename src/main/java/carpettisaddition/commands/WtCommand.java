/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

import carpet.script.utils.ShapeDispatcher;
import carpettisaddition.helpers.carpet.shape.ShapeHolder;
import carpettisaddition.helpers.carpet.shape.ShapeUtil;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.Lists;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.SpawnHelper;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Collections;
import java.util.List;

import static net.minecraft.server.command.CommandManager.literal;

public class WtCommand extends AbstractCommand
{
	private static final WtCommand INSTANCE = new WtCommand();
	private final List<ShapeHolder<? extends ShapeDispatcher.ExpiringShape>> shapes = Lists.newArrayList();

	public static WtCommand getInstance()
	{
		return INSTANCE;
	}
	
	public WtCommand()
	{
		super("wt");
	}

	@Override
	public void registerCommand(CommandTreeContext.Register context)
	{
		context.dispatcher.register(
				literal("wt").
				then(literal("draw").executes(this::wt)).
				then(literal("clear").executes(this::clear))
		);
	}

	private int clear(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException
	{
		ServerCommandSource source = ctx.getSource();
		ServerWorld world = source.getWorld();

		ShapeDispatcher.sendShape(world.getServer().getPlayerManager().getPlayerList(), this.shapes.stream().map(h -> h.toPair(false)).toList());
		this.shapes.clear();
		return 0;
	}

	private int wt(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException
	{
		ServerCommandSource source = ctx.getSource();
		ServerWorld world = source.getWorld();
		ServerPlayerEntity player = source.getPlayer();

		BlockPos pos1 = new BlockPos(-85, 66, 43);
		BlockPos pos2 = new BlockPos(-53, 80, 75);

		this.clear(ctx);
		MutableInt counter = new MutableInt(0);
		BlockPos.iterate(pos1, pos2).forEach(pos -> {
			boolean wiske = SpawnHelper.shouldUseNetherFortressSpawns(pos, source.getWorld(), SpawnGroup.MONSTER, world.getStructureAccessor());
			if (wiske)
			{
				this.shapes.add(ShapeUtil.createLabel(
						Messenger.s("x", Formatting.RED),
						Vec3d.of(pos).add(0.5, 0.5, 0.5),
						DimensionWrapper.of(world),
						null
				));
				counter.add(1);
			}
		});
		ShapeDispatcher.sendShape(Collections.singleton(player), this.shapes.stream().map(h -> h.toPair(true)).toList());
		Messenger.tell(source, Messenger.s("shouldUseNetherFortressSpawns hit " + counter.getValue()));
		return 0;
	}
}

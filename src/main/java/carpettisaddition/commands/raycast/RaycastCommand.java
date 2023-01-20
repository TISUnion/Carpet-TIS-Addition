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

package carpettisaddition.commands.raycast;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.utils.CarpetModUtil;
import carpettisaddition.utils.CommandUtil;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;

import java.util.function.Function;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.minecraft.command.arguments.BlockPosArgumentType.blockPos;
import static net.minecraft.command.arguments.BlockPosArgumentType.getLoadedBlockPos;
import static net.minecraft.command.arguments.Vec3ArgumentType.getVec3;
import static net.minecraft.command.arguments.Vec3ArgumentType.vec3;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RaycastCommand extends AbstractCommand
{
	private static final String NAME = "raycast";
	private static final RaycastCommand INSTANCE = new RaycastCommand();

	private RaycastCommand()
	{
		super(NAME);
	}

	public static RaycastCommand getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void registerCommand(CommandTreeContext.Register context)
	{
		context.dispatcher.register(
				literal(NAME).
				requires(s -> CarpetModUtil.canUseCommand(s, CarpetTISAdditionSettings.commandRaycast)).
				then(literal("emsim").
						requires(s -> CarpetTISAdditionSettings.ultraSecretSetting.equals("endermelon")).
						then(argument("pos", blockPos()).
								then(argument("max_r", integer()).
										executes(c -> endermenlonSimulate(
												c.getSource(),
												getLoadedBlockPos(c, "pos"),
												getInteger(c, "max_r")
										))
								)
						)
				).
				then(literal("block").
						then(argument("start", vec3()).
								then(argument("end", vec3()).
										executes(this::performRaycast).
										then(CommandUtil.enumArg("shapeMode", RayTraceContext.ShapeType.class).
												executes(this::performRaycast).
												then(CommandUtil.enumArg("fluidMode", RayTraceContext.FluidHandling.class).
														executes(this::performRaycast)
												)
										)
								)
						)
				)
		);
	}

	private int endermenlonSimulate(ServerCommandSource source, BlockPos pos, int maxR) throws CommandSyntaxException
	{
		new RaycastSimulator(source.getWorld(), source.getEntityOrThrow()).simulate(pos, maxR);
		return 0;
	}

	@SuppressWarnings("unchecked")
	private <T extends Enum<?>> T getEnumArg(CommandContext<?> context, String argName, T def) throws CommandSyntaxException
	{
		String enumString;
		try
		{
			enumString = getString(context, argName);
		}
		catch (IllegalArgumentException e)
		{
			return def;
		}
		try
		{
			return (T)Enum.valueOf(def.getClass(), enumString.toUpperCase());
		}
		catch (IllegalArgumentException e)
		{
			BaseText msg = Messenger.formatting(tr("unknown_enum." + argName, enumString), Formatting.RED);
			throw new SimpleCommandExceptionType(msg).create();
		}
	}

	private int performRaycast(CommandContext<ServerCommandSource> c) throws CommandSyntaxException
	{
		Vec3d start = getVec3(c, "start");
		Vec3d end = getVec3(c, "end");
		RayTraceContext.ShapeType shapeMode = getEnumArg(c, "shapeMode", RayTraceContext.ShapeType.COLLIDER);
		RayTraceContext.FluidHandling fluidMode = getEnumArg(c, "fluidMode", RayTraceContext.FluidHandling.NONE);
		ServerCommandSource source = c.getSource();
		ServerWorld world = source.getWorld();

		BlockHitResult result = world.rayTrace(new RayTraceContext(start, end, shapeMode, fluidMode, source.getEntityOrThrow()));
		if (result.getType() == HitResult.Type.MISS)
		{
			Messenger.tell(source, Messenger.formatting(tr("missed"), Formatting.DARK_RED));
			return 0;
		}
		else
		{
			BaseText coordText = Messenger.coord(result.getBlockPos(), DimensionWrapper.of(world));
			BaseText blockText = Messenger.fancy(
					Messenger.block(world.getBlockState(result.getBlockPos())),
					coordText,
					coordText.getStyle().getClickEvent()
			);
			Function<Vec3d, BaseText> c2t = coord -> Messenger.coord(coord, DimensionWrapper.of(world));
			Messenger.tell(source,Messenger.c(
					Messenger.fancy(
							"l",
							tr("hit"),
							Messenger.format("%s -> %s", c2t.apply(start), c2t.apply(end)),
							null
					),
					Messenger.s(" "),
					Messenger.formatting(blockText, Formatting.AQUA),
					"g  @ ",
					c2t.apply(result.getPos())
			));
			return 1;
		}
	}
}

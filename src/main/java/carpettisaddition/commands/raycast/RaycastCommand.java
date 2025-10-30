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
import carpettisaddition.utils.CommandUtils;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ClipContext;

import java.util.function.Function;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.minecraft.commands.arguments.coordinates.BlockPosArgument.blockPos;
import static net.minecraft.commands.arguments.coordinates.BlockPosArgument.getLoadedBlockPos;
import static net.minecraft.commands.arguments.coordinates.Vec3Argument.getVec3;
import static net.minecraft.commands.arguments.coordinates.Vec3Argument.vec3;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

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
										then(CommandUtils.enumArg("shapeMode", ClipContext.Block.class).
												executes(this::performRaycast).
												then(CommandUtils.enumArg("fluidMode", ClipContext.Fluid.class).
														executes(this::performRaycast)
												)
										)
								)
						)
				)
		);
	}

	private int endermenlonSimulate(CommandSourceStack source, BlockPos pos, int maxR) throws CommandSyntaxException
	{
		new RaycastSimulator(source.getLevel(), source.getEntityOrException()).simulate(pos, maxR);
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
			BaseComponent msg = Messenger.formatting(tr("unknown_enum." + argName, enumString), ChatFormatting.RED);
			throw new SimpleCommandExceptionType(msg).create();
		}
	}

	private int performRaycast(CommandContext<CommandSourceStack> c) throws CommandSyntaxException
	{
		Vec3 start = getVec3(c, "start");
		Vec3 end = getVec3(c, "end");
		ClipContext.Block shapeMode = getEnumArg(c, "shapeMode", ClipContext.Block.COLLIDER);
		ClipContext.Fluid fluidMode = getEnumArg(c, "fluidMode", ClipContext.Fluid.NONE);
		CommandSourceStack source = c.getSource();
		ServerLevel world = source.getLevel();

		BlockHitResult result = world.clip(new ClipContext(start, end, shapeMode, fluidMode, source.getEntityOrException()));
		if (result.getType() == HitResult.Type.MISS)
		{
			Messenger.tell(source, Messenger.formatting(tr("missed"), ChatFormatting.DARK_RED));
			return 0;
		}
		else
		{
			BaseComponent coordText = Messenger.coord(result.getBlockPos(), DimensionWrapper.of(world));
			BaseComponent blockText = Messenger.fancy(
					Messenger.block(world.getBlockState(result.getBlockPos())),
					coordText,
					coordText.getStyle().getClickEvent()
			);
			Function<Vec3, BaseComponent> c2t = coord -> Messenger.coord(coord, DimensionWrapper.of(world));
			Messenger.tell(source,Messenger.c(
					Messenger.fancy(
							"l",
							tr("hit"),
							Messenger.format("%s -> %s", c2t.apply(start), c2t.apply(end)),
							null
					),
					Messenger.s(" "),
					Messenger.formatting(blockText, ChatFormatting.AQUA),
					"g  @ ",
					c2t.apply(result.getLocation())
			));
			return 1;
		}
	}
}

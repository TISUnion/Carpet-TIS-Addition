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

package carpettisaddition.mixins.carpet.tweaks.command.spawnTrackingRestart;

import carpet.commands.SpawnCommand;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import static net.minecraft.commands.Commands.literal;

@Mixin(SpawnCommand.class)
public abstract class SpawnCommandMixin
{
	@Shadow(remap = false)
	private static int stopTracking(CommandSourceStack source)
	{
		return 0;
	}

	@Shadow(remap = false)
	//#if MC >= 12002
	//$$ private static int startTracking(CommandSourceStack source, net.minecraft.world.level.levelgen.structure.BoundingBox filter)
	//#else
	private static int startTracking(CommandSourceStack source, net.minecraft.core.BlockPos a, net.minecraft.core.BlockPos b)
	//#endif
	{
		return 0;
	}

	@Unique
	private static int startTrackingWithoutFilter(CommandSourceStack source)
	{
		//#if MC >= 12002
		//$$ return startTracking(source, null);
		//#else
		return startTracking(source, null, null);
		//#endif
	}

	/**
	 * Add literal("restart") as a child of node literal("tracking") by modifying argument in then(literal("tracking"))
	 */
	@Dynamic
	@ModifyArg(
			method = "register",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "stringValue=tracking",
							ordinal = 0
					),
					to = @At(
							value = "CONSTANT",
							args = "stringValue=test",
							ordinal = 0
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;then(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;",
					ordinal = 4
			),
			index = 0,
			remap = false
	)
	private static ArgumentBuilder<CommandSourceStack, ?> appendRestartArgumentOnSpawnTracking(ArgumentBuilder<CommandSourceStack, ?> builder)
	{
		builder.then(
				literal("restart").
						executes(c -> {
							int result = 0;
							result += stopTracking(c.getSource());
							result += startTrackingWithoutFilter(c.getSource());
							return result > 0 ? 1 : 0;
						})
		);
		return builder;
	}
}

/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.mixins.command.spawn.natualSpawning;

import carpet.commands.SpawnCommand;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.spawn.natualSpawning.SpawnNatualSpawningCommand;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//#if MC >= 1.19
//$$ import com.llamalad7.mixinextras.sugar.Local;
//$$ import net.minecraft.commands.CommandBuildContext;
//#endif

@Mixin(SpawnCommand.class)
public abstract class SpawnCommandMixin
{
	@ModifyArg(
			method = "register",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/CommandDispatcher;register(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;"
			),
			index = 0,
			remap = false
	)
	private static LiteralArgumentBuilder<CommandSourceStack> addMobcapsLocalCommand(
			LiteralArgumentBuilder<CommandSourceStack> rootNode
			//#if MC >= 1.19
			//$$ , @Local(argsOnly = true) CommandBuildContext commandBuildContext
			//#endif
	)
	{
		SpawnNatualSpawningCommand.getInstance().extendCommand(
				CommandTreeContext.of(
						rootNode
						//#if MC >= 1.19
						//$$ , commandBuildContext
						//#endif
				)
		);
		return rootNode;
	}
}

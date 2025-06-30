/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.opPlayerNoCheat;

import carpettisaddition.helpers.rule.opPlayerNoCheat.OpPlayerNoCheatHelper;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.SummonCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

/**
 * Stop pasting schematic in SMP
 * <p>
 * mc1.14 ~ mc1.21.5: subproject 1.15.2 (main project)
 * mc1.21.6+        : subproject 1.21.6        <--------
 */
@Mixin(SummonCommand.class)
public abstract class SummonCommandMixin
{
	@ModifyArg(
			method = "register",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;",
					remap = false
			),
			require = 1,
			allow = 1
	)
	private static Predicate<ServerCommandSource> checkIfAllowCheating_summonCommand(Predicate<ServerCommandSource> predicate)
	{
		return source -> predicate.test(source) && OpPlayerNoCheatHelper.canCheat(source);
	}
}

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

package carpettisaddition.mixins.command.tick.warp;

import carpet.commands.TickCommand;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.logging.loggers.tickwarp.TickWarpHUDLogger;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//#if 11900 <= MC && MC < 12003
//$$ import com.mojang.brigadier.CommandDispatcher;
//$$ import net.minecraft.commands.CommandBuildContext;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

@Mixin(TickCommand.class)
public abstract class TickCommandMixin
{
	//#if 11900 <= MC && MC < 12003
	//$$ private static CommandBuildContext currentCommandBuildContext$TISCM = null;
 //$$
	//$$ @Inject(method = "register", at = @At("HEAD"), remap = false)
	//$$ private static void storeCommandBuildContext(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, CallbackInfo ci)
	//$$ {
	//$$ 	currentCommandBuildContext$TISCM = commandBuildContext;
	//$$ }
	//#endif

	@ModifyArg(
			method = "register",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/CommandDispatcher;register(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;"
			),
			index = 0,
			remap = false
	)
	private static LiteralArgumentBuilder<CommandSourceStack> registerTickWarpInfo(LiteralArgumentBuilder<CommandSourceStack> builder)
	{
		TickWarpHUDLogger.getInstance().extendCommand(
				CommandTreeContext.of(
						builder
						//#if MC >= 11900
						//$$ , currentCommandBuildContext$TISCM
						//#endif
				)
		);
		return builder;
	}
}

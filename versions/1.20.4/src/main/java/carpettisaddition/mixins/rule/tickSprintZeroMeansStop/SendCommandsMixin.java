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

package carpettisaddition.mixins.rule.tickSprintZeroMeansStop;

import carpettisaddition.helpers.rule.tickCommandCarpetfied.TickCommandCarpetfiedRules;
import carpettisaddition.helpers.rule.tickSprintZeroMeansStop.TickSprintShortcuts;
import com.mojang.brigadier.tree.RootCommandNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//#if MC >= 26.4
//$$ import net.minecraft.commands.SuggestionBox;
//#else
import net.minecraft.commands.Commands;
//#endif

/**
 * mc >= 1.20.3: subproject 1.20.4                 <--------
 * mc <  1.20.3: subproject 1.15.2 (main project)
 */
//#if MC >= 26.4
//$$ @Mixin(SuggestionBox.class)
//#else
@Mixin(Commands.class)
//#endif
public abstract class SendCommandsMixin
{
	@ModifyArg(
			//#if MC >= 26.4
			//$$ method = "toPacket",
			//#else
			method = "sendCommands",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.21.6
					//$$ target = "Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket;<init>(Lcom/mojang/brigadier/tree/RootCommandNode;Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$NodeInspector;)V"
					//#else
					target = "Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket;<init>(Lcom/mojang/brigadier/tree/RootCommandNode;)V"
					//#endif
			),
			index = 0
	)
	private <S> RootCommandNode<S> tickSprintZeroMeansStop_addClientCommandShortcuts(RootCommandNode<S> rootNode)
	{
		if (TickCommandCarpetfiedRules.tickSprintZeroMeansStop())
		{
			TickSprintShortcuts.addShortcutsToClientTree(rootNode);
		}
		return rootNode;
	}
}

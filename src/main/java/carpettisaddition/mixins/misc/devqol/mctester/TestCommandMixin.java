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

package carpettisaddition.mixins.misc.devqol.mctester;

import carpettisaddition.utils.ModIds;
import carpettisaddition.utils.mixin.testers.DevelopmentEnvironmentTester;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.command.TestCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * MC Tester mod is bundled with lithium mod in development environment,
 * and it will enable the test command with {@link mctester.mixin.enable_testing.CommandManagerMixin}
 *<p>
 * That's stupid, and it causes player getting kicked on joining the game
 * with "java.lang.IllegalArgumentException: Unrecognized argument type net.minecraft.command.argument.TestClassArgumentType"
 * exception spamming in the console
 * <p>
 * Reason: {@link net.minecraft.command.arguments.ArgumentTypes#register} does not register those test argument types
 * used in the test command, resulting in a serializing failure when the server sends the command tree to the player
 */
@Restriction(require = {
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.15"),
		@Condition(ModIds.mc_tester),
		@Condition(type = Condition.Type.TESTER, tester = DevelopmentEnvironmentTester.class),
})
@Mixin(TestCommand.class)
public abstract class TestCommandMixin
{
	@Inject(method = "register", at = @At("HEAD"), cancellable = true)
	private static void pleaseDontRegisterTestCommand(CallbackInfo ci)
	{
		ci.cancel();
	}
}

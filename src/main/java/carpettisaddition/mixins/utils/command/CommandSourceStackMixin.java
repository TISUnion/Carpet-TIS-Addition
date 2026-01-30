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

package carpettisaddition.mixins.utils.command;

import carpettisaddition.utils.command.CommandSourceStackWithExtraContextArguments;
import com.google.common.collect.Maps;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(CommandSourceStack.class)
public abstract class CommandSourceStackMixin implements CommandSourceStackWithExtraContextArguments
{
	@Shadow
	public abstract CommandSourceStack withPosition(Vec3 vec3);

	@Shadow @Final
	private Vec3 worldPosition;

	@Unique
	private Map<String, ParsedArgument<CommandSourceStack, ?>> extraParsedArgs$TISCM = null;

	@SuppressWarnings("unchecked")
	@Override
	@NotNull
	public CommandSourceStack createMerge$TISCM(CommandSourceStack source, CommandContext<CommandSourceStack> ctx)
	{
		CommandSourceStack copy = this.withPosition(this.worldPosition.add(1, 0, 0)).withPosition(this.worldPosition);
		Map<String, ParsedArgument<CommandSourceStack, ?>> newExtra = ((CommandSourceStackMixin)(Object)copy).extraParsedArgs$TISCM = Maps.newHashMap();
		if (this.extraParsedArgs$TISCM != null)
		{
			newExtra.putAll(this.extraParsedArgs$TISCM);
		}
		newExtra.putAll(((CommandContextAccessor<CommandSourceStack>)ctx).getArguments$TISCM());
		return copy;
	}

	@Override
	public void putExtraContextArgument$TISCM(String name, ParsedArgument<CommandSourceStack, ?> value)
	{
		if (this.extraParsedArgs$TISCM == null)
		{
			this.extraParsedArgs$TISCM = Maps.newHashMap();
		}
		this.extraParsedArgs$TISCM.put(name, value);
	}

	@Override
	@Nullable
	public ParsedArgument<CommandSourceStack, ?> getExtraContextArgument$TISCM(String name)
	{
		return this.extraParsedArgs$TISCM != null ? this.extraParsedArgs$TISCM.get(name) : null;
	}

	@SuppressWarnings("DataFlowIssue")
	@ModifyReturnValue(
			method = {
					"withEntity",
					"withPosition",
					"withRotation",

					//#if MC >= 1.20.3
					//$$ "withCallback(Lnet/minecraft/commands/CommandResultCallback;)Lnet/minecraft/commands/CommandSourceStack;",
					//#else
					"withCallback(Lcom/mojang/brigadier/ResultConsumer;)Lnet/minecraft/commands/CommandSourceStack;",
					//#endif

					"withSuppressedOutput",
					"withPermission",
					"withMaximumPermission",
					"withAnchor",
					"withLevel",

					//#if MC >= 1.19
					//$$ "withSigningContext",
					//#endif
			},
			at = @At("TAIL")
	)
	private CommandSourceStack copyExtraParsedArgsIfNeeded(CommandSourceStack copy)
	{
		CommandSourceStack self = (CommandSourceStack)(Object)this;
		if (copy != self && this.extraParsedArgs$TISCM != null)
		{
			((CommandSourceStackMixin)(Object)copy).extraParsedArgs$TISCM = Maps.newHashMap(this.extraParsedArgs$TISCM);
		}
		return copy;
	}
}

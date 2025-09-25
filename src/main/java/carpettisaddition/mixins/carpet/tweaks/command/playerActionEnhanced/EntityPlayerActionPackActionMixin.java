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

package carpettisaddition.mixins.carpet.tweaks.command.playerActionEnhanced;

import carpet.helpers.EntityPlayerActionPack;
import carpettisaddition.helpers.carpet.playerActionEnhanced.IEntityPlayerActionPackAction;
import carpettisaddition.helpers.carpet.playerActionEnhanced.randomly.gen.RandomGen;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerActionPack.Action.class)
public abstract class EntityPlayerActionPackActionMixin implements IEntityPlayerActionPackAction
{
	///////////////////////////////// randomly /////////////////////////////////

	@Mutable
	@Shadow(remap = false) @Final public int interval;

	@Unique
	private RandomGen intervalRandomGen = null;

	@Override
	public void setIntervalRandomGenerator$TISCM(RandomGen gen)
	{
		this.intervalRandomGen = gen;
	}

	@Inject(
			method = "tick",
			at = @At(
					value = "FIELD",
					target = "Lcarpet/helpers/EntityPlayerActionPack$Action;interval:I"
					//#if MC >= 11600
					//$$ , ordinal = 1
					//#endif
			),
			remap = false
	)
	private void changeIntervalRandomly(CallbackInfoReturnable<Boolean> cir)
	{
		if (this.intervalRandomGen != null)
		{
			this.interval = this.intervalRandomGen.generateRandomInterval();
		}
	}

	///////////////////////////////// perTick /////////////////////////////////

	@Unique
	private Integer perTick = null;

	@Override
	public void setPerTickMultiplier$TISCM(int perTick)
	{
		this.perTick = perTick;
	}

	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lcarpet/helpers/EntityPlayerActionPack$ActionType;execute(Lnet/minecraft/server/network/ServerPlayerEntity;Lcarpet/helpers/EntityPlayerActionPack$Action;)Z",
					remap = true
			),
			remap = false
	)
	private void perTickMultiplier(EntityPlayerActionPack actionPack, EntityPlayerActionPack.ActionType type, CallbackInfoReturnable<Boolean> cir)
	{
		if (this.perTick != null)
		{
			for (int i = 0; i < this.perTick - 1; i++)
			{
				EntityPlayerActionPackActionTypeAccessor typeAccessor = ((EntityPlayerActionPackActionTypeAccessor)(Object)type);
				ServerPlayerEntity player = ((EntityPlayerActionPackAccessor)actionPack).getPlayer();
				EntityPlayerActionPack.Action self = (EntityPlayerActionPack.Action)(Object)this;

				typeAccessor.invokeExecute(player, self);
				typeAccessor.invokeInactiveTick(player, self);
			}
		}
	}
}

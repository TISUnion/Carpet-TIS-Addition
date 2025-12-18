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

package carpettisaddition.mixins.carpet.tweaks.logger.explosion;

import carpet.logging.logHelpers.ExplosionLogHelper;
import carpettisaddition.helpers.carpet.tweaks.logger.explosion.ExplosionLogHelperWithEntity;
import carpettisaddition.helpers.carpet.tweaks.logger.explosion.ITntEntity;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.TextUtils;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ExplosionLogHelper.class)
public abstract class ExplosionLogHelperMixin implements ExplosionLogHelperWithEntity
{
	// fabric carpet 1.4.84 removes the entity fields, but we need that
	// so here's comes another one
	@Unique
	private Entity entity$TISCM = null;

	@Override
	public void setEntity$TISCM(Entity entity$TISCM)
	{
		this.entity$TISCM = entity$TISCM;
	}

	/**
	 * velocity -> angle in [0, 2pi)
	 * for carpet rule hardcodeTNTangle
	 */
	@Unique
	private double calculateAngle(Vec3 velocity)
	{
		double vx = velocity.x(), vz = velocity.z();
		double angle;
		if (vz != 0)
		{
			angle = Math.atan(vx / vz);
			if (vz > 0)
			{
				angle += Math.PI;
			}
			else if (vx > 0)
			{
				angle += 2 * Math.PI;
			}
		}
		else
		{
			angle = vx > 0 ? 1.5 * Math.PI : 0.5 * Math.PI;
		}
		return angle;
	}

	@Inject(
			//#if MC >= 26.1
			//$$ method = "lambda$onExplosionDone$0",
			//#else
			method = "lambda$onExplosionDone$1",
			//#endif
			at = @At("RETURN"),
			remap = false,
			cancellable = true
	)
	private void attachBlockDestroyedWarning(
			//#if MC >= 11700
			//$$ long gametime, String option,
			//#else
			List<BaseComponent> messages_, String option,
			//#endif

			//#if MC >= 11900
			//$$ CallbackInfoReturnable<Component[]> cir
			//#else
			CallbackInfoReturnable<BaseComponent[]> cir
			//#endif
	)
	{
		if (this.entity$TISCM instanceof PrimedTnt)
		{
			ITntEntity iTntEntity = (ITntEntity)this.entity$TISCM;
			if (iTntEntity.dataRecorded$TISCM())
			{
				List<BaseComponent> messages = Lists.newArrayList();
				for (Component text : cir.getReturnValue())
				{
					if (text instanceof BaseComponent)
					{
						// for easier 1.19 compact
						//noinspection CastCanBeRemovedNarrowingVariableType
						messages.add((BaseComponent)text);
					}
				}

				String angleString = String.valueOf(this.calculateAngle(iTntEntity.getInitializedVelocity$TISCM()));
				String posString = TextUtils.coord(iTntEntity.getInitializedPosition$TISCM());
				BaseComponent tntText = Messenger.fancy(
						"r",
						Messenger.s("[TNT]"),
						Messenger.c(
								"w Initialized Data\n",
								String.format("w - Position: %s\n", posString),
								String.format("w - Angle: %s", angleString)
						),
						Messenger.ClickEvents.suggestCommand(posString + " " + angleString)
				);
				switch (option)
				{
					case "brief":
						BaseComponent lastMessage = messages.get(messages.size() - 1);
						messages.set(messages.size() - 1, Messenger.c(lastMessage, "w  ", tntText));
						break;
					case "full":
						messages.add(Messenger.c(Messenger.s("  "), tntText));
						break;
				}

				cir.setReturnValue(messages.toArray(new BaseComponent[0]));
			}
		}
	}
}

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

package carpettisaddition.mixins.carpet.tweaks.logger.projectile;

import carpet.logging.logHelpers.TrajectoryLogHelper;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.carpet.tweaks.logger.projectile.ProjectileLoggerTarget;
import carpettisaddition.helpers.carpet.tweaks.logger.projectile.TrajectoryLoggerUtil;
import carpettisaddition.helpers.carpet.tweaks.logger.projectile.VisualizeTrajectoryHelper;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(TrajectoryLogHelper.class)
public abstract class TrajectoryLogHelperMixin
{
	@Shadow(remap = false) private ArrayList<Vec3d> positions;
	@Shadow(remap = false) private boolean doLog;
	@Shadow(remap = false) @Final private static int MAX_TICKS_PER_LINE;

	// visualize logging
	@Unique
	private World world;
	@Unique
	private Entity entity;
	private boolean doVisualizeLogging;
	private boolean hasCreatedVisualizer;
	private final Translator translator = new Translator("logger.projectiles.visualized");

	@Inject(method = "<init>", at = @At("TAIL"))
	private void initTISCMStuffs(String logName, CallbackInfo ci)
	{
		this.doVisualizeLogging = false;
		this.hasCreatedVisualizer = false;
		if (TrajectoryLoggerUtil.isVisualizer.get())
		{
			TrajectoryLoggerUtil.isVisualizer.set(false);
			this.doLog = false;
			return;
		}
		if ("projectiles".equals(logName))
		{
			this.entity = TrajectoryLoggerUtil.currentEntity.get();
			TrajectoryLoggerUtil.currentEntity.set(null);
			if (this.entity != null)
			{
				this.world = this.entity.getEntityWorld();
				this.doVisualizeLogging = this.world != null;
			}
		}
	}

	@Inject(method = "onFinish", at = @At("HEAD"), remap = false)
	private void checkIfIsVisualizeLogger(CallbackInfo ci)
	{
		if (this.doVisualizeLogging)
		{
			if (VisualizeTrajectoryHelper.isVisualizeProjectile(this.entity))
			{
				this.doLog = false;
			}
			else
			{
				VisualizeTrajectoryHelper.clearVisualizers();
			}
		}
	}

	private Optional<HitResult> getHitResult()
	{
		if (this.entity instanceof ProjectileLoggerTarget)
		{
			return Optional.ofNullable(((ProjectileLoggerTarget)this.entity).getHitResult());
		}
		return Optional.empty();
	}

	/**
	 * lambda method in {@link TrajectoryLogHelper#onFinish}
	 */
	@Inject(method = "lambda$onFinish$0", at = @At("TAIL"), remap = false, cancellable = true)
	private void projectileLoggerEnhance(
			String option,
			//#if MC >= 11900
			//$$ CallbackInfoReturnable<Text[]> cir
			//#else
			CallbackInfoReturnable<BaseText[]> cir
			//#endif
	)
	{
		List<BaseText> comp = Lists.newArrayList();
		for (Text text : cir.getReturnValue())
		{
			if (text instanceof BaseText)
			{
				// easier 1.19 compact
				//noinspection CastCanBeRemovedNarrowingVariableType
				comp.add((BaseText)text);
			}
			else
			{
				CarpetTISAdditionServer.LOGGER.warn("Unsupported text type {} found in return value in TrajectoryLogHelperMixin#projectileLoggerEnhance", text.getClass());
				return;
			}
		}

		Optional<HitResult> hitResultOptional = getHitResult();
		Vec3d hitPos = null;
		BaseText hitType = null;
		if (hitResultOptional.isPresent())
		{
			hitPos = hitResultOptional.get().getPos();
			if (hitResultOptional.get() instanceof BlockHitResult)
			{
				hitType = Messenger.c("w block ", Messenger.coord(null, ((BlockHitResult)hitResultOptional.get()).getBlockPos(), DimensionWrapper.of(this.world)));
			}
			else if (hitResultOptional.get() instanceof EntityHitResult)
			{
				hitType = Messenger.c("w entity (", Messenger.entity(null, ((EntityHitResult)hitResultOptional.get()).getEntity()), "w )");
			}
			else
			{
				hitType = Messenger.s("?");
			}
		}
		switch (option)
		{
			case "brief":
				if (hitResultOptional.isPresent())
				{
					BaseText lastLine = comp.get(comp.size() - 1);
					BaseText marker = Messenger.fancy(
							"g",
							Messenger.s(" x"),
							Messenger.c(
									"w Hit: ",
									hitType,
									String.format("w \nx: %f", hitPos.getX()),
									String.format("w \ny: %f", hitPos.getY()),
									String.format("w \nz: %f", hitPos.getZ())
							),
							null
					);
					if (lastLine.getString().length() >= MAX_TICKS_PER_LINE * 2)  // " x".length() == 2
					{
						comp.add(marker);
					}
					else
					{
						lastLine.append(marker);
					}
				}
				break;
			case "full":
				if (hitResultOptional.isPresent())
				{
					comp.add(Messenger.c("w Hit: ", hitType));
				}
				break;
			case "visualize":
				if (this.doVisualizeLogging)
				{
					if (CarpetTISAdditionSettings.visualizeProjectileLoggerEnabled)
					{
						comp.add(this.translator.tr("info", this.positions.size()));
						if (!this.hasCreatedVisualizer)
						{
							for (int i = 0; i < this.positions.size(); i++)
							{
								VisualizeTrajectoryHelper.createVisualizer(this.world, this.positions.get(i), String.valueOf(i));
							}
							hitResultOptional.ifPresent(hitResult -> VisualizeTrajectoryHelper.createVisualizer(this.world, hitResult.getPos(), "Hit"));
							this.hasCreatedVisualizer = true;
						}
					}
					else
					{
						comp.add(Messenger.fancy(
								"w",
								this.translator.tr("not_enabled.warn"),
								this.translator.tr("not_enabled.hint"),
								new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/carpet visualizeProjectileLoggerEnabled true")
						));
					}
				}
				break;
		}
		cir.setReturnValue(comp.toArray(
				//#if MC >= 11900
				//$$ new Text[0]
				//#else
				new BaseText[0]
				//#endif
		));
	}
}

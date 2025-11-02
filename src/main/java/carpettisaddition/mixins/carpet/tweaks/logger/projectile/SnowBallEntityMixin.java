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

import carpet.logging.LoggerRegistry;
import carpettisaddition.helpers.carpet.tweaks.logger.projectile.VisualizeTrajectoryHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 12003
//#disable-remap
//$$ import net.minecraft.world.level.Explosion;
//#enable-remap
//#endif

@Mixin(Snowball.class)
public abstract class SnowBallEntityMixin extends ThrowableItemProjectile
{
	public SnowBallEntityMixin(EntityType<? extends ThrowableItemProjectile> type, Level world)
	{
		super(type, world);
	}

	@Intrinsic
	@Override
	public void tick()
	{
		if (VisualizeTrajectoryHelper.isVisualizeProjectile(this))
		{
			if (LoggerRegistry.getLogger("projectiles").hasOnlineSubscribers())
			{
				if (this.getDeltaMovement().lengthSqr() > 0)
				{
					this.hasImpulse = true;
					this.setDeltaMovement(Vec3.ZERO);
				}
				VisualizeTrajectoryHelper.addVisualizer(this);
			}
			else
			{
				//#if MC >= 11700
				//$$ this.discard();
				//#else
				this.remove();
				//#endif
			}
		}
		else
		{
			super.tick();
		}
	}

	@Intrinsic
	@Override
	public boolean isIgnoringBlockTriggers()
	{
		if (VisualizeTrajectoryHelper.isVisualizeProjectile(this))
		{
			return true;
		}
		return super.isIgnoringBlockTriggers();
	}

	@Intrinsic
	@Override
	public boolean ignoreExplosion(
			//#if MC >= 12003
			//#disable-remap
			//$$ Explosion explosion
			//#enable-remap
			//#endif
	)
	{
		if (VisualizeTrajectoryHelper.isVisualizeProjectile(this))
		{
			return true;
		}
		return super.ignoreExplosion(
				//#if MC >= 12003
				//$$ explosion
				//#endif
		);
	}
}

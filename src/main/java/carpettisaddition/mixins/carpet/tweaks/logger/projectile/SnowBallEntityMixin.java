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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.thrown.SnowballEntity;
import net.minecraft.entity.thrown.ThrownItemEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 12003
//$$ import net.minecraft.world.explosion.Explosion;
//#endif

@Mixin(SnowballEntity.class)
public abstract class SnowBallEntityMixin extends ThrownItemEntity
{
	public SnowBallEntityMixin(EntityType<? extends ThrownItemEntity> type, World world)
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
				if (this.getVelocity().lengthSquared() > 0)
				{
					this.velocityDirty = true;
					this.setVelocity(Vec3d.ZERO);
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
	public boolean canAvoidTraps()
	{
		if (VisualizeTrajectoryHelper.isVisualizeProjectile(this))
		{
			return true;
		}
		return super.canAvoidTraps();
	}

	@Intrinsic
	@Override
	public boolean isImmuneToExplosion(
			//#if MC >= 12003
			//$$ Explosion explosion
			//#endif
	)
	{
		if (VisualizeTrajectoryHelper.isVisualizeProjectile(this))
		{
			return true;
		}
		return super.isImmuneToExplosion(
				//#if MC >= 12003
				//$$ explosion
				//#endif
		);
	}
}

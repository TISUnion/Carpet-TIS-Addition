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

package carpettisaddition.mixins.command.player.pertick;

import carpettisaddition.helpers.carpet.playerActionEnhanced.IServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements IServerPlayerEntity
{
	public void pushOldPosRot(Vec3d pos, Vec2f rot)
	{
		ServerPlayerEntity self = (ServerPlayerEntity)(Object)this;
		self.prevX = pos.x;
		self.prevY = pos.y;
		self.prevZ = pos.z;
		self.prevPitch = rot.x;
		self.prevYaw = rot.y;
	}

	public void popOldPosRot()
	{
		ServerPlayerEntity self = (ServerPlayerEntity)(Object)this;
		self.prevX = self.getPos().x;
		self.prevY = self.getPos().y;
		self.prevZ = self.getPos().z;
		self.prevPitch = self.getPitch(1.0f);
		self.prevYaw = self.getYaw(1.0f);
	}
}

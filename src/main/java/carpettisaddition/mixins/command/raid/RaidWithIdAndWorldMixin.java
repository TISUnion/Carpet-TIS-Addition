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

package carpettisaddition.mixins.command.raid;

import carpettisaddition.commands.raid.RaidWithIdAndWorld;
import net.minecraft.entity.raid.Raid;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * mc1.14 ~ mc1.21.4: subproject 1.15.2 (main project)        <--------
 * mc1.21.5+        : subproject 1.21.5
 */
@Mixin(Raid.class)
public abstract class RaidWithIdAndWorldMixin implements RaidWithIdAndWorld
{
	@Shadow @Final private int id;
	@Shadow @Final private ServerWorld world;

	@Override
	public int getRaidId$TISCM()
	{
		return this.id;
	}

	@Override
	public void setRaidId$TISCM(int id)
	{
		throw new RuntimeException("TISCM assertion error: not implemented in mc < 1.21.5");
	}

	@Override
	public ServerWorld getRaidWorld$TISCM()
	{
		return this.world;
	}

	@Override
	public void setRaidWorld$TISCM(ServerWorld world)
	{
		throw new RuntimeException("TISCM assertion error: not implemented in mc < 1.21.5");
	}
}

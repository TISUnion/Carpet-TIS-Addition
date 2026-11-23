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

package carpettisaddition.mixins.command.lifetime.spawning.transdimension;

import carpettisaddition.utils.compat.DummyClass;
import org.spongepowered.asm.mixin.Mixin;

/**
 *           mc >= 1.21.2: subproject 1.21.3                <--------
 * 1.19.4 <= mc <  1.21.2: subproject 1.19.4
 *           mc <  1.19.4: subproject 1.15.2 (main project)
 *
 * Commands and portals both use Entity.teleportCrossDimension, which the existing mixins track.
 */
@Mixin(DummyClass.class)
public abstract class TeleportCommandMixin
{
}

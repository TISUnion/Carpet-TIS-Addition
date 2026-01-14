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

package carpettisaddition.mixins.rule.endGatewayChunkLoadingBackport;

import carpettisaddition.utils.compat.DummyClass;
import org.spongepowered.asm.mixin.Mixin;

/**
 * mc1.14   ~ mc1.16.5: subproject 1.15.2 (main project)
 * mc1.17.1 ~ mc1.20.6: subproject 1.17.1
 * mc1.21+            : subproject 1.21.1        <--------
 */
@Mixin(DummyClass.class)
public abstract class TheEndGatewayBlockEntityMixin
{
}

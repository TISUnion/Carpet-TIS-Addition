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

package carpettisaddition.mixins.command.lifetime.removal.merge;

import net.minecraft.entity.ExperienceOrbEntity;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 12105
//$$ import org.spongepowered.asm.mixin.gen.Invoker;
//#else
import org.spongepowered.asm.mixin.gen.Accessor;
//#endif

// used in 1.17+
@Mixin(ExperienceOrbEntity.class)
public interface ExperienceOrbEntityAccessor
{
	//#if MC >= 12105
	//$$ @Invoker("getValue")
	//#else
	@Accessor("amount")
	//#endif
	int getAmount$TISCM();

	//#if MC >= 12105
	//$$ @Invoker("setValue")
	//#else
	@Accessor("amount")
	//#endif
	void setAmount$TISCM(int amount);
}
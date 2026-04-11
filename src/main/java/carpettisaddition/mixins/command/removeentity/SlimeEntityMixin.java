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

package carpettisaddition.mixins.command.removeentity;

import carpettisaddition.commands.removeentity.EntityToBeCleanlyRemoved;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 26.2
//$$ import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
//#else
import net.minecraft.world.entity.monster.Slime;
//#endif

@Mixin(
		//#if MC >= 26.2
		//$$ AbstractCubeMob.class
		//#else
		Slime.class
		//#endif
)
public abstract class SlimeEntityMixin implements EntityToBeCleanlyRemoved
{
	@Unique
	private boolean toBeCleanlyRemoved$TISCM = false;

	@Override
	public void setToBeCleanlyRemoved$TISCM()
	{
		this.toBeCleanlyRemoved$TISCM = true;
	}

	@ModifyExpressionValue(
			method = "remove",
			at = @At(
					value = "CONSTANT",
					args = "intValue=1"
			)
	)
	private int dontSplitIfIsRemovedByRemoveEntityCommand(int value)
	{
		if (this.toBeCleanlyRemoved$TISCM)
		{
			value = Integer.MAX_VALUE;
		}
		return value;
	}
}

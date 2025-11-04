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

package carpettisaddition.mixins.command.removeentity;

import carpettisaddition.commands.removeentity.EntityToBeCleanlyRemoved;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 12102
//$$ import net.minecraft.world.entity.vehicle.AbstractChestBoat;
//#else
import net.minecraft.world.entity.vehicle.ChestBoat;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
@Mixin(
		//#if MC >= 12102
		//$$ AbstractChestBoat.class
		//#else
		ChestBoat.class
		//#endif
)
public abstract class ChestBoatEntityMixin implements EntityToBeCleanlyRemoved
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
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/Entity$RemovalReason;shouldDestroy()Z"
			)
	)
	private boolean dontDropContentsIfIsRemovedByRemoveEntityCommand(boolean willDropContents)
	{
		if (this.toBeCleanlyRemoved$TISCM)
		{
			willDropContents = false;
		}
		return willDropContents;
	}
}

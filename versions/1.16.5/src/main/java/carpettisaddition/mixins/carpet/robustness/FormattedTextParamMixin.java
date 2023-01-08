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

package carpettisaddition.mixins.carpet.robustness;

import carpet.script.utils.ShapeDispatcher;
import carpet.script.value.FormattedTextValue;
import carpet.script.value.Value;
import carpet.utils.Messenger;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.utils.ModIds;
import com.google.gson.JsonParseException;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

//#if MC >= 11903
//$$ import net.minecraft.world.World;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.16"))
@Mixin(ShapeDispatcher.FormattedTextParam.class)
public abstract class FormattedTextParamMixin
{
	/**
	 * @reason Added try-catch in case it deserialize failed
	 * Might happen e.g. when client receive text tag from 1.15.2 server with old fabric carpet, since the content would
	 * be a raw string instead of a serialized text string
	 * @author Fallen_Breath
	 */
	@Overwrite(remap = false)
	public Value decode(
			Tag tag
			//#if MC >= 11903
			//$$ , World world
			//#endif
	)
	{
		String str = tag.asString();
		try
		{
			// vanilla carpet
			return FormattedTextValue.deserialize(str);
		}
		catch (JsonParseException e)
		{
			CarpetTISAdditionServer.LOGGER.warn("Fail to decode incoming tag in FormattedTextParam, text \"{}\" is not deserialize-able", str);
			return new FormattedTextValue(Messenger.s(str));
		}
	}
}
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

package carpettisaddition.utils.compat.carpet;

import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.text.BaseText;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * A fake one for less mixin conflicts for mc1.14
 * Used in {@link carpettisaddition.mixins.carpet.tweaks.logger.explosion.ExplosionLogHelperMixin}
 */
public class ExplosionLogHelper
{
	public final Entity entity = null;
	private final Map<Integer, Integer> someMap = Maps.newHashMap();

	private void onExplosionDone()
	{
		someMap.put(1, 2);
		BiFunction<List<BaseText>, String, BaseText[]> doLog = (messages, options) -> {
			someMap.forEach((k, v) -> {
				v = v + 1;
			});
			List<BaseText> list = Lists.newArrayList();
			list.add(Messenger.s("fake"));
			return list.toArray(new BaseText[0]);
		};
	}
}
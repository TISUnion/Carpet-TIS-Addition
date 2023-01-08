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

package carpettisaddition.mixins.rule.synchronizedLightThread;

import carpet.utils.CarpetProfiler;
import carpettisaddition.helpers.rule.synchronizedLightThread.LightThreadSynchronizer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

//#if MC >= 11900
//$$ import com.google.common.collect.Maps;
//$$ import java.util.Collections;
//$$ import java.util.Map;
//#else
import com.google.common.collect.Lists;
import java.util.List;
//#endif

@Mixin(CarpetProfiler.class)
public abstract class CarpetProfilerMixin
{
	@Shadow(remap = false) @Final @Mutable
	private static
	//#if MC >= 11900
	//$$ Map<String, String>
	//#else
	String[]
	//#endif
	SECTIONS;

	static
	{
		//#if MC >= 11900
		//$$ Map<String, String> map = Maps.newLinkedHashMap(SECTIONS);
		//$$ map.put(LightThreadSynchronizer.SECTION_NAME, LightThreadSynchronizer.SECTION_DESCRIPTION);
		//$$ SECTIONS = Collections.unmodifiableMap(map);
		//#else
		List<String> list = Lists.newArrayList(SECTIONS);
		list.add(LightThreadSynchronizer.SECTION_NAME);
		SECTIONS = list.toArray(new String[0]);
		//#endif
	}
}

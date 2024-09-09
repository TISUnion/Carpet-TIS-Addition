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

package carpettisaddition.helpers.rule.synchronizedLightThread;

import carpet.utils.CarpetProfiler;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.mixins.rule.synchronizedLightThread.ServerLightingProviderAccessor;
import carpettisaddition.mixins.rule.synchronizedLightThread.TaskExecutorAccessor;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.util.thread.TaskExecutor;
import net.minecraft.util.thread.TaskQueue;
import net.minecraft.world.chunk.light.LightingProvider;

import java.util.List;

public class LightThreadSynchronizer
{
	private static final Translator translator = new Translator("rule.synchronizedLightThread");
	public static final String SECTION_NAME = "Lighting synchronization";
	public static final String SECTION_DESCRIPTION = "Light engine synchronization by rule synchronizedLightThread to make sure the lighting engine will not fall behind";

	private static boolean isSafeToWait(boolean synchronizedLightThread, CarpetTISAdditionSettings.LightUpdateOptions lightUpdates)
	{
		return !(synchronizedLightThread && !lightUpdates.shouldExecuteLightTask());
	}

	public static void waitForLightThread(ServerWorld serverWorld)
	{
		serverWorld.getProfiler().push(SECTION_NAME);
		CarpetProfiler.ProfilerToken token = CarpetProfiler.start_section(serverWorld, SECTION_NAME, CarpetProfiler.TYPE.GENERAL);

		LightingProvider lightingProvider =
				//#if MC >= 11500
				serverWorld.getLightingProvider();
				//#else
				//$$ serverWorld.getChunkManager().getLightingProvider();
				//#endif

		if (lightingProvider != null)
		{
			TaskExecutor<?> processor = ((ServerLightingProviderAccessor) lightingProvider).getProcessor();
			// the task queue of the executor of the light thread
			//#if MC >= 12102
			//$$ TaskQueue<?> queue
			//#else
			TaskQueue<?, ?> queue
			//#endif
					= ((TaskExecutorAccessor<?>)processor).getQueue();
			while (!queue.isEmpty())
			{
				Thread.yield();

				// just in case
				if (!isSafeToWait(CarpetTISAdditionSettings.synchronizedLightThread, CarpetTISAdditionSettings.lightUpdates))
				{
					break;
				}
			}
		}

		serverWorld.getProfiler().pop();
		CarpetProfiler.end_current_section(token);
	}

	public static boolean checkRuleSafety(ServerCommandSource source, boolean synchronizedLightThread, CarpetTISAdditionSettings.LightUpdateOptions lightUpdates)
	{
		if (isSafeToWait(synchronizedLightThread, lightUpdates))
		{
			return true;
		}
		if (source != null)
		{
			List<BaseText> list = Lists.newArrayList();
			list.add(Messenger.formatting(translator.tr("safety_warning.0"), "r"));
			list.add(Messenger.formatting(translator.tr("safety_warning.1"), "r"));
			list.add(Messenger.formatting(translator.tr("safety_warning.2"), "r"));
			list.add(Messenger.s("  - synchronizedLightThread: " + synchronizedLightThread, "r"));
			list.add(Messenger.s("  - lightUpdates: " + lightUpdates.toString().toLowerCase(), "r"));
			Messenger.tell(source, list);
		}
		return false;
	}
}

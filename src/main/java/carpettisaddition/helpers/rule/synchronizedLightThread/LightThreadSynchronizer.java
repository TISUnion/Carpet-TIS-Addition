package carpettisaddition.helpers.rule.synchronizedLightThread;

import carpet.utils.CarpetProfiler;
import carpet.utils.Messenger;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.mixins.rule.synchronizedLightThread.ServerLightingProviderAccessor;
import carpettisaddition.mixins.rule.synchronizedLightThread.TaskExecutorAccessor;
import carpettisaddition.translations.Translator;
import com.google.common.collect.Lists;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.util.thread.TaskQueue;
import net.minecraft.world.chunk.light.LightingProvider;

import java.util.List;

public class LightThreadSynchronizer
{
	private static final Translator translator = new Translator("rule", "synchronizedLightThread");
	public static final String SECTION_NAME = "Lighting synchronization";

	private static boolean isSafeToWait(boolean synchronizedLightThread, CarpetTISAdditionSettings.LightUpdateOptions lightUpdates)
	{
		return !(synchronizedLightThread && !lightUpdates.shouldExecuteLightTask());
	}

	public static void waitForLightThread(ServerWorld serverWorld)
	{
		serverWorld.getProfiler().push(SECTION_NAME);
		CarpetProfiler.ProfilerToken token = CarpetProfiler.start_section(serverWorld, SECTION_NAME, CarpetProfiler.TYPE.GENERAL);

		LightingProvider lightingProvider = serverWorld.getLightingProvider();
		if (lightingProvider instanceof ServerLightingProvider)
		{
			// the task queue of the executor of the light thread
			TaskQueue<?, ?> queue = ((TaskExecutorAccessor<?>)((ServerLightingProviderAccessor)lightingProvider).getProcessor()).getQueue();
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
			list.add(Messenger.s(translator.tr("safety_warning.0", "Dangerous combination between rule synchronizedLightThread and rule lightUpdates detected"), "r"));
			list.add(Messenger.s(translator.tr("safety_warning.1", "Rule modification has been cancelled to prevent infinite waiting in lighting synchronization"), "r"));
			list.add(Messenger.s(translator.tr("safety_warning.2", "Related rule values to be set:"), "r"));
			list.add(Messenger.s("  - synchronizedLightThread: " + synchronizedLightThread, "r"));
			list.add(Messenger.s("  - lightUpdates: " + lightUpdates.toString().toLowerCase(), "r"));
			Messenger.send(source, list);
		}
		return false;
	}
}

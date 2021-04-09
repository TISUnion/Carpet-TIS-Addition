package carpettisaddition.mixins.rule.synchronizedLightThread;

import carpet.utils.CarpetProfiler;
import carpettisaddition.helpers.rule.synchronizedLightThread.LightThreadSynchronizer;
import com.google.common.collect.Lists;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(CarpetProfiler.class)
public abstract class CarpetProfilerMixin
{
	@Mutable
	@Shadow(remap = false) @Final private static String[] SECTIONS;

	static
	{
		List<String> list = Lists.newArrayList(SECTIONS);
		list.add(LightThreadSynchronizer.SECTION_NAME);
		SECTIONS = list.toArray(new String[0]);
	}
}

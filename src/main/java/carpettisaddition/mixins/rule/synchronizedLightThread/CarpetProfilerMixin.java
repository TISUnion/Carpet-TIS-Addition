package carpettisaddition.mixins.rule.synchronizedLightThread;

import carpet.utils.CarpetProfiler;
import carpettisaddition.helpers.rule.synchronizedLightThread.LightThreadSynchronizer;
import com.google.common.collect.Maps;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.Map;

@Mixin(CarpetProfiler.class)
public abstract class CarpetProfilerMixin
{
	@Mutable @Final @Shadow(remap = false)
	private static Map<String, String> SECTIONS;

	static
	{
		Map<String, String> map = Maps.newLinkedHashMap(SECTIONS);
		map.put(LightThreadSynchronizer.SECTION_NAME, "Light engine synchronization by rule synchronizedLightThread to make sure the lighting engine will not fall behind");
		SECTIONS = Collections.unmodifiableMap(map);
	}
}

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

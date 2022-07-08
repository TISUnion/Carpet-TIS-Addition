package carpettisaddition.utils.compat.carpet;

import carpet.utils.Messenger;
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
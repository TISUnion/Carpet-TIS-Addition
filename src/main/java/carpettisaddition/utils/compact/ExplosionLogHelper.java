package carpettisaddition.utils.compact;

import carpet.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.text.BaseText;

import java.util.List;
import java.util.function.BiFunction;

/**
 * A fake one for less mixin conflicts
 * Used in {@link carpettisaddition.mixins.carpet.tweaks.logger.explosion.ExplosionLogHelperMixin}
 */
public class ExplosionLogHelper
{
	public final Entity entity = null;

	@SuppressWarnings("SuspiciousToArrayCall")
	private void onExplosionDone()
	{
		BiFunction<List<BaseText>, String, BaseText[]> doLog = (messages, options) -> {
			return Lists.newArrayList(Messenger.s("fake")).toArray(new BaseText[0]);
		};
	}
}

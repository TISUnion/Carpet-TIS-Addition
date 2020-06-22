package carpettisaddition.mixins;

import carpettisaddition.helpers.CustomStatsHelper;
import it.unimi.dsi.fastutil.objects.Object2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(ServerStatHandler.class)
public class ServerStatHandlerMixin
{
	@Redirect(
			method = "sendStats",
			at = @At(
					value = "INVOKE",
					target = "Lit/unimi/dsi/fastutil/objects/Object2IntMap;put(Ljava/lang/Object;I)I"
			),
			remap = false
	)
	private int dontPutNotVanillaStat(Object2IntMap<Stat<?>> map, Object obj, int value)
	{
		if (CustomStatsHelper.isCustomStat((Stat)obj))
		{
			return map.defaultReturnValue();
		}
		else
		{
			return map.put((Stat)obj, value);
		}
	}
}

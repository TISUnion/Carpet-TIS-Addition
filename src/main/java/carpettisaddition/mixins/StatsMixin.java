package carpettisaddition.mixins;

import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(Stats.class)
public class StatsMixin
{
	// also go update in CustomStatsHelper
	private static final Identifier BREAK_BEDROCK;
	private static final Identifier FIREWORK_BOOST;

	@Shadow
	private static Identifier register(String string, StatFormatter statFormatter)
	{
		return null;
	}

	static
	{
		BREAK_BEDROCK = register("break_bedrock", StatFormatter.DEFAULT);
		FIREWORK_BOOST = register("firework_boost", StatFormatter.DEFAULT);
	}
}

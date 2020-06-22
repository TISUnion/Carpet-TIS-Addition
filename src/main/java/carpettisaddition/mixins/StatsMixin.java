package carpettisaddition.mixins;

import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(Stats.class)
public class StatsMixin
{
	private static final Identifier BREAK_BEDROCK;

	@Shadow
	private static Identifier register(String string, StatFormatter statFormatter)
	{
		return null;
	}

	static
	{
		BREAK_BEDROCK = register("break_bedrock", StatFormatter.DEFAULT);
	}
}

package carpettisaddition;

import carpettisaddition.utils.deobfuscator.StackTraceDeobfuscator;
import net.fabricmc.api.ModInitializer;

public class CarpetTISAdditionMod implements ModInitializer
{
	@Override
	public void onInitialize()
	{
		StackTraceDeobfuscator.init();
	}
}

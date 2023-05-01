package carpettisaddition.utils.mixin.testers;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.mixin.ConditionTester;
import me.fallenbreath.conditionalmixin.api.util.VersionChecker;
import net.fabricmc.loader.api.FabricLoader;

public class YeetUpdateSuppressionCrashTester implements ConditionTester
{
	@Override
	public boolean isSatisfied(String mixinClassName)
	{
		return isAllowed();
	}

	public static boolean isAllowed()
	{
		return
				!isMatched(ModIds.carpet, ">=1.4.49 <=1.4.76") &&
				!isMatched(ModIds.carpet_extra, ">=1.4.14 <=1.4.43");
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	private static boolean isMatched(String modId, String versionPredicate)
	{
		return FabricLoader.getInstance().
				getModContainer(modId).
				map(mod -> VersionChecker.doesVersionSatisfyPredicate(mod.getMetadata().getVersion(), versionPredicate)).
				orElse(false);
	}
}

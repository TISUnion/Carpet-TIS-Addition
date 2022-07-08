package carpettisaddition.utils.mixin;

import carpettisaddition.utils.ModIds;
import me.jellysquid.mods.lithium.common.config.LithiumConfig;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

import java.io.File;

//#if MC >= 11600
//$$ import me.jellysquid.mods.lithium.common.config.Option;
//#endif

public class LithiumConfigAccess
{
	@Nullable
	private static final Object config;

	private static boolean isLithiumLoaded()
	{
		return FabricLoader.getInstance().isModLoaded(ModIds.lithium);
	}

	/**
	 * For 1.15.2 lithium, check {@link me.jellysquid.mods.lithium.mixin.LithiumMixinPlugin#setupMixins}
	 * and fill interested rules in the switch statement below
	 *
	 * For 1.16+ lithium, {@link me.jellysquid.mods.lithium.common.config.LithiumConfig#getEffectiveOptionForMixin} exists
	 * so no more manual maintaining
	 */
	public static boolean isLithiumMixinRuleEnabled(String mixinRule)
	{
		if (config == null)
		{
			return false;
		}

		//#if MC >= 11600
		//$$ // in lithium's usage, the passed argument is something like "block.stone.StoneOptimizationMixin"
		//$$ // so we need to attach a fake class name as suffix, to make sure lithium's logic works correctly
		//$$ Option option = ((LithiumConfig)config).getEffectiveOptionForMixin(mixinRule + ".TISCM_DummyMixin");
		//$$ return option != null && option.isEnabled();
		//#else
		switch (mixinRule)
		{
			default:
				return false;
		}
		//#endif
	}

	static
	{
		if (isLithiumLoaded())
		{
			config = LithiumConfig.load(new File("./config/lithium.properties"));
		}
		else
		{
			config = null;
		}
	}
}

package carpettisaddition;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class CarpetTISAdditionMixinPlugin implements IMixinConfigPlugin
{
	private static final String LITHIUM_MOD_ID = "lithium";

	@Override
	public void onLoad(String mixinPackage)
	{

	}

	@Override
	public String getRefMapperConfig()
	{
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
	{
		boolean isLithiumLoaded = FabricLoader.getInstance().isModLoaded(LITHIUM_MOD_ID);
		if (mixinClassName.contains(MicroTickLoggerManager.ON_SCHEDULE_TILE_TICK_EVENT_BACKUP_MIXIN))
		{
			return isLithiumLoaded;
		}
		if (mixinClassName.contains(MicroTickLoggerManager.ON_SCHEDULE_TILE_TICK_EVENT_MAIN_MIXIN))
		{
			if (isLithiumLoaded)
			{
				CarpetTISAdditionServer.LOGGER.info("Detected Lithium mod, loading backup mixin for onScheduleTileTick event");
				CarpetTISAdditionServer.LOGGER.info("ScheduleTileTick events might not be fully listened");
				return false;
			}
			else
			{
				return true;
			}
		}
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets)
	{

	}

	@Override
	public List<String> getMixins()
	{
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
	{

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
	{

	}
}

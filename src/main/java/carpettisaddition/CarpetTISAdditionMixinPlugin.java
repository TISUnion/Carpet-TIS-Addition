package carpettisaddition;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class CarpetTISAdditionMixinPlugin implements IMixinConfigPlugin
{
	private final Logger LOGGER = LogManager.getLogger();
	private static final String LITHIUM_MOD_ID = "lithium";
	private static final String MALILIB_MOD_ID = "malilib";

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
		if (mixinClassName.contains(".compat.lithium."))
		{
			return FabricLoader.getInstance().isModLoaded(LITHIUM_MOD_ID);
		}
		if (mixinClassName.contains(".compat.malilib."))
		{
			return FabricLoader.getInstance().isModLoaded(MALILIB_MOD_ID);
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

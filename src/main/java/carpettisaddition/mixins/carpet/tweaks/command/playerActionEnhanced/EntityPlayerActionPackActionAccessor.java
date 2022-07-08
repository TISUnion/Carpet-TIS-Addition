package carpettisaddition.mixins.carpet.tweaks.command.playerActionEnhanced;

import carpet.helpers.EntityPlayerActionPack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityPlayerActionPack.Action.class)
public interface EntityPlayerActionPackActionAccessor
{
	@Invoker("<init>")
	static EntityPlayerActionPack.Action invokeConstructor(
			int limit, int interval, int offset
			//#if MC >= 11600
			//$$ , boolean continuous
			//#endif
	)
	{
		throw new RuntimeException();
	}
}

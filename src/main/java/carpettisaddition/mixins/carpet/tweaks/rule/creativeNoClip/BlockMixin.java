package carpettisaddition.mixins.carpet.tweaks.rule.creativeNoClip;

import carpet.CarpetSettings;
import carpettisaddition.helpers.carpet.tweaks.rule.creativeNoClip.CreativeNoClipHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin
{
	@Inject(
			method = "pushEntitiesUpBeforeBlockChange",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;getEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/List;"
			)
	)
	private static void dontMoveCreativeNoClipPlayers_enter(CallbackInfoReturnable<BlockState> cir)
	{
		if (CarpetSettings.creativeNoClip)
		{
			CreativeNoClipHelper.ignoreNoClipPlayersFlag.set(true);
		}
	}

	@Inject(
			method = "pushEntitiesUpBeforeBlockChange",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;getEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/List;",
					shift = At.Shift.AFTER
			)
	)
	private static void dontMoveCreativeNoClipPlayers_exit(CallbackInfoReturnable<BlockState> cir)
	{
		if (CarpetSettings.creativeNoClip)
		{
			CreativeNoClipHelper.ignoreNoClipPlayersFlag.set(false);
		}
	}
}

package carpettisaddition.mixins.carpet.tweaks.command.noPlayerMountAbuse;

import carpet.helpers.EntityPlayerActionPack;
import carpet.utils.Messenger;
import carpettisaddition.utils.TextUtil;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(EntityPlayerActionPack.class)
public abstract class EntityPlayerActionPackMixin
{
	@Shadow(remap = false) @Final private ServerPlayerEntity player;

	@Inject(
			method = "mount",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;size()I"
			),
			locals = LocalCapture.CAPTURE_FAILHARD,
			remap = false
	)
	private void pleaseDoSomePermissionCheck(boolean onlyRideables, CallbackInfoReturnable<EntityPlayerActionPack> cir, List<Entity> entities)
	{
		if (!onlyRideables)  // mount anything
		{
			MinecraftServer minecraftServer = this.player.getServer();
			if (minecraftServer != null)
			{
				if (!minecraftServer.getPlayerManager().isOperator(this.player.getGameProfile()))  // not op
				{
					// hey that's illegal
					Messenger.m(this.player, Messenger.s(TextUtil.getMiscTranslator().tr(
							"player_mount_anything_permission_denied", "Only OP players are allowed to mount anything"), "r"
					));
					entities.clear();
				}
			}
		}
	}
}

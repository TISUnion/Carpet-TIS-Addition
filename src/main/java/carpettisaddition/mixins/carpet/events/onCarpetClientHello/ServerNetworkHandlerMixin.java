package carpettisaddition.mixins.carpet.events.onCarpetClientHello;

import carpet.network.ServerNetworkHandler;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.15"))
@Mixin(ServerNetworkHandler.class)
public abstract class ServerNetworkHandlerMixin
{
	@Inject(method = "onHello", at = @At("TAIL"), remap = false)
	private static void onCarpetClientHello(ServerPlayerEntity playerEntity, PacketByteBuf packetData, CallbackInfo ci)
	{
		CarpetTISAdditionServer.getInstance().onCarpetClientHello(playerEntity);
	}
}

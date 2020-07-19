package carpettisaddition.mixins.command;

import net.minecraft.entity.raid.Raid;
import net.minecraft.entity.raid.RaidManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(RaidManager.class)
public abstract class RaidManager_raidTrackerMixin
{
	@Inject(
			method = "startRaid",
			at = @At(value = "RETURN")
	)
	private void logRaidCreated(ServerPlayerEntity player, CallbackInfoReturnable<Raid> cir)
	{
		Raid raid = cir.getReturnValue();
		if (raid != null)
		{
		}
	}
}

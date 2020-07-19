package carpettisaddition.mixins.command;

import carpettisaddition.helpers.RaidTracker;
import net.minecraft.entity.raid.Raid;
import net.minecraft.entity.raid.RaidManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;


@Mixin(RaidManager.class)
public abstract class RaidManager_raidTrackerMixin
{
	@Inject(
			method = "getOrCreateRaid",
			at = @At(value = "RETURN")
	)
	private void logRaidCreated(ServerWorld world, BlockPos pos, CallbackInfoReturnable<Raid> cir)
	{
		if (world.getRaidAt(pos) == null)
		{
			RaidTracker.trackRaidGenerated(cir.getReturnValue());
		}
	}
}

package carpettisaddition.mixins.command;

import carpettisaddition.helpers.RaidTracker;
import net.minecraft.entity.raid.Raid;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Raid.class)
public abstract class Raid_raidTrackerMixin
{
	@Inject(
			method = "<init>(ILnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V",
			at = @At(value = "RETURN")
	)
	private void onConstruct(CallbackInfo ci)
	{
		RaidTracker.trackRaidGenerated((Raid)(Object)this);
	}

	@Inject(
			method = "addRaider",
			at = @At(value = "RETURN")
	)
	void onAddedRaider(int wave, RaiderEntity raider, BlockPos pos, boolean existing, CallbackInfo ci)
	{
		if (!existing)
		{
			RaidTracker.trackNewRaider(raider);
		}
	}
}

package carpettisaddition.mixins.command.raid;

import carpettisaddition.commands.raid.RaidTracker;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Raid.class)
public abstract class RaidMixin
{
	@Inject(
			method = "<init>(ILnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V",
			at = @At(value = "RETURN")
	)
	private void onConstruct(CallbackInfo ci)
	{
		RaidTracker.getInstance().trackRaidGenerated((Raid)(Object)this);
	}

	@Inject(
			method = "addRaider",
			at = @At(value = "RETURN")
	)
	void onAddedRaider(int wave, RaiderEntity raider, BlockPos pos, boolean existing, CallbackInfo ci)
	{
		if (!existing)
		{
			RaidTracker.getInstance().trackNewRaider(raider);
		}
	}
}

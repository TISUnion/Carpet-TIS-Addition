package carpettisaddition.mixins.rule.chunkTickSpeed;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
	@Shadow public abstract void tickChunk(WorldChunk chunk, int randomTickSpeed);

	private int depth;

	@Inject(method = "tickChunk", at = @At("HEAD"), cancellable = true)
	void checkIfCancelAndInitDepth(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.chunkTickSpeed == 0)
		{
			ci.cancel();
			return;
		}
		this.depth++;
	}

	@Inject(method = "tickChunk", at = @At("RETURN"))
	void tickMultipleTimes(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci)
	{
		if (this.depth == 1)
		{
			// in case rule gets changed during the chunk tick stage
			int ruleValue = CarpetTISAdditionSettings.chunkTickSpeed;
			for (int i = 0; i < ruleValue - 1; i++)
			{
				this.tickChunk(chunk, randomTickSpeed);
			}
		}
		this.depth--;
	}

	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerChunkManager;tick(Ljava/util/function/BooleanSupplier;)V"
			)
	)
	void resetTickChunkDepth(BooleanSupplier shouldKeepTicking, CallbackInfo ci)
	{
		// just in case whatever happened
		this.depth = 0;
	}
}

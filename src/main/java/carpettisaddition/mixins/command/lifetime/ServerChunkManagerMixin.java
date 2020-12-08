package carpettisaddition.mixins.command.lifetime;

import carpettisaddition.commands.lifetime.interfaces.IServerWorld;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerChunkManager.class)
public abstract class ServerChunkManagerMixin
{
	@Shadow @Final private ServerWorld world;

	@Inject(
			method = "tickChunks",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/SpawnHelper;setupSpawn(ILjava/lang/Iterable;Lnet/minecraft/world/SpawnHelper$ChunkSource;)Lnet/minecraft/world/SpawnHelper$Info;"
			)
	)
	private void onCountingMobcapLifeTimeTracker(CallbackInfo ci)
	{
		((IServerWorld)this.world).getLifeTimeWorldTracker().increaseSpawnStageCounter();
	}
}

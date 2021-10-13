package carpettisaddition.mixins.command.refresh;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ThreadedAnvilChunkStorage.class)
public interface ThreadedAnvilChunkStorageAccessor
{
	@Accessor
	Long2ObjectLinkedOpenHashMap<ChunkHolder> getCurrentChunkHolders();

	@Accessor
	int getWatchDistance();

	@Invoker
	static int invokeGetChebyshevDistance(ChunkPos pos, ServerPlayerEntity player, boolean useCameraPosition)
	{
		return 0;
	}

	@Invoker
	void invokeSendWatchPackets(ServerPlayerEntity player, ChunkPos pos, Packet<?>[] packets, boolean withinMaxWatchDistance, boolean withinViewDistance);
}

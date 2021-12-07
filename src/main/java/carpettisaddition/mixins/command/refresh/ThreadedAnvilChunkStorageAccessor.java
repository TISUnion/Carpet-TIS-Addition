package carpettisaddition.mixins.command.refresh;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;
import org.apache.commons.lang3.mutable.MutableObject;
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

	// isChunkWithinEuclideanDistanceRange or whatever
	@Invoker("method_39975")
	static boolean invokeIsChunkWithinEuclideanDistanceRange(int chunkX, int chunkZ, int playerSectionX, int playerSectionZ, int distance)
	{
		return false;
	}

	@Invoker
	void invokeSendWatchPackets(ServerPlayerEntity player, ChunkPos pos, MutableObject<ChunkDataS2CPacket> mutableObject, boolean withinMaxWatchDistance, boolean withinViewDistance);
}

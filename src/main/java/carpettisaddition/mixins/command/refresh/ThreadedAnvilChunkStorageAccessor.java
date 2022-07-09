package carpettisaddition.mixins.command.refresh;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

//#if MC >= 11800
//$$ import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
//$$ import org.apache.commons.lang3.mutable.MutableObject;
//#else
import net.minecraft.network.Packet;
//#endif

@Mixin(ThreadedAnvilChunkStorage.class)
public interface ThreadedAnvilChunkStorageAccessor
{
	@Accessor
	Long2ObjectLinkedOpenHashMap<ChunkHolder> getCurrentChunkHolders();

	@Accessor
	int getWatchDistance();

	//#if MC >= 11800
	//$$ // isChunkWithinEuclideanDistanceRange or whatever
	//$$ @Invoker("isWithinDistance")
	//$$ static boolean invokeIsChunkWithinEuclideanDistanceRange(int chunkX, int chunkZ, int playerSectionX, int playerSectionZ, int distance)
	//$$ {
	//$$ 	return false;
	//$$ }
	//#endif

	//#if MC < 11800
	@Invoker
	static int invokeGetChebyshevDistance(ChunkPos pos, ServerPlayerEntity player, boolean useCameraPosition)
	{
		return 0;
	}
	//#endif

	@Invoker
	void invokeSendWatchPackets(
			ServerPlayerEntity player, ChunkPos pos,
			//#if MC >= 11800
			//$$ MutableObject<ChunkDataS2CPacket> mutableObject,
			//#else
			Packet<?>[] packets,
			//#endif
			boolean withinMaxWatchDistance, boolean withinViewDistance
	);
}

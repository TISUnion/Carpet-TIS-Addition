package carpettisaddition.mixins.command.lifetime.spawning;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin
{
	@Inject(
			method = "randomTick",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/entity/EntityType;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/text/Text;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void onPigmanSpawnedLifeTimeTracker(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci, Entity entity)
	{
		if (entity != null)
		{
			((LifetimeTrackerTarget) entity).recordSpawning(LiteralSpawningReason.PORTAL_PIGMAN);
		}
	}
}

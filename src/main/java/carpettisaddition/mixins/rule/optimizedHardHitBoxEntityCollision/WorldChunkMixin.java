package carpettisaddition.mixins.rule.optimizedHardHitBoxEntityCollision;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.optimizedHardHitBoxEntityCollision.OptimizedHardHitBoxEntityCollisionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.TypeFilterableList;
import net.minecraft.util.math.Box;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Predicate;

@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin
{
	@Shadow @Final private TypeFilterableList<Entity>[] entitySections;

	private TypeFilterableList<Entity>[] hardHitBoxEntitySections;
	private boolean optimizedHHBECEnabled;  // optimizedHardHitBoxEntityCollisionEnabled

	@SuppressWarnings("unchecked")
	@Inject(
			//#if MC >= 11500
			method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/biome/source/BiomeArray;Lnet/minecraft/world/chunk/UpgradeData;Lnet/minecraft/world/TickScheduler;Lnet/minecraft/world/TickScheduler;J[Lnet/minecraft/world/chunk/ChunkSection;Ljava/util/function/Consumer;)V",
			//#else
			//$$ method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/util/math/ChunkPos;[Lnet/minecraft/world/biome/Biome;Lnet/minecraft/world/chunk/UpgradeData;Lnet/minecraft/world/TickScheduler;Lnet/minecraft/world/TickScheduler;J[Lnet/minecraft/world/chunk/ChunkSection;Ljava/util/function/Consumer;)V",
			//#endif
			at = @At("TAIL")
	)
	private void optimizedHardHitBoxEntityCollision_onConstruct(CallbackInfo ci)
	{
		this.hardHitBoxEntitySections = (TypeFilterableList<Entity>[])(new TypeFilterableList[this.entitySections.length]);
		for(int i = 0; i < this.hardHitBoxEntitySections.length; ++i)
		{
			this.hardHitBoxEntitySections[i] = new TypeFilterableList<>(Entity.class);
		}
		this.optimizedHHBECEnabled = CarpetTISAdditionSettings.optimizedHardHitBoxEntityCollision;
	}

	@Inject(method = "addEntity", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void optimizedHardHitBoxEntityCollision_onAddEntity(Entity entity, CallbackInfo ci, int k)
	{
		if (this.optimizedHHBECEnabled)
		{
			if (OptimizedHardHitBoxEntityCollisionHelper.hasHardHitBox(entity))
			{
				this.hardHitBoxEntitySections[k].add(entity);
			}
		}
	}

	@Inject(method = "remove(Lnet/minecraft/entity/Entity;I)V", at = @At("TAIL"))
	private void optimizedHardHitBoxEntityCollision_onRemoveEntity(Entity entity, int i, CallbackInfo ci)
	{
		if (this.optimizedHHBECEnabled)
		{
			this.hardHitBoxEntitySections[i].remove(entity);
		}
	}

	/**
	 * this method is used in {@link World#getEntities(Entity, Box, Predicate)}
	 * which will be invoked in {@link EntityView#getEntityCollisions} (or {@link EntityView#method_20743} in mc 1.14)
	 */
	@Redirect(
			method = "getEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/List;Ljava/util/function/Predicate;)V",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/world/chunk/WorldChunk;entitySections:[Lnet/minecraft/util/TypeFilterableList;"
			),
			require = 4
	)
	private TypeFilterableList<Entity>[] optimizedHardHitBoxEntityCollision_redirectEntitySections(WorldChunk chunk)
	{
		if (this.optimizedHHBECEnabled && OptimizedHardHitBoxEntityCollisionHelper.checkHardHitBoxEntityOnly.get())
		{
			return this.hardHitBoxEntitySections;
		}
		// vanilla
		return this.entitySections;
	}
}

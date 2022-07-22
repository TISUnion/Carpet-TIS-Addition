package carpettisaddition.mixins.rule.optimizedHardHitBoxEntityCollision;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.optimizedHardHitBoxEntityCollision.OptimizedHardHitBoxEntityCollisionHelper;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.Entity;
import net.minecraft.util.collection.TypeFilterableList;
import net.minecraft.util.math.Box;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.17"))
@Mixin(EntityTrackingSection.class)
public abstract class EntityTrackingSectionMixin<T extends EntityLike>
{
	// just like WorldChunk#entitySections in 1.16- but it's per chunk section and it uses genericity
	@Shadow @Final private TypeFilterableList<T> collection;

	private TypeFilterableList<T> hardHitBoxEntitySections;
	private boolean optimizedHHBECEnabled;  // optimizedHardHitBoxEntityCollisionEnabled

	/**
	 * Enable only if {@param entityClass} is Entity.class
	 * See the return type of ServerWorld#getEntityLookup() for the reason of using Entity.class as judgement
	 */
	@Inject(
			method = "<init>",
			at = @At("TAIL")
	)
	private void onConstruct(Class<T> entityClass, EntityTrackingStatus status, CallbackInfo ci)
	{
		this.hardHitBoxEntitySections = new TypeFilterableList<>(entityClass);
		this.optimizedHHBECEnabled = CarpetTISAdditionSettings.optimizedHardHitBoxEntityCollision && entityClass == Entity.class;
	}

	@Inject(method = "add", at = @At("TAIL"))
	private void onAddEntity(T entity, CallbackInfo ci)
	{
		if (this.optimizedHHBECEnabled)
		{
			if (entity instanceof Entity && OptimizedHardHitBoxEntityCollisionHelper.hasHardHitBox((Entity)entity))
			{
				this.hardHitBoxEntitySections.add(entity);
			}
		}
	}

	@Inject(method = "remove", at = @At("TAIL"))
	private void onRemoveEntity(T entity, CallbackInfoReturnable<Boolean> ci)
	{
		if (this.optimizedHHBECEnabled)
		{
			this.hardHitBoxEntitySections.remove(entity);
		}
	}

	/**
	 * Invoke path:
	 * - {@link EntityView#getEntityCollisions}
	 * - {@link World#getOtherEntities(Entity, Box, Predicate)}
	 * - {@link SimpleEntityLookup#forEachIntersects(net.minecraft.util.math.Box, java.util.function.Consumer)}
	 * - {@link SectionedEntityCache#forEachIntersects(net.minecraft.util.math.Box, java.util.function.Consumer)}
	 * -
	 *   (<=1.17) {@link EntityTrackingSection#forEach(java.util.function.Predicate, java.util.function.Consumer)}
	 *   (>=1.18) {@link EntityTrackingSection#forEach(net.minecraft.util.math.Box, java.util.function.Consumer)}
	 *
	 * For 1.17: looks like this is the method to collect objects in this chunk section based storage
	 */
	@Redirect(
			//#if MC >= 11800
			//$$ method = "forEach(Lnet/minecraft/util/math/Box;Ljava/util/function/Consumer;)V",
			//#else
			method = "forEach(Ljava/util/function/Predicate;Ljava/util/function/Consumer;)V",
			//#endif
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/world/entity/EntityTrackingSection;collection:Lnet/minecraft/util/collection/TypeFilterableList;"
			),
			require = 1
	)
	private TypeFilterableList<T> redirectEntitySections(EntityTrackingSection<T> section)
	{
		if (this.optimizedHHBECEnabled && OptimizedHardHitBoxEntityCollisionHelper.checkHardHitBoxEntityOnly.get())
		{
			return this.hardHitBoxEntitySections;
		}
		// vanilla
		return this.collection;
	}
}
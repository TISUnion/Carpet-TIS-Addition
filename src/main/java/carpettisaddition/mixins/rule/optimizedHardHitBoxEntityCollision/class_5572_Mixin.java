package carpettisaddition.mixins.rule.optimizedHardHitBoxEntityCollision;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.optimizedHardHitBoxEntityCollision.OptimizedHardHitBoxEntityCollisionHelper;
import net.minecraft.class_5572;
import net.minecraft.class_5584;
import net.minecraft.entity.Entity;
import net.minecraft.util.collection.TypeFilterableList;
import net.minecraft.util.math.Box;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(class_5572.class)
public abstract class class_5572_Mixin<T>
{
	// just like WorldChunk#entitySections in 1.16- but it's per chunk section and it uses genericity
	@Shadow @Final private TypeFilterableList<T> field_27248;

	private TypeFilterableList<T> hardHitBoxEntitySections;
	private boolean optimizedHHBECEnabled;  // optimizedHardHitBoxEntityCollisionEnabled

	@Inject(
			method = "<init>",
			at = @At("TAIL")
	)
	private void onConstruct(Class<T> class_, class_5584 arg, CallbackInfo ci)
	{
		this.hardHitBoxEntitySections = new TypeFilterableList<>(class_);
		// Enable only if @param class_ is Entity.class
		// See the return type of ServerWorld#method_31592() for the reason of using Entity.class as judgement
		this.optimizedHHBECEnabled = CarpetTISAdditionSettings.optimizedHardHitBoxEntityCollision && class_ == Entity.class;
	}

	@Inject(method = "method_31764", at = @At("TAIL"))
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

	@Inject(method = "method_31767", at = @At("TAIL"))
	private void onRemoveEntity(T entity, CallbackInfoReturnable<Boolean> ci)
	{
		if (this.optimizedHHBECEnabled)
		{
			this.hardHitBoxEntitySections.remove(entity);
		}
	}

	/**
	 * this method is used in {@link World#getOtherEntities(Entity, Box, Predicate)}
	 * which will be invoked in {@link EntityView#getEntityCollisions}
	 *
	 * For 1.17: looks like this is the method to collect objects in this chunk section based storage
	 */
	@Redirect(
			method = "method_31765",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/class_5572;field_27248:Lnet/minecraft/util/collection/TypeFilterableList;"
			),
			require = 1
	)
	private TypeFilterableList<T> redirectEntitySections(class_5572<T> cls)
	{
		if (this.optimizedHHBECEnabled && OptimizedHardHitBoxEntityCollisionHelper.checkHardHitBoxEntityOnly.get())
		{
			return this.hardHitBoxEntitySections;
		}
		// vanilla
		return this.field_27248;
	}
}

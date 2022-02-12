package carpettisaddition.mixins.rule.preciseEntityPlacement;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.preciseEntityPlacement.PreciseEntityPlacer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin<T extends Entity>
{
	private boolean shouldApplyPreciseEntityPlacement = false;

	@Inject(method = "spawnFromItemStack", at = @At("HEAD"))
	private void preciseEntityPlacementSetFlag(CallbackInfoReturnable<Entity> cir)
	{
		if (CarpetTISAdditionSettings.preciseEntityPlacement)
		{
			this.shouldApplyPreciseEntityPlacement = true;
		}
	}

	@Inject(method = "spawnFromItemStack", at = @At("TAIL"))
	private void preciseEntityPlacementRemoveFlag(CallbackInfoReturnable<Entity> cir)
	{
		this.shouldApplyPreciseEntityPlacement = false;
	}

	@ModifyVariable(
			method = "create(Lnet/minecraft/world/World;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/text/Text;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnType;ZZ)Lnet/minecraft/entity/Entity;",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/Entity;refreshPositionAndAngles(DDDFF)V",
					shift = At.Shift.AFTER
			)
	)
	private T preciseEntityPlacement(T entity, World world, @Nullable CompoundTag itemTag, @Nullable Text name, @Nullable PlayerEntity player, BlockPos pos, SpawnType spawnType, boolean alignPosition, boolean invertY)
	{
		// in case of multi-threading, there's an extra spawnEggTargetPos null check in
		// the method PreciseEntityPlacer#adjustEntityFromSpawnEgg,
		// so using non-thread-local flag field (shouldApplyPreciseEntityPlacement) is fine
		if (CarpetTISAdditionSettings.preciseEntityPlacement && this.shouldApplyPreciseEntityPlacement)
		{
			PreciseEntityPlacer.adjustEntityFromSpawnEgg(entity);
		}

		return entity;
	}
}

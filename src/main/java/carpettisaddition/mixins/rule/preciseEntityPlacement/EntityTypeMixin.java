package carpettisaddition.mixins.rule.preciseEntityPlacement;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.preciseEntityPlacement.PreciseEntityPlacer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin<T extends Entity>
{
	@ModifyVariable(
			method = "create(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/text/Text;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/Entity;refreshPositionAndAngles(DDDFF)V",
					shift = At.Shift.AFTER
			)
	)
	private T preciseEntityPlacement(T entity, ServerWorld serverWorld, @Nullable NbtCompound itemTag, @Nullable Text name, @Nullable PlayerEntity player, BlockPos pos, SpawnReason spawnReason, boolean alignPosition, boolean invertY)
	{
		// there's an extra spawnEggTargetPos null check in the method PreciseEntityPlacer#adjustEntityFromSpawnEgg
		// testing if the adjusting is required
		if (CarpetTISAdditionSettings.preciseEntityPlacement)
		{
			PreciseEntityPlacer.adjustEntityFromSpawnEgg(entity);
		}
		return entity;
	}
}

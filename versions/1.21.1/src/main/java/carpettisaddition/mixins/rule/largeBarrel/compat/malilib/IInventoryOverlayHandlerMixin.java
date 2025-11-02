/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpettisaddition.mixins.rule.largeBarrel.compat.malilib;

import carpettisaddition.CarpetTISAdditionMod;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.largeBarrel.LargeBarrelHelper;
import carpettisaddition.helpers.rule.largeBarrel.compat.malilib.LargeBarrelMasaModUtils;
import carpettisaddition.utils.ModIds;
import carpettisaddition.utils.ReflectionUtils;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(require = {
		// Added in malilib (sakura-ryoko's fork) version:
		//   1.21-0.21.6
		//   1.21.3-0.22.4
		//   1.21.4-0.23.1
		@Condition(value = ModIds.malilib, versionPredicates = {
				">=0.21.6 <0.22",
				">=0.22.4 <0.23",
				">=0.23.1",
		}),
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.21")
})
@Pseudo
@Mixin(targets = "fi.dy.masa.malilib.interfaces.IInventoryOverlayHandler")
public interface IInventoryOverlayHandlerMixin
{
	@SuppressWarnings("UnresolvedMixinReference")
	@ModifyExpressionValue(
			method = "requestBlockEntityAt(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lorg/apache/commons/lang3/tuple/Pair;",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"
			)
	)
	private BlockState requestTheOtherSideOfTheLargeBarrel(
			BlockState state,
			@Local(argsOnly = true) Level world,
			@Local(argsOnly = true) BlockPos pos
	)
	{
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			BlockPos otherPos = LargeBarrelHelper.getOtherPos(state, world, pos);
			if (otherPos != null)
			{
				// this.getDataSyncer().requestBlockEntity(world, otherPos);
				ReflectionUtils.invoker(this.getClass(), "getDataSyncer", m -> m.getParameterCount() == 0).
						ifPresentOrElse(
								invoker -> {
									Object dataSyncer = invoker.apply(this, new Object[]{});
									LargeBarrelMasaModUtils.invokeIDataSyncerRequestBlockEntity(dataSyncer, world, otherPos);
								},
								() -> CarpetTISAdditionMod.LOGGER.error("Failed to locate method IInventoryOverlayHandler#getDataSyncer for {}", this)
						);
			}
		}
		return state;
	}
}

/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
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

package carpettisaddition.mixins.carpet.tweaks.rule.tntRandomRange;

import carpet.CarpetSettings;
import carpettisaddition.helpers.carpet.tweaks.rule.tntRandomRange.TntRandomRangeHelper;
import carpettisaddition.helpers.carpet.tweaks.rule.tntRandomRange.WrappedRandom;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

//#if MC >= 12102
//$$ import net.minecraft.server.level.ServerLevel;
//$$ import net.minecraft.world.level.ServerExplosion;
//$$ import net.minecraft.core.BlockPos;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//$$ import java.util.List;
//#else
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

public abstract class ExplosionMixin
{
	// Wrap before cancellable HEAD injections can run the explosion calculation.
	@Mixin(
			//#if MC >= 12102
			//$$ value = ServerExplosion.class,
			//#else
			value = Explosion.class,
			//#endif
			priority = 800
	)
	public static abstract class WrapWorldRandom
	{
		@Shadow @Final
		//#if MC >= 12102
		//$$ private ServerLevel level;
		//#else
		private Level level;
		//#endif

		@Inject(
				//#if MC >= 12102
				//$$ method = "calculateExplodedPositions",
				//#else
				method = "explode",
				//#endif
				at = @At("HEAD")
		)
		private void wrapWorldRandom(
				//#if MC >= 12102
				//$$ CallbackInfoReturnable<List<BlockPos>> cir
				//#else
				CallbackInfo ci
				//#endif
		)
		{
			if (!this.level.isClientSide())
			{
				TntRandomRangeHelper.restoreWorldRandom(this.level);
				if (CarpetSettings.tntRandomRange >= 0)
				{
					((WorldAccessor)this.level).setRandom(WrappedRandom.wrap(this.level.getRandom()));
				}
			}
		}
	}

	// Restore after Lithium's block raycast, which runs at RETURN with priority 1000.
	@Mixin(
			//#if MC >= 12102
			//$$ value = ServerExplosion.class,
			//#else
			value = Explosion.class,
			//#endif
			priority = 1100
	)
	public static abstract class RestoreWorldRandom
	{
		@Shadow @Final
		//#if MC >= 12102
		//$$ private ServerLevel level;
		//#else
		private Level level;
		//#endif

		@Inject(
				//#if MC >= 12102
				//$$ method = "calculateExplodedPositions",
				//#else
				method = "explode",
				//#endif
				at = @At("RETURN")
		)
		private void restoreWorldRandom(
				//#if MC >= 12102
				//$$ CallbackInfoReturnable<List<BlockPos>> cir
				//#else
				CallbackInfo ci
				//#endif
		)
		{
			TntRandomRangeHelper.restoreWorldRandom(this.level);
		}
	}
}

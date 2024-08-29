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
import carpettisaddition.helpers.carpet.tweaks.rule.tntRandomRange.WrappedRandom;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

//#if MC >= 12102
//$$ import net.minecraft.server.world.ServerWorld;
//$$ import net.minecraft.world.explosion.ExplosionImpl;
//$$ import net.minecraft.util.math.BlockPos;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//$$ import java.util.List;
//#else
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

// increase priority to make the wrapWorldRandom injection be in front of other cancellable injections
@Mixin(
		//#if MC >= 12102
		//$$ value = ExplosionImpl.class,
		//#else
		value = Explosion.class,
		//#endif
		priority = 800
)
public class ExplosionMixin
{
	@Shadow @Final
	//#if MC >= 12102
	//$$ private ServerWorld world;
	//#else
	private World world;
	//#endif

	private void tryUnWrapWorldRandom()
	{
		if (this.world.random instanceof WrappedRandom)
		{
			((WorldAccessor)this.world).setRandom(((WrappedRandom)this.world.random).unwrap());
		}
	}

	@Inject(
			//#if MC >= 12102
			//$$ method = "getBlocksToDestroy",
			//#else
			method = "collectBlocksAndDamageEntities",
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
		// Do wrapping thing server side only
		if (!this.world.isClient())
		{
			this.tryUnWrapWorldRandom();
			if (CarpetSettings.tntRandomRange >= 0)
			{
				((WorldAccessor) this.world).setRandom(WrappedRandom.wrap(this.world.random));
			}
		}
	}

	@Inject(
			//#if MC >= 12102
			//$$ method = "getBlocksToDestroy",
			//#else
			method = "collectBlocksAndDamageEntities",
			//#endif
			at = @At("RETURN")
	)
	private void unwrapWorldRandom(
			//#if MC >= 12102
			//$$ CallbackInfoReturnable<List<BlockPos>> cir
			//#else
			CallbackInfo ci
			//#endif
	)
	{
		this.tryUnWrapWorldRandom();
	}
}

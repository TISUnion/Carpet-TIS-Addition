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

package carpettisaddition.mixins.carpet.tweaks.bugfixes.scarpetShapeReplacementParticle;

import carpet.script.utils.ShapeDispatcher;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 12102
//$$ import net.minecraft.util.math.ColorHelper;
//#else
import org.joml.Vector3f;
//#endif

/**
 * impl in mc [1.20.5, 1.21.4]
 * see subproject 1.20.6
 */
@Restriction(require = {
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.20.5"),
		@Condition(value = ModIds.carpet, versionPredicates = "<1.4.169"),
})
@Mixin(ShapeDispatcher.ExpiringShape.class)
public abstract class ShapeDispatcherExpiringShapeMixin
{
	@Shadow(remap = false) protected float r;
	@Shadow(remap = false) protected float fa;
	@Shadow(remap = false) protected float fr;

	/**
	 * The particle command argument changes its syntax in mc1.20.5,
	 * but fabric-carpet still uses the old syntax until its v1.4.169 (1.21.5-rc1) release
	 * at <a href="https://github.com/gnembon/fabric-carpet/commit/da5b937e78c949ecea743cf607fb3d31249b48e6">da5b937</a>
	 */
	@Inject(
			method = "replacementParticle",
			at = @At("HEAD"),
			cancellable = true
	)
	private void dontUseCommandArgumentParsingWithOutdatedInputOrItWillCrash(DynamicRegistryManager regs, CallbackInfoReturnable<ParticleEffect> cir)
	{
		boolean bg = fa == 0;
		cir.setReturnValue(new DustParticleEffect(
				//#if MC >= 12102
				//$$ ColorHelper.fromFloats(1.0f, bg ? r : fr, bg ? r : fr, bg ? r : fr),
				//#else
				new Vector3f(bg ? r : fr, bg ? r : fr, bg ? r : fr),
				//#endif
				1
		));
	}
}

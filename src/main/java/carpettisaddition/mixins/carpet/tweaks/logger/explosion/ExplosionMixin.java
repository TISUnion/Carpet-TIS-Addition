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

package carpettisaddition.mixins.carpet.tweaks.logger.explosion;

import carpettisaddition.helpers.carpet.tweaks.logger.explosion.ExplosionLogHelperWithEntity;
import carpettisaddition.utils.ReflectionUtils;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 12102
//$$ import net.minecraft.world.explosion.ExplosionImpl;
//#else
import net.minecraft.world.explosion.Explosion;
//#endif

/**
 * Inject after than fabric carpet's mixin
 *   <1.4.59: {@link carpet.mixins.ExplosionMixin}
 *   >=1.4.59: {@link carpet.mixins.Explosion_optimizedTntMixin}
 * So the eLogger field is constructed
 */
@Mixin(
		//#if MC >= 12102
		//$$ value = ExplosionImpl.class,
		//#else
		value = Explosion.class,
		//#endif
		priority = 2000
)
public abstract class ExplosionMixin
{
	@Shadow @Final private Entity entity;

	@Inject(
			//#if MC >= 12102
			//$$ method = "<init>",
			//#elseif MC >= 12005
			//$$ method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/explosion/Explosion$DestructionType;Lnet/minecraft/particle/ParticleEffect;Lnet/minecraft/particle/ParticleEffect;Lnet/minecraft/registry/entry/RegistryEntry;)V",
			//#elseif MC >= 12003
			//$$ method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/explosion/Explosion$DestructionType;Lnet/minecraft/particle/ParticleEffect;Lnet/minecraft/particle/ParticleEffect;Lnet/minecraft/sound/SoundEvent;)V",
			//#elseif MC >= 11600
			//$$ method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/explosion/Explosion$DestructionType;)V",
			//#else
			method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;DDDFZLnet/minecraft/world/explosion/Explosion$DestructionType;)V",
			//#endif
			at = @At("TAIL")
	)
	private void hackCarpetExplosionLogHelper(CallbackInfo ci)
	{
		ReflectionUtils.getField(this, "eLogger").ifPresent(eLogger -> {
			if (eLogger instanceof ExplosionLogHelperWithEntity)
			{
				((ExplosionLogHelperWithEntity)eLogger).setEntity$TISCM(this.entity);
			}
		});
	}
}

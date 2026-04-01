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

package carpettisaddition.mixins.command.lifetime.spawning.blockdrop;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//#if MC >= 11700
//$$ import carpettisaddition.commands.lifetime.LifeTimeTracker;
//$$ import carpettisaddition.commands.lifetime.utils.LifetimeMixinUtil;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

@Mixin(Block.class)
public abstract class BlockMixin
{
	@ModifyArg(
			//#if MC >= 11700
			//$$ method = "popResource(Lnet/minecraft/world/level/Level;Ljava/util/function/Supplier;Lnet/minecraft/world/item/ItemStack;)V",
			//#else
			method = "popResource",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
			)
	)
	private static Entity lifetimeTracker_recordSpawning_blockDrop_commonItem(Entity itemEntity)
	{
		((LifetimeTrackerTarget)itemEntity).recordSpawning$TISCM(LiteralSpawningReason.BLOCK_DROP);
		return itemEntity;
	}

	//#if MC >= 11700
	//$$ @Inject(method = "popExperience", at = @At("HEAD"))
	//$$ private void lifetimeTracker_recordSpawning_blockDrop_commonXpOrbStart(CallbackInfo ci)
	//$$ {
	//$$ 	if (LifeTimeTracker.isActivated()) LifetimeMixinUtil.xpOrbSpawningReason.set(LiteralSpawningReason.BLOCK_DROP);
	//$$ }
	//$$
	//$$ @Inject(method = "popExperience", at = @At("TAIL"))
	//$$ private void lifetimeTracker_recordSpawning_blockDrop_commonXpOrbEnd(CallbackInfo ci)
	//$$ {
	//$$ 	if (LifeTimeTracker.isActivated()) LifetimeMixinUtil.xpOrbSpawningReason.remove();
	//$$ }
	//#else
	@ModifyArg(
			method = "popExperience",
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.16
					//$$ target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
					//#else
					target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
					//#endif
			)
	)
	private static Entity lifetimeTracker_recordSpawning_blockDrop_commonXpOrb(Entity xpEntity)
	{
		((LifetimeTrackerTarget)xpEntity).recordSpawning$TISCM(LiteralSpawningReason.BLOCK_DROP);
		return xpEntity;
	}
	//#endif
}

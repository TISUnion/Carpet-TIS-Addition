/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.optimizedFastEntityMovement.compat.lithium;

import carpettisaddition.helpers.rule.optimizedFastEntityMovement.OFEMContext;
import carpettisaddition.helpers.rule.optimizedFastEntityMovement.OFEMUtil;
import carpettisaddition.utils.ModIds;
import carpettisaddition.utils.mixin.testers.LithiumEntityMovementOptimizationTester;
import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.jellysquid.mods.lithium.common.entity.movement.ChunkAwareBlockCollisionSweeper;
import me.jellysquid.mods.lithium.common.util.collections.LazyList;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Collections;

/**
 * Lithium's optimization `entity.collisions.movement` uses default priority 1000
 * We need to mixin into its merged static method, so here comes priority 2000
 */
@Restriction(require = {
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.21"),
		@Condition(value = ModIds.lithium, versionPredicates = ">=0.12.5"),
		@Condition(type = Condition.Type.TESTER, tester = LithiumEntityMovementOptimizationTester.class)
})
@Mixin(value = Entity.class, priority = 2000)
public abstract class EntityMixin
{
	@Unique
	private static final LazyList<VoxelShape> EMPTY_BLOCK_COLLECTIONS = new LazyList<>(Lists.newArrayList(), Collections.emptyIterator());

	@Dynamic("Should be added by lithium entity.collisions.movement")
	@ModifyExpressionValue(
			method = "lithium$CollideMovement",
			at = @At(
					value = "NEW",
					target = "(Ljava/util/ArrayList;Ljava/util/Iterator;)Lme/jellysquid/mods/lithium/common/util/collections/LazyList;",
					remap = false
			),
			remap = false
	)
	private static LazyList<VoxelShape> dontUseThatLargeBlockCollisions(
			LazyList<VoxelShape> originalList,
			/* parent method parameters -> */
			@Local(argsOnly = true) World world,
			@Local(argsOnly = true) @Nullable Entity entity,
			@Local(argsOnly = true) Vec3d movement,
			@Share("OFEMContext") LocalRef<OFEMContext> context
	)
	{
		OFEMContext ctx = OFEMUtil.checkAndCreateContext(world, entity, movement);
		context.set(ctx);
		if (ctx != null)
		{
			return EMPTY_BLOCK_COLLECTIONS;
		}
		// vanilla lithium
		return originalList;
	}

	@Dynamic("Should be added by lithium entity.collisions.movement")
	@ModifyArgs(
			method = "lithium$CollideMovement",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/shape/VoxelShapes;calculateMaxOffset(Lnet/minecraft/util/math/Direction$Axis;Lnet/minecraft/util/math/Box;Ljava/lang/Iterable;D)D",
					remap = true
			),
			remap = false
	)
	private static void useAxisOnlyBlockCollisions(Args args, @Share("OFEMContext") LocalRef<OFEMContext> context)
	{
		OFEMContext ctx = context.get();
		if (ctx == null)
		{
			return;
		}

		Direction.Axis axis = args.get(0);
		Box entityBoundingBox = args.get(1);
		Iterable<VoxelShape> blockCollisions = args.get(2);
		double maxDist = args.get(3);

		ctx.axis = axis;
		ctx.movementOnAxis = maxDist;
		ctx.entityBoundingBox = entityBoundingBox;

		// make sure now we are calculating max offset via blockCollisions
		// we don't want to touch the world border stuff
		if (blockCollisions == EMPTY_BLOCK_COLLECTIONS)
		{
			var newList = OFEMUtil.getAxisOnlyBlockCollision(ctx, (world, entity, box) -> {
				// ref: me.jellysquid.mods.lithium.mixin.entity.collisions.movement.EntityMixin#lithium$CollideMovement
				// Don't need hide the last collision here, cuz the collision order with rule on is already not that vanilla XD
				var blockCollisionSweeper = new ChunkAwareBlockCollisionSweeper(world, entity, box, false);
				return new LazyList<>(Lists.newArrayList(), blockCollisionSweeper);
			});
			args.set(2, newList);
		}
	}
}
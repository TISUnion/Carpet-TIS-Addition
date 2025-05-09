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

package carpettisaddition.mixins.rule.optimizedFastEntityMovement.compat.lithium;

import carpettisaddition.helpers.rule.optimizedFastEntityMovement.OFEMContext;
import carpettisaddition.helpers.rule.optimizedFastEntityMovement.OFEMUtil;
import carpettisaddition.utils.ModIds;
import carpettisaddition.utils.mixin.testers.LithiumEntityMovementOptimizationTester;
import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.jellysquid.mods.lithium.common.entity.LithiumEntityCollisions;
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

import java.util.List;

//#if MC < 11900
import net.minecraft.world.CollisionView;
//#endif

/**
 * mc1.14 ~ mc1.17.x: subproject 1.15.2 (main project)
 * mc1.18 ~ mc1.20.x: subproject 1.18.2        <--------
 * mc1.21+          : subproject 1.21.1
 * <p>
 * Lithium's optimization `entity.collisions.movement` uses default priority 1000
 * We need to mixin into its merged static method, so here comes priority 2000
 */
@Restriction(require = {
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"),
		@Condition(type = Condition.Type.TESTER, tester = LithiumEntityMovementOptimizationTester.class)
})
@Mixin(value = Entity.class, priority = 2000)
public abstract class EntityMixin
{
	@Unique
	private static final List<VoxelShape> EMPTY_BLOCK_COLLECTIONS = Lists.newArrayList();

	@Dynamic("Should be added by lithium entity.collisions.movement")
	@WrapOperation(
			method = "lithiumCollideMultiAxisMovement",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11900
					//$$ target = "Lme/jellysquid/mods/lithium/common/entity/LithiumEntityCollisions;getBlockCollisions(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/List;",
					//#else
					target = "Lme/jellysquid/mods/lithium/common/entity/LithiumEntityCollisions;getBlockCollisions(Lnet/minecraft/world/CollisionView;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/List;",
					//#endif
					remap = true
			),
			remap = false
	)
	private static List<VoxelShape> dontUseThatLargeBlockCollisions(
			// lithium 0.11.1 changed the world param type from CollisionView to World
			//#if MC >= 11900
			//$$ World world,
			//#else
			CollisionView world,
			//#endif
			Entity entity, Box box, Operation<List<VoxelShape>> original,
			/* parent method parameters -> */
			@Nullable Entity entityParam, Vec3d movement, Box entityBoundingBox, World worldParam,
			@Share("OFEMContext") LocalRef<OFEMContext> context
	)
	{
		OFEMContext ctx = OFEMUtil.checkAndCreateContext((World)world, entity, movement);
		context.set(ctx);
		if (ctx != null)
		{
			return EMPTY_BLOCK_COLLECTIONS;
		}
		// vanilla lithium
		return original.call(world, entity, box);
	}

	@Dynamic("Should be added by lithium entity.collisions.movement")
	@ModifyArgs(
			method = "lithiumCollideMultiAxisMovement",
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
			args.set(2, OFEMUtil.getAxisOnlyBlockCollision(ctx, LithiumEntityCollisions::getBlockCollisions));
		}
	}
}
/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.mixins.carpet.shape;

import carpet.script.utils.ShapeDispatcher;
import carpet.script.value.Value;
import carpettisaddition.helpers.carpet.shape.ShapeDispatcherPreciseExpiringShape;
import carpettisaddition.helpers.carpet.shape.ShapeUtil;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

//#if MC >= 1.20.5
//$$ import net.minecraft.core.RegistryAccess;
//#endif

//#if MC < 1.17
import org.apache.commons.lang3.tuple.Pair;
//#endif

/**
 * mc >= 1.15: subproject 1.15.2 (main project)  <--------
 * mc <  1.15: subproject 1.14.4
 */
@Mixin(ShapeDispatcher.class)
public abstract class ShapeDispatcherMixin
{
	@Inject(method = "fromTag", at = @At("HEAD"), remap = false)
	private static void readPrecision(
			CallbackInfoReturnable<ShapeDispatcher.ExpiringShape> cir,
			@Local(argsOnly = true) LocalRef<CompoundTag> tag,
			@Share("precise") LocalBooleanRef precise
	)
	{
		CompoundTag originalTag = tag.get();
		if (originalTag.contains(ShapeDispatcherPreciseExpiringShape.PRECISE_NBT_KEY))
		{
			precise.set(ByteTag.valueOf(true).equals(originalTag.get(ShapeDispatcherPreciseExpiringShape.PRECISE_NBT_KEY)));
			CompoundTag copy = originalTag.copy();
			copy.remove(ShapeDispatcherPreciseExpiringShape.PRECISE_NBT_KEY);
			tag.set(copy);
		}
	}

	@ModifyReturnValue(method = "fromTag", at = @At("RETURN"), remap = false)
	private static ShapeDispatcher.ExpiringShape applyPrecision(ShapeDispatcher.ExpiringShape shape, @Share("precise") LocalBooleanRef precise)
	{
		if (shape != null)
		{
			((ShapeDispatcherPreciseExpiringShape)shape).setPrecise$TISCM(precise.get());
		}
		return shape;
	}

	@WrapOperation(
			method = "sendShape",
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.20.5
					//$$ target = "Lcarpet/script/utils/ShapeDispatcher$ExpiringShape;toTag(Ljava/util/Map;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/nbt/CompoundTag;"
					//#else
					target = "Lcarpet/script/utils/ShapeDispatcher$ExpiringShape;toTag(Ljava/util/Map;)Lnet/minecraft/nbt/CompoundTag;"
					//#endif
			)
	)
	private static CompoundTag writePrecision(Map<String, Value> params,
			//#if MC >= 1.20.5
			//$$ RegistryAccess registryAccess,
			//#endif
			Operation<CompoundTag> original,
			//#if MC >= 1.17
			//$$ @Local ShapeDispatcher.ShapeWithConfig shape
			//#else
			@Local Pair<ShapeDispatcher.ExpiringShape, Map<String, Value>> shape
			//#endif
	)
	{
		CompoundTag tag = original.call(params
				//#if MC >= 1.20.5
				//$$ , registryAccess
				//#endif
		);
		ShapeDispatcher.ExpiringShape expiringShape =
				//#if MC >= 1.17
				//$$ shape.shape();
				//#else
				shape.getLeft();
				//#endif
		if (((ShapeDispatcherPreciseExpiringShape)expiringShape).isPrecise$TISCM())
		{
			tag.putBoolean(ShapeDispatcherPreciseExpiringShape.PRECISE_NBT_KEY, true);
		}
		return tag;
	}

	//#if MC < 1.19.4
	// Match the shape send lambdas without depending on compiler-generated names.
	@WrapOperation(
			method = "*",
			at = @At(
					value = "INVOKE",
					target = "Lcarpet/network/ServerNetworkHandler;sendCustomCommand(Lnet/minecraft/server/level/ServerPlayer;Ljava/lang/String;Lnet/minecraft/nbt/Tag;)V"
			)
	)
	private static void prepareShapesForPlayer(ServerPlayer player, String command, Tag data, Operation<Void> original)
	{
		original.call(player, command, ShapeUtil.prepareShapesForPlayer(player, (ListTag)data));
	}
	//#endif
}

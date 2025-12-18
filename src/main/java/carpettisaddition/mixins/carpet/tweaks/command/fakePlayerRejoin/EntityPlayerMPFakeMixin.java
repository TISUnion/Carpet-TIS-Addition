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

package carpettisaddition.mixins.carpet.tweaks.command.fakePlayerRejoin;

import carpet.patches.EntityPlayerMPFake;
import carpettisaddition.helpers.carpet.tweaks.command.fakePlayerRejoin.FakePlayerRejoinHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 12104
//$$ import java.util.function.BiConsumer;
//#endif

//#if MC >= 12002
//$$ import org.spongepowered.asm.mixin.injection.ModifyArg;
//$$ import java.util.function.Consumer;
//#endif

//#if MC >= 11600
//$$ import net.minecraft.server.level.ServerLevel;
//#else
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
//#endif

@Mixin(EntityPlayerMPFake.class)
public abstract class EntityPlayerMPFakeMixin
{
	@WrapWithCondition(
			//#if MC >= 26.1
			//$$ method = "lambda$createFake$0",
			//#elseif MC >= 12002
			//$$ method = "lambda$createFake$2",
			//#else
			method = "createFake",
			//#endif
			at = @At(
					value = "FIELD",
					target = "Lcarpet/patches/EntityPlayerMPFake;fixStartingPosition:Ljava/lang/Runnable;",
					remap = false
			)
	)
	private static boolean fakePlayerRejoin_disableFixerOnRejoin(EntityPlayerMPFake instance, Runnable newValue)
	{
		return !FakePlayerRejoinHelper.isRejoin.get();
	}

	//#if MC >= 12002
	//$$ @ModifyArg(
	//$$ 		method = "createFake",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				//#if MC >= 12104
	//$$ 				//$$ target = "Ljava/util/concurrent/CompletableFuture;whenCompleteAsync(Ljava/util/function/BiConsumer;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;",
	//$$ 				//#elseif MC >= 12005
	//$$ 				//$$ target = "Ljava/util/concurrent/CompletableFuture;thenAcceptAsync(Ljava/util/function/Consumer;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;",
	//$$ 				//#else
	//$$ 				target = "Ljava/util/concurrent/CompletableFuture;thenAccept(Ljava/util/function/Consumer;)Ljava/util/concurrent/CompletableFuture;",
	//$$ 				//#endif
	//$$ 				remap = false
	//$$ 		),
	//$$ 		remap = true
	//$$ )
	//$$ //#if MC >= 12104
	//$$ //$$ private static <T> BiConsumer<? super T, ? super Throwable> fakePlayerRejoin_forwardTheFlag(BiConsumer<? super T, ? super Throwable> action)
	//$$ //#else
	//$$ private static <T> Consumer<? super T> fakePlayerRejoin_forwardTheFlag(Consumer<? super T> action)
	//$$ //#endif
	//$$ {
	//$$ 	boolean isRejoin = FakePlayerRejoinHelper.isRejoin.get();
	//$$ 	//#if MC >= 12104
	//$$ 	//$$ return (value, t) -> {
	//$$ 	//#else
	//$$ 	return value -> {
	//$$ 	//#endif
	//$$ 		if (isRejoin)
	//$$ 		{
	//$$ 			FakePlayerRejoinHelper.isRejoin.set(true);
	//$$ 		}
	//$$ 		try
	//$$ 		{
	//$$ 			//#if MC >= 12104
	//$$ 			//$$ action.accept(value, t);
	//$$ 			//#else
	//$$ 			action.accept(value);
	//$$ 			//#endif
	//$$ 		}
	//$$ 		finally
	//$$ 		{
	//$$ 			FakePlayerRejoinHelper.isRejoin.remove();
	//$$ 		}
	//$$ 	};
	//$$ }
	//#endif

	//#if MC <= 11500
	//$$ @ModifyExpressionValue(
	//$$ 		method = "createFake",
	//$$ 		at = @At(
	//$$ 				value = "FIELD",
	//$$ 				target = "Lcarpet/patches/EntityPlayerMPFake;dimension:Lnet/minecraft/world/level/dimension/DimensionType;",
	//$$ 				ordinal = 0
	//$$ 		)
	//$$ )
	//$$ private static DimensionType fakePlayerRejoin_dontDoTransdimensionTeleport(
	//$$ 		DimensionType playerDimension,
	//$$ 		@Local(argsOnly = true) DimensionType targetDimension
	//$$ )
	//$$ {
	//$$ 	if (FakePlayerRejoinHelper.isRejoin.get())
	//$$ 	{
	//$$ 		playerDimension = targetDimension;
	//$$ 	}
	//$$ 	return playerDimension;
	//$$ }
	//#endif

	@WrapWithCondition(
			//#if MC >= 26.1
			//$$ method = "lambda$createFake$0",
			//#elseif MC >= 12002
			//$$ method = "lambda$createFake$2",
			//#else
			method = "createFake",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 12102
					//$$ target = "Lcarpet/patches/EntityPlayerMPFake;teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FFZ)Z"
					//#elseif MC >= 11600
					//$$ target = "Lcarpet/patches/EntityPlayerMPFake;teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V"
					//#else
					target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;teleport(DDDFF)V"
					//#endif
			)
	)
	private static boolean fakePlayerRejoin_dontRequestTeleport(
			//#if MC >= 12102
			//$$ EntityPlayerMPFake instance, ServerLevel serverWorld, double x, double y, double z, java.util.Set<?> set, float yaw, float pitch, boolean resetCamera
			//#elseif MC >= 11600
			//$$ EntityPlayerMPFake instance, ServerLevel serverWorld, double x, double y, double z, float yaw, float pitch
			//#else
			ServerGamePacketListenerImpl instance, double x, double y, double z, float yaw, float pitch
			//#endif
	)
	{
		return !FakePlayerRejoinHelper.isRejoin.get();
	}
}

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

//#if MC >= 12002
//$$ import org.spongepowered.asm.mixin.injection.ModifyArg;
//$$ import java.util.function.Consumer;
//#endif

//#if MC >= 11600
//$$ import net.minecraft.server.world.ServerWorld;
//#else
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.server.network.ServerPlayNetworkHandler;
//#endif

@Mixin(EntityPlayerMPFake.class)
public abstract class EntityPlayerMPFakeMixin
{
	@WrapWithCondition(
			//#if MC >= 12002
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
	//$$ 				//#if MC >= 12005
	//$$ 				//$$ target = "Ljava/util/concurrent/CompletableFuture;thenAcceptAsync(Ljava/util/function/Consumer;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;",
	//$$ 				//#else
	//$$ 				target = "Ljava/util/concurrent/CompletableFuture;thenAccept(Ljava/util/function/Consumer;)Ljava/util/concurrent/CompletableFuture;",
	//$$ 				//#endif
	//$$ 				remap = false
	//$$ 		),
	//$$ 		remap = true
	//$$ )
	//$$ private static <T> Consumer<? super T> fakePlayerRejoin_forwardTheFlag(Consumer<? super T> action)
	//$$ {
	//$$ 	boolean isRejoin = FakePlayerRejoinHelper.isRejoin.get();
	//$$ 	return value -> {
	//$$ 		if (isRejoin)
	//$$ 		{
	//$$ 			FakePlayerRejoinHelper.isRejoin.set(true);
	//$$ 		}
	//$$ 		try
	//$$ 		{
	//$$ 			action.accept(value);
	//$$ 		}
	//$$ 		finally
	//$$ 		{
	//$$ 			FakePlayerRejoinHelper.isRejoin.remove();
	//$$ 		}
	//$$ 	};
	//$$ }
	//#endif

	//#if MC <= 11500
	@ModifyExpressionValue(
			method = "createFake",
			at = @At(
					value = "FIELD",
					target = "Lcarpet/patches/EntityPlayerMPFake;dimension:Lnet/minecraft/world/dimension/DimensionType;",
					ordinal = 0
			)
	)
	private static DimensionType fakePlayerRejoin_dontDoTransdimensionTeleport(
			DimensionType playerDimension,
			@Local(argsOnly = true) DimensionType targetDimension
	)
	{
		if (FakePlayerRejoinHelper.isRejoin.get())
		{
			playerDimension = targetDimension;
		}
		return playerDimension;
	}
	//#endif

	@WrapWithCondition(
			//#if MC >= 12002
			//$$ method = "lambda$createFake$2",
			//#else
			method = "createFake",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 12102
					//$$ target = "Lcarpet/patches/EntityPlayerMPFake;teleport(Lnet/minecraft/server/world/ServerWorld;DDDFFZ)V"
					//#elseif MC >= 11600
					//$$ target = "Lcarpet/patches/EntityPlayerMPFake;teleport(Lnet/minecraft/server/world/ServerWorld;DDDFF)V"
					//#else
					target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;requestTeleport(DDDFF)V"
					//#endif
			)
	)
	private static boolean fakePlayerRejoin_dontRequestTeleport(
			//#if MC >= 11600
			//$$ EntityPlayerMPFake instance, ServerWorld serverWorld, double x, double y, double z, float yaw, float pitch
			//$$ //#if MC >= 12102
			//$$ //$$ , boolean resetCamera
			//$$ //#endif
			//#else
			ServerPlayNetworkHandler instance, double x, double y, double z, float yaw, float pitch
			//#endif
	)
	{
		return !FakePlayerRejoinHelper.isRejoin.get();
	}
}

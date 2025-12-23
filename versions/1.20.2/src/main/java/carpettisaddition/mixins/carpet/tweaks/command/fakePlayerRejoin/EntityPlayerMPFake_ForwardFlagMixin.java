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

package carpettisaddition.mixins.carpet.tweaks.command.fakePlayerRejoin;

import carpet.patches.EntityPlayerMPFake;
import carpettisaddition.helpers.carpet.tweaks.command.fakePlayerRejoin.FakePlayerRejoinHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//#if MC >= 12104
//$$ import java.util.function.BiConsumer;
//#else
import java.util.function.Consumer;
//#endif

@Mixin(EntityPlayerMPFake.class)
public abstract class EntityPlayerMPFake_ForwardFlagMixin
{
	@ModifyArg(
			method = "createFake",
			at = @At(
					value = "INVOKE",
					//#if MC >= 12104
					//$$ target = "Ljava/util/concurrent/CompletableFuture;whenCompleteAsync(Ljava/util/function/BiConsumer;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;",
					//#elseif MC >= 12005
					//$$ target = "Ljava/util/concurrent/CompletableFuture;thenAcceptAsync(Ljava/util/function/Consumer;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;",
					//#else
					target = "Ljava/util/concurrent/CompletableFuture;thenAccept(Ljava/util/function/Consumer;)Ljava/util/concurrent/CompletableFuture;",
					//#endif
					remap = false
			),
			remap = true
	)
	//#if MC >= 12104
	//$$ private static <T> BiConsumer<? super T, ? super Throwable> fakePlayerRejoin_forwardTheFlag(BiConsumer<? super T, ? super Throwable> action)
	//#else
	private static <T> Consumer<? super T> fakePlayerRejoin_forwardTheFlag(Consumer<? super T> action)
	//#endif
	{
		boolean isRejoin = FakePlayerRejoinHelper.isRejoin.get();
		//#if MC >= 12104
		//$$ return (value, t) -> {
		//#else
		return value -> {
			//#endif
			if (isRejoin)
			{
				FakePlayerRejoinHelper.isRejoin.set(true);
			}
			try
			{
				//#if MC >= 12104
				//$$ action.accept(value, t);
				//#else
				action.accept(value);
				//#endif
			}
			finally
			{
				FakePlayerRejoinHelper.isRejoin.remove();
			}
		};
	}
}

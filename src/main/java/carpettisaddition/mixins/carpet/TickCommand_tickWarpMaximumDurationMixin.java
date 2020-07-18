package carpettisaddition.mixins.carpet;

import carpet.commands.TickCommand;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(TickCommand.class)
public abstract class TickCommand_tickWarpMaximumDurationMixin
{
	@Redirect(
			method = "register",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "intValue=4000000"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/arguments/IntegerArgumentType;integer(II)Lcom/mojang/brigadier/arguments/IntegerArgumentType;",
					ordinal = 0
			),
			remap = false
	)
	private static IntegerArgumentType restrictInRange(int min, int max)
	{
		return IntegerArgumentType.integer(min, Integer.MAX_VALUE);
	}

}

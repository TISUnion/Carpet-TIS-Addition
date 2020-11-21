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
			// should be accurate enough
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "stringValue=warp",
							ordinal = 0
					),
					to = @At(
							value = "CONSTANT",
							args = "stringValue=tail command",
							ordinal = 0
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/arguments/IntegerArgumentType;integer(II)Lcom/mojang/brigadier/arguments/IntegerArgumentType;",
					ordinal = 0
			),
			require = 0,
			remap = false
	)
	private static IntegerArgumentType restrictInRange(int min, int max)
	{
		// fabric carpet removed the 4000000 upper limit in version 1.4.18
		// so here's an extra check to make sure it's what we want
		if (min == 0 && max == 4000000)
		{
			max = Integer.MAX_VALUE;
		}
		return IntegerArgumentType.integer(min, max);
	}

}

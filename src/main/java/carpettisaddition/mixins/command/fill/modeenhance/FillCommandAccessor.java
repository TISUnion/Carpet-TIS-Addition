package carpettisaddition.mixins.command.fill.modeenhance;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.command.arguments.BlockStateArgument;
import net.minecraft.server.command.FillCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockBox;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Predicate;

@Mixin(FillCommand.class)
public interface FillCommandAccessor
{
	@Invoker
	static int invokeExecute(ServerCommandSource source, BlockBox range, BlockStateArgument block, FillCommand.Mode mode, @Nullable Predicate<CachedBlockPosition> filter) throws CommandSyntaxException
	{
		throw new RuntimeException();
	}
}

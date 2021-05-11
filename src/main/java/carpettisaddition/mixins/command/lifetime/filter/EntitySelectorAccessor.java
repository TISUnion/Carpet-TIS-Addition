package carpettisaddition.mixins.command.lifetime.filter;

import carpettisaddition.commands.lifetime.filter.IEntitySelector;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.predicate.NumberRange;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(EntitySelector.class)
public interface EntitySelectorAccessor extends IEntitySelector
{
	@Accessor
	boolean getIncludesNonPlayers();

	@Accessor
	boolean getLocalWorldOnly();

	@Accessor
	Predicate<Entity> getBasePredicate();

	@Accessor
	NumberRange.FloatRange getDistance();

	@Accessor
	Function<Vec3d, Vec3d> getPositionOffset();

	@Nullable
	@Accessor
	Box getBox();

	@Accessor
	boolean getSenderOnly();

	@Nullable
	@Accessor
	String getPlayerName();

	@Nullable
	@Accessor
	UUID getUuid();

	@Nullable
	@Accessor
	EntityType<?> getType();

	@Accessor
	boolean getUsesAt();

	@Invoker
	Predicate<Entity> invokeGetPositionPredicate(Vec3d vec3d);
}

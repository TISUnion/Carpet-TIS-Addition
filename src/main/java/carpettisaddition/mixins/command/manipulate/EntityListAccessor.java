package carpettisaddition.mixins.command.manipulate;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.world.EntityList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityList.class)
public interface EntityListAccessor
{
	@Invoker
	void invokeEnsureSafe();

	@Accessor
	Int2ObjectMap<Entity> getEntities();
}

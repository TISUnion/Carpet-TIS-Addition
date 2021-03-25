package carpettisaddition.mixins.logger.gameevent;

import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameEvent.class)
public interface GameEventAccessor
{
    @Accessor("id")
    public String getId();
}

package carpettisaddition.mixins.command;

import net.minecraft.village.raid.Raid;
import net.minecraft.village.raid.RaidManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;


@Mixin(RaidManager.class)
public interface RaidManagerAccessor
{
	@Accessor
	public Map<Integer, Raid> getRaids();
}

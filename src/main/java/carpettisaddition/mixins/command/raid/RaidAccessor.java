package carpettisaddition.mixins.command.raid;

import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.village.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.Set;


@Mixin(Raid.class)
public interface RaidAccessor
{
	@Accessor
	public int getWavesSpawned();

	@Accessor
	public int getWaveCount();

	@Accessor
	public Raid.Status getStatus();

	@Accessor
	public Map<Integer, RaiderEntity> getWaveToCaptain();

	@Accessor
	public Map<Integer, Set<RaiderEntity>> getWaveToRaiders();
}

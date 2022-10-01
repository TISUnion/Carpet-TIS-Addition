package carpettisaddition.mixins.rule.instantBlockUpdaterReintroduced;

import carpettisaddition.helpers.rule.instantBlockUpdaterReintroduced.NeighborUpdaterChangeableWorld;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.World;
import net.minecraft.world.block.NeighborUpdater;
import net.minecraft.world.block.SimpleNeighborUpdater;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
@Mixin(World.class)
public abstract class WorldMixin implements NeighborUpdaterChangeableWorld
{
	@Mutable @Shadow @Final protected NeighborUpdater neighborUpdater;

	private NeighborUpdater previousNeighborUpdater$TISCM = null;
	private boolean usingInstantNeighborUpdater$TISCM = false;

	@Override
	public void setUseInstantNeighborUpdater$TISCM(boolean useInstant)
	{
		if (useInstant == this.usingInstantNeighborUpdater$TISCM)
		{
			return;
		}
		if (useInstant)
		{
			this.previousNeighborUpdater$TISCM = this.neighborUpdater;
			this.neighborUpdater = new SimpleNeighborUpdater((World)(Object)this);
		}
		else
		{
			this.neighborUpdater = Objects.requireNonNull(this.previousNeighborUpdater$TISCM);
			this.previousNeighborUpdater$TISCM = null;
		}
		this.usingInstantNeighborUpdater$TISCM = useInstant;
	}
}

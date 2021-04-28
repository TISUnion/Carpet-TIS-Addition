package carpettisaddition.commands.lifetime.filter;

import carpettisaddition.mixins.command.lifetime.filter.EntitySelectorAccessor;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class EntityFilter implements Predicate<Entity>
{
	private final EntitySelectorAccessor entitySelector;
	private final ServerCommandSource serverCommandSource;

	public EntityFilter(@NotNull ServerCommandSource serverCommandSource, @NotNull EntitySelector entitySelector)
	{
		this.entitySelector = (EntitySelectorAccessor)entitySelector;
		this.serverCommandSource = serverCommandSource;
	}

	@Override
	public boolean test(Entity testEntity)
	{
		if (testEntity == null)
		{
			return false;
		}
		if (this.entitySelector.getPlayerName() != null)
		{
			ServerPlayerEntity serverPlayerEntity = this.serverCommandSource.getMinecraftServer().getPlayerManager().getPlayer(this.entitySelector.getPlayerName());
			return testEntity == serverPlayerEntity;
		} 
		else if (this.entitySelector.getUuid() != null) 
		{
			for (ServerWorld serverWorld : this.serverCommandSource.getMinecraftServer().getWorlds())
			{
				Entity entity = serverWorld.getEntity(this.entitySelector.getUuid());
				if (testEntity == entity)
				{
					return true;
				}
			}
			return false;
		}
		Vec3d anchorPos = this.entitySelector.getPositionOffset().apply(this.serverCommandSource.getPosition());
		Predicate<Entity> predicate = this.entitySelector.invokeGetPositionPredicate(anchorPos);
		if (this.entitySelector.getSenderOnly() && testEntity != this.serverCommandSource.getEntity())
		{
			return false;
		}
		if (this.entitySelector.getLocalWorldOnly() && testEntity.getEntityWorld() != this.serverCommandSource.getWorld())
		{
			return false;
		}
		if (this.entitySelector.getType() != null && testEntity.getType() != this.entitySelector.getType())
		{
			return false;
		}
		if (this.entitySelector.getBox() != null && !testEntity.getBoundingBox().intersects(this.entitySelector.getBox().offset(anchorPos)))
		{
			return false;
		}
		return predicate.test(testEntity);
	}
}

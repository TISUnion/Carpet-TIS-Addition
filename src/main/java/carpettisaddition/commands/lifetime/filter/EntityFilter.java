package carpettisaddition.commands.lifetime.filter;

import carpettisaddition.mixins.command.lifetime.filter.EntitySelectorAccessor;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.utils.DimensionWrapper;
import carpettisaddition.utils.Messenger;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class EntityFilter extends TranslationContext implements Predicate<Entity>
{
	private final EntitySelectorAccessor entitySelector;
	private final ServerCommandSource serverCommandSource;

	public EntityFilter(@NotNull ServerCommandSource serverCommandSource, @NotNull EntitySelector entitySelector)
	{
		super(EntityFilterManager.getInstance().getTranslator());
		this.entitySelector = (EntitySelectorAccessor)entitySelector;
		this.serverCommandSource = serverCommandSource;
	}

	private Vec3d getAnchorPos()
	{
		return this.entitySelector.getPositionOffset().apply(this.serverCommandSource.getPosition());
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
			ServerPlayerEntity serverPlayerEntity = this.serverCommandSource.getServer().getPlayerManager().getPlayer(this.entitySelector.getPlayerName());
			return testEntity == serverPlayerEntity;
		} 
		else if (this.entitySelector.getUuid() != null) 
		{
			for (ServerWorld serverWorld : this.serverCommandSource.getServer().getWorlds())
			{
				Entity entity = serverWorld.getEntity(this.entitySelector.getUuid());
				if (testEntity == entity)
				{
					return true;
				}
			}
			return false;
		}
		Vec3d anchorPos = this.getAnchorPos();
		Predicate<Entity> predicate = this.entitySelector.invokeGetPositionPredicate(anchorPos);
		if (this.entitySelector.getSenderOnly() && testEntity != this.serverCommandSource.getEntity())
		{
			return false;
		}
		if (this.entitySelector.getLocalWorldOnly() && testEntity.getEntityWorld() != this.serverCommandSource.getWorld())
		{
			return false;
		}
		if (this.entitySelector.getEntityFilter() != null && this.entitySelector.getEntityFilter().downcast(testEntity) == null)
		{
			return false;
		}
		if (this.entitySelector.getBox() != null && !testEntity.getBoundingBox().intersects(this.entitySelector.getBox().offset(anchorPos)))
		{
			return false;
		}
		return predicate.test(testEntity);
	}

	public BaseText toText()
	{
		String inputText = this.entitySelector.getInputText();
		return Messenger.fancy(
				"y",
				Messenger.s(inputText),
				Messenger.c(
						tr("dimension"), "w : ",
						Messenger.dimension(DimensionWrapper.of(this.serverCommandSource.getWorld())),
						"w \n",
						tr("anchor_pos"), "w : ",
						Messenger.s(this.getAnchorPos().toString())
				),
				new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, inputText)
		);
	}
}

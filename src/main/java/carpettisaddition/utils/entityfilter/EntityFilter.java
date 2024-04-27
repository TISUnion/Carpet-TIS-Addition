/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpettisaddition.utils.entityfilter;

import carpettisaddition.mixins.utils.entityfilter.EntitySelectorAccessor;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Predicate;

public class EntityFilter extends TranslationContext implements Predicate<Entity>
{
	private final EntitySelectorAccessor entitySelector;
	private final ServerCommandSource serverCommandSource;

	public EntityFilter(@NotNull ServerCommandSource serverCommandSource, @NotNull EntitySelector entitySelector)
	{
		super("util.entity_filter");
		this.entitySelector = (EntitySelectorAccessor)entitySelector;
		this.serverCommandSource = serverCommandSource;
	}

	public static EntityFilter create(Entity entity, String filterDescriptor) throws CommandSyntaxException
	{
		ServerCommandSource source = entity.getCommandSource();
		EntitySelectorReader reader = new EntitySelectorReader(new StringReader(filterDescriptor), source.hasPermissionLevel(2));
		return new EntityFilter(source, reader.read());
	}

	public static Optional<EntityFilter> createOptional(Entity entity, String filterDescriptor)
	{
		try
		{
			return Optional.of(create(entity, filterDescriptor));
		}
		catch (CommandSyntaxException e)
		{
			return Optional.empty();
		}
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
		if (
				//#if MC >= 11700
				this.entitySelector.getEntityFilter() != null && this.entitySelector.getEntityFilter().downcast(testEntity) == null
				//#else
				//$$ this.entitySelector.getType() != null && testEntity.getType() != this.entitySelector.getType()
				//#endif
		)
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

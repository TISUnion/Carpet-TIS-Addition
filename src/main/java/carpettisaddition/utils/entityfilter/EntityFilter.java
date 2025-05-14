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
import carpettisaddition.utils.EntityUtils;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

	public static EntityFilter create(ServerPlayerEntity player, String filterDescriptor) throws CommandSyntaxException
	{
		ServerCommandSource source = player.getCommandSource();
		EntitySelectorReader reader = new EntitySelectorReader(new StringReader(filterDescriptor), source.hasPermissionLevel(2));
		return new EntityFilter(source, reader.read());
	}

	public static Optional<EntityFilter> createOptional(PlayerEntity player, String filterDescriptor)
	{
		if (!(player instanceof ServerPlayerEntity))
		{
			return Optional.empty();
		}
		try
		{
			return Optional.of(create((ServerPlayerEntity)player, filterDescriptor));
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

	@Nullable
	private Box getOffsetBox(Vec3d anchorPos)
	{
		Box box = this.entitySelector.getBox();
		return box == null ? null : box.offset(anchorPos);
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
		Vec3d anchorPos = this.getAnchorPos();
		Box offsetBox = this.getOffsetBox(anchorPos);
		Predicate<Entity> predicate = this.entitySelector.invokeGetPositionPredicate(
				anchorPos
				//#if MC >= 12100
				//$$ , offsetBox, this.serverCommandSource.getEnabledFeatures()
				//#endif
		);
		if (this.entitySelector.getSenderOnly() && testEntity != this.serverCommandSource.getEntity())
		{
			return false;
		}
		if (this.entitySelector.getLocalWorldOnly() && EntityUtils.getEntityWorld(testEntity) != this.serverCommandSource.getWorld())
		{
			return false;
		}
		if (
				//#if MC >= 11700
				//$$ this.entitySelector.getEntityFilter() != null && this.entitySelector.getEntityFilter().downcast(testEntity) == null
				//#else
				this.entitySelector.getType() != null && testEntity.getType() != this.entitySelector.getType()
				//#endif
		)
		{
			return false;
		}
		if (offsetBox != null && !testEntity.getBoundingBox().intersects(offsetBox))
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
				Messenger.ClickEvents.suggestCommand(inputText)
		);
	}
}

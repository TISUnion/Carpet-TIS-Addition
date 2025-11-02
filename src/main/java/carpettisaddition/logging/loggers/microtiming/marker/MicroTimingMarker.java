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

package carpettisaddition.logging.loggers.microtiming.marker;

//#if MC >= 11500
import carpet.script.utils.ShapeDispatcher;
//#else
//$$ import carpettisaddition.utils.compat.carpet.scarpet.ShapeDispatcher;
//#endif

import carpet.script.value.NumericValue;
import carpet.script.value.Value;
import carpettisaddition.helpers.carpet.shape.ShapeHolder;
import carpettisaddition.helpers.carpet.shape.ShapeUtil;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.mixins.logger.microtiming.marker.DyeColorAccessor;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.TextUtils;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

//#if MC < 11700
import org.apache.commons.lang3.tuple.Pair;
//#endif

public class MicroTimingMarker
{
	public static final int MARKER_SYNC_INTERVAL = 5 * 20;  // 5sec
	public static final int MARKER_RENDER_DURATION = 10 * 60 * 20;  // 5min
	// 5min / 5sec = 60, so it should works fine unless the server tps is below 20 / 60 = 0.333

	private final ServerLevel serverWorld;
	private final BlockPos blockPos;
	public final DyeColor color;
	private final ShapeHolder<ShapeDispatcher.Box> box;
	@Nullable
	private final ShapeHolder<ShapeDispatcher.Text> text;
	@Nullable
	private final BaseComponent markerName;
	private MicroTimingMarkerType markerType;
	private boolean movable;

	public long tickCounter;

	@SuppressWarnings("ConstantConditions")
	private MicroTimingMarker(ServerLevel serverWorld, BlockPos blockPos, DyeColor color, @Nullable BaseComponent markerName, MicroTimingMarkerType markerType, boolean movable)
	{
		this.serverWorld = serverWorld;
		this.blockPos = blockPos;
		this.color = color;
		this.markerName = markerName;
		this.markerType = markerType;
		this.movable = movable;

		Function<BlockPos, Vec3> fv =
				//#if MC >= 11600
				//$$ Vec3::atLowerCornerOf;
				//#else
				Vec3::new;
				//#endif

		this.box = ShapeUtil.createBox(
				fv.apply(blockPos),
				fv.apply(blockPos.offset(1, 1, 1)),
				DimensionWrapper.of(serverWorld),
				((long)((DyeColorAccessor)(Object)this.color).getTextColor() << 8) | 0xAF
		);
		if (this.markerName != null)
		{
			BaseComponent text = Messenger.c(
					//#if MC >= 11600
					//$$ MicroTimingUtil.getColorStyle(this.color) + " # ",
					//#else
					Messenger.s(Messenger.parseCarpetStyle(MicroTimingUtil.getColorStyle(this.color)).getColor() + "# " + ChatFormatting.RESET),
					//#endif
					Messenger.copy(this.markerName)
			);
			this.text = ShapeUtil.createLabel(text, new Vec3(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D), DimensionWrapper.of(serverWorld), null);
		}
		else
		{
			this.text = null;
		}
		this.updateLineWidth();
	}

	public MicroTimingMarker(ServerLevel serverWorld, BlockPos blockPos, DyeColor color, @Nullable BaseComponent markerName)
	{
		this(serverWorld, blockPos, color, markerName, MicroTimingMarkerType.REGULAR, false);
	}

	public boolean isMovable()
	{
		return this.movable;
	}

	public void setMovable(boolean movable)
	{
		this.movable = movable;
	}

	@Nullable
	public String getMarkerNameString()
	{
		return this.markerName != null ? this.markerName.getString() : null;
	}

	public MicroTimingMarkerType getMarkerType()
	{
		return this.markerType;
	}

	private void updateLineWidth()
	{
		this.box.setValue("line", new NumericValue(this.markerType.getLineWidth()));
	}

	public boolean rollMarkerType()
	{
		boolean hasNext;
		switch (this.markerType)
		{
			case REGULAR:
				this.markerType = MicroTimingMarkerType.END_ROD;
				hasNext = true;
				break;
			case END_ROD:
			default:
				hasNext = false;
				break;
		}
		if (hasNext)
		{
			this.cleanShapeToAll();
			this.updateLineWidth();
			this.sendShapeToAll();
		}
		return hasNext;
	}

	public
	//#if MC >= 11700
	//$$ List<ShapeDispatcher.ShapeWithConfig>
	//#else
	List<Pair<ShapeDispatcher.ExpiringShape, Map<String, Value>>>
	//#endif
	getShapeDataList(boolean display)
	{
		//#if MC >= 11700
		//$$ List<ShapeDispatcher.ShapeWithConfig>
		//#else
		List<Pair<ShapeDispatcher.ExpiringShape, Map<String, Value>>>
		//#endif
				result = Lists.newArrayList();

		result.add(this.box.toPair(display));
		if (this.text != null)
		{
			result.add(this.text.toPair(display));
		}
		return result;
	}

	public void sendShapeToAll()
	{
		ShapeDispatcher.sendShape(
				MicroTimingUtil.getSubscribedPlayers(),
				this.getShapeDataList(true)
				//#if MC >= 12005
				//$$ , this.serverWorld.getRegistryManager()
				//#endif
		);
	}

	public void cleanShapeToAll()
	{
		ShapeDispatcher.sendShape(
				MicroTimingUtil.getSubscribedPlayers(),
				this.getShapeDataList(false)
				//#if MC >= 12005
				//$$ , this.serverWorld.getRegistryManager()
				//#endif
		);
	}

	public StorageKey getStorageKey()
	{
		return new StorageKey(this.serverWorld, this.blockPos);
	}

	/**
	 * Create a copied marker at offset direction
	 */
	public MicroTimingMarker offsetCopy(Direction direction)
	{
		return new MicroTimingMarker(this.serverWorld, this.blockPos.relative(direction), this.color, this.markerName, this.markerType, this.movable);
	}

	// 1.15 client cannot response to text component color, so just use Formatting symbol here
	private BaseComponent withFormattingSymbol(String text)
	{
		//#if MC >= 11600
		//$$ return Messenger.s(text, MicroTimingUtil.getColorStyle(this.color));
		//#else
		return Messenger.s(Messenger.parseCarpetStyle(MicroTimingUtil.getColorStyle(color)).getColor() + text + ChatFormatting.RESET);
		//#endif
	}

	// [1, 2, 3]
	public BaseComponent toShortText()
	{
		return this.withFormattingSymbol(TextUtils.coord(this.blockPos));
	}

	// [1, 2, 3] red
	public BaseComponent toFullText()
	{
		return Messenger.c(
				Messenger.s(TextUtils.coord(this.blockPos)),
				this.withFormattingSymbol(" " + this.color.toString())
		);
	}
}

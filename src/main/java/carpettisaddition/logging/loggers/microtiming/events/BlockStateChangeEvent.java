package carpettisaddition.logging.loggers.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class BlockStateChangeEvent extends AbstractSetblockStateEvent
{
	private final Map<Property<?>, PropertyChange> changes = Maps.newLinkedHashMap();

	public BlockStateChangeEvent(EventType eventType, BlockState oldBlockState, BlockState newBlockState, Boolean returnValue, int flags)
	{
		super(eventType, "block_state_change", oldBlockState, newBlockState, returnValue, flags);
	}

	public void setChanges(Collection<PropertyChange> changes)
	{
		this.changes.clear();
		changes.forEach(change -> this.changes.put(change.property, change));
	}

	private BaseText getChangesText(boolean isHover)
	{
		Function<@Nullable Property<?>, BaseText> hoverMaker = currentProperty -> {
			List<BaseText> lines = Lists.newArrayList();
			lines.add(Messenger.formatting(tr("state_change_details"), Formatting.BOLD));
			this.oldBlockState.getProperties().stream().
					map(property -> {
						BaseText text = Optional.ofNullable(this.changes.get(property)).
								map(PropertyTexts::change).
								orElseGet(() -> {
									Object value = this.oldBlockState.get(property);
									return PropertyTexts.value(": ", property, value);
								});
						if (property.equals(currentProperty))
						{
							text.append(Messenger.s("    <---", Formatting.GRAY));
						}
						return text;
					}).
					forEach(lines::add);
			return Messenger.join(Messenger.s("\n"), lines.toArray(new BaseText[0]));
		};
		if (isHover)
		{
			return hoverMaker.apply(null);
		}
		else
		{
			return Messenger.join(
					Messenger.s(" "),
					this.changes.values().stream().
							map(change -> Messenger.hover(
									PropertyTexts.value("=", change.property, change.newValue),
									hoverMaker.apply(change.property)
							)).
							toArray(BaseText[]::new)
			);
		}
	}

	@Override
	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		BaseText titleText = Messenger.fancy(
				null,
				Messenger.formatting(tr("state_change"), COLOR_ACTION),
				this.getFlagsText(),
				null
		);
		if (this.getEventType() != EventType.ACTION_END)
		{
			list.add(Messenger.c(
					titleText,
					"g : ",
					this.getChangesText(false)
			));
		}
		else
		{
			list.add(Messenger.fancy(
					Messenger.c(titleText, Messenger.getSpaceText(), Messenger.formatting(tr("finished"), COLOR_RESULT)),
					this.getChangesText(true),
					null
			));
		}
		if (this.returnValue != null)
		{
			list.add("w  ");
			list.add(MicroTimingUtil.getSuccessText(this.returnValue, true));
		}
		return Messenger.c(list.toArray(new Object[0]));
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		BlockStateChangeEvent that = (BlockStateChangeEvent) o;
		return Objects.equals(changes, that.changes);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), changes);
	}

	public static class PropertyTexts
	{
		// xxx${divider}aaa
		public static BaseText value(String divider, Property<?> property, Object value)
		{
			return Messenger.c(
					Messenger.s(property.getName()),
					"g " + divider,
					Messenger.property(property, value)
			);
		}

		// xxx: aaa->bbb
		public static BaseText change(Property<?> property, Object oldValue, Object newValue)
		{
			return Messenger.c(
					Messenger.s(property.getName()),
					"g : ",
					Messenger.property(property, oldValue),
					"g ->",
					Messenger.property(property, newValue)
			);
		}

		public static BaseText change(PropertyChange propertyChange)
		{
			return change(propertyChange.property, propertyChange.oldValue, propertyChange.newValue);
		}
	}

	public static class PropertyChange
	{
		public final Property<?> property;
		public final Object oldValue;
		public final Object newValue;

		public PropertyChange(Property<?> property, Object oldValue, Object newValue)
		{
			this.property = property;
			this.oldValue = oldValue;
			this.newValue = newValue;
		}

		@Override
		public boolean equals(Object o)
		{
			if (this == o) return true;
			if (!(o instanceof PropertyChange)) return false;
			PropertyChange changes = (PropertyChange) o;
			return Objects.equals(property, changes.property) &&
					Objects.equals(oldValue, changes.oldValue) &&
					Objects.equals(newValue, changes.newValue);
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(property, oldValue, newValue);
		}
	}
}

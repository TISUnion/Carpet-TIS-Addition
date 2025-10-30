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

package carpettisaddition.commands.info.world;

import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.info.InfoSubcommand;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.Lists;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

//#if MC >= 11600
//$$ import net.minecraft.world.level.ServerWorldProperties;
//#else
import net.minecraft.world.level.storage.LevelData;
//#endif

import static net.minecraft.commands.Commands.literal;

public class InfoWorldCommand extends InfoSubcommand
{
	private static final InfoWorldCommand INSTANCE = new InfoWorldCommand();

	public static InfoWorldCommand getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void extendCommand(CommandTreeContext.Node context)
	{
		context.node.
				then(literal("ticking_order").
						executes((c) -> showWorldTickOrder(c.getSource()))
				).
				then(literal("weather").
						executes((c) -> showWeather(c.getSource()))
		);
	}

	private int showWorldTickOrder(CommandSourceStack source)
	{
		List<Level> worlds = Lists.newArrayList(source.getServer().getAllLevels());
		Messenger.tell(source, tr("ticking_order", worlds.size()));
		int order = 0;
		for (Level world : worlds)
		{
			order++;
			Messenger.tell(
					source,
					Messenger.c(
							"g " + order + ". ",
							Messenger.dimension(DimensionWrapper.of(world))
					)
			);
		}
		return 1;
	}

	@SuppressWarnings("ConstantConditions")
	private int showWeather(CommandSourceStack source)
	{
		ServerLevel world = source.getLevel();

		//#if MC >= 11600
		//$$ ServerWorldProperties worldInfo = (ServerWorldProperties)world.getLevelProperties();
		//#else
		LevelData worldInfo = world.getLevelData();
		//#endif

		int clearTime = worldInfo.getClearWeatherTime();
		boolean raining = worldInfo.isRaining();
		boolean thundering = worldInfo.isThundering();
		int rainTime = worldInfo.getRainTime();
		int thunderTime = worldInfo.getThunderTime();

		BiFunction<String, Object, BaseComponent> itemPrint = (key, value) -> Messenger.c(
				Messenger.hover(tr("weather.data." + key), Messenger.s(key)),
				Messenger.s(" = ", ChatFormatting.GRAY),
				Messenger.colored(value)
		);
		Function<Integer, BaseComponent> toMin = ticks -> Messenger.c(
				Messenger.hover(Messenger.s(String.format("%.1f", (double)ticks / 20 / 60)), Messenger.s(ticks + " ticks")),
				"g min"
		);
		BiFunction<BaseComponent, Integer, BaseComponent> durationPrint = (name, ticks) -> Messenger.c(name, "g : ", toMin.apply(ticks));

		Messenger.tell(source, Messenger.s(""));
		Messenger.tell(source, Messenger.c("g ======= ", tr("weather.data.title"), "g  ======="));
		Messenger.tell(source, itemPrint.apply("clearWeatherTime", clearTime));
		Messenger.tell(source, itemPrint.apply("rainTime", rainTime));
		Messenger.tell(source, itemPrint.apply("thunderTime", thunderTime));
		Messenger.tell(source, itemPrint.apply("raining", raining));
		Messenger.tell(source, itemPrint.apply("thundering", thundering));

		Messenger.tell(source, Messenger.s(""));
		Messenger.tell(source, Messenger.c("g ======= ", tr("weather.forecast.title"), "g  ======="));
		if (clearTime > 0)
		{
			Messenger.tell(source, durationPrint.apply(tr("weather.forecast.clear_sky_duration"), clearTime));
		}
		else if (raining && thundering)
		{
			Messenger.tell(source, tr("weather.forecast.current", Messenger.formatting(tr("weather.weathers.thundering"), ChatFormatting.LIGHT_PURPLE)));
			Messenger.tell(source, durationPrint.apply(tr("weather.forecast.rain_duration"), rainTime));
			Messenger.tell(source, durationPrint.apply(tr("weather.forecast.thunder_duration"), Math.min(rainTime, thunderTime)));
		}
		else if (raining && !thundering)
		{
			Messenger.tell(source, tr("weather.forecast.current", Messenger.formatting(tr("weather.weathers.raining"), ChatFormatting.BLUE)));
			Messenger.tell(source, durationPrint.apply(tr("weather.forecast.rain_duration"), rainTime));
			if (thunderTime < rainTime)
			{
				Messenger.tell(source, tr("weather.forecast.thunder_in", toMin.apply(thunderTime)));
			}
			else
			{
				Messenger.tell(source, tr("weather.forecast.no_thunder"));
			}
		}
		else
		{
			Messenger.tell(source, tr("weather.forecast.current", Messenger.formatting(tr("weather.weathers.clear_sky"), ChatFormatting.WHITE)));
			Messenger.tell(source, tr("weather.forecast.rain_in", toMin.apply(rainTime)));
			if (thundering)
			{
				if (rainTime < thunderTime)
				{
					Messenger.tell(source, tr("weather.forecast.rain_in.with_thunder", toMin.apply(thunderTime - rainTime)));
				}
				else
				{
					Messenger.tell(source, tr("weather.forecast.rain_in.thunder_unknown", toMin.apply(thunderTime)));
				}
			}
			else
			{
				if (rainTime < thunderTime)
				{
					Messenger.tell(source, tr("weather.forecast.rain_in.no_thunder_for_now", toMin.apply(thunderTime)));
				}
				else
				{
					Messenger.tell(source, tr("weather.forecast.rain_in.maybe_thunder", toMin.apply(thunderTime)));
				}
			}
		}
		return 1;
	}
}

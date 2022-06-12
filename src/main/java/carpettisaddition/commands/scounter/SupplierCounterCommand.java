package carpettisaddition.commands.scounter;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.utils.CarpetModUtil;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import net.minecraft.util.DyeColor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.minecraft.command.CommandSource.suggestMatching;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SupplierCounterCommand extends AbstractCommand
{
	private static final SupplierCounterCommand INSTANCE = new SupplierCounterCommand();
	private static final String NAME = "supplier_counter";
	public static final String PREFIX = "scounter";
	public static final List<String> COLORS = Arrays.stream(DyeColor.values()).map(cl -> cl.getName().toLowerCase()).collect(Collectors.toList());

	private final Map<DyeColor, SupplierCounter> counter = Maps.newEnumMap(DyeColor.class);

	public SupplierCounterCommand()
	{
		super(NAME);
		for (DyeColor color : DyeColor.values())
		{
			this.counter.put(color, new SupplierCounter(color, this.getTranslator().getDerivedTranslator("counter")));
		}
	}

	public static boolean isActivated()
	{
		return CarpetTISAdditionSettings.hopperNoItemCost;
	}

	public static SupplierCounterCommand getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher)
	{
		LiteralArgumentBuilder<ServerCommandSource> root = literal(PREFIX).
				requires(s -> CarpetModUtil.canUseCommand(s, CarpetTISAdditionSettings.hopperNoItemCost)).
				executes(c -> reportAll(c.getSource())).
				then(literal("reset").
						executes(c -> resetAll(c.getSource()))
				).
				then(argument("color", word()).
						suggests((c, b) -> suggestMatching(COLORS, b)).
						executes(c -> report(c.getSource(), getString(c, "color"), false)).
						then(literal("realtime").
								executes(c -> report(c.getSource(), getString(c, "color"), true))
						).
						then(literal("reset").
								executes(c -> resetSingle(c.getSource(), getString(c, "color")))
						)
				);
		dispatcher.register(root);
	}

	public void addFor(DyeColor color, ItemStack stack)
	{
		this.counter.get(color).addItem(stack);
	}

	public int report(ServerCommandSource source, String color, boolean realtime)
	{
		return this.doWithCounter(source, color, counter -> {
			Messenger.tell(source, counter.report(realtime));
			return 1;
		});
	}

	private int reportAll(ServerCommandSource source)
	{
		int printed = 0;

		for (SupplierCounter counter : this.counter.values())
		{
			List<BaseText> lines = counter.report(false);

			if (lines.size() > 1)
			{
				if (printed > 0)
				{
					Messenger.tell(source, Messenger.s(""));
				}
				printed++;
				Messenger.tell(source, lines);
			}
		}

		if (printed == 0)
		{
			Messenger.tell(source, tr("no_item_yet"));
		}

		return printed;
	}

	private int doWithCounter(ServerCommandSource source, String color, Function<SupplierCounter, Integer> consumer)
	{
		return this.getCounter(color).
				map(consumer).
				orElseGet(() -> {
					Messenger.tell(source, tr("unknown_color", color));
					return 0;
				});
	}

	public int resetAll(ServerCommandSource source)
	{
		for (SupplierCounter counter : this.counter.values())
		{
			counter.reset();
		}
		Messenger.tell(source, tr("restart_all"), true);
		return 1;
	}

	public int resetSingle(ServerCommandSource source, String color)
	{
		return this.doWithCounter(source, color, counter -> {
			counter.reset();
			Messenger.tell(source, tr("restart_single", counter.getNameText()), true);
			return 1;
		});
	}

	public Optional<SupplierCounter> getCounter(String color)
	{
		try
		{
			DyeColor dyeColor = DyeColor.valueOf(color.toUpperCase());
			return Optional.of(this.counter.get(dyeColor));
		}
		catch (IllegalArgumentException e)
		{
			return Optional.empty();
		}
	}
}

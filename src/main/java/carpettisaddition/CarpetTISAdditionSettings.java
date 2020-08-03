package carpettisaddition;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;

import java.util.regex.Pattern;

import static carpet.settings.RuleCategory.*;

/**
 * Here is your example Settings class you can plug to use carpetmod /carpet settings command
 */
public class CarpetTISAdditionSettings
{
    public static final String TIS = "TIS";
    public static final String CARPET_MOD = "carpet_mod";  // _ cannot be replaced by space or you can't /carpet list this

    @Rule(
            desc = "Set the range where player will receive a block event packet after a block event fires successfully",
            extra = "For piston the packet is used to render the piston movement animation. Decrease it to reduce client's lag",
            validate = ValidatorBlockEventPacketRange.class,
            options = {"0", "16", "64", "128"},
            strict = false,
            category = {TIS, OPTIMIZATION}
    )
    public static double blockEventPacketRange = 64;
	public static class ValidatorBlockEventPacketRange extends Validator<Double>
	{
		@Override
		public Double validate(ServerCommandSource source, ParsedRule<Double> currentRule, Double newValue, String string)
		{
			return 0 <= newValue && newValue <= 1024 ? newValue : null;
		}

		@Override
		public String description() { return "You must choose a value from 0 to 1024";}
	}


	@Rule(
			desc = "Overwrite the size limit of structure block",
			extra = "Relative position might display wrongly on client side if it's larger than 32",
			validate = ValidateStructureBlockLimit.class,
			options = {"32", "64", "96", "127"},
			strict = false,
			category = {TIS, CREATIVE}
	)
	public static int structureBlockLimit = 32;
	private static class ValidateStructureBlockLimit extends Validator<Integer>
	{
		@Override
		public Integer validate(ServerCommandSource source, ParsedRule<Integer> currentRule, Integer newValue, String string)
		{
			return (newValue > 0 && newValue <= 127) ? newValue : null;
		}
		public String description()
		{
			return "You must choose a value from 1 to 127";
		}
	}

	@Rule(
			desc = "Overwrite the tracking distance of xp orb",
			extra = "Change it to 0 to disable tracking",
			options = {"0", "1", "8", "32"},
			validate = ValidateXPTrackingDistance.class,
			strict = false,
			category = {TIS, CREATIVE}
	)
	public static double xpTrackingDistance = 8.0F;
	private static class ValidateXPTrackingDistance extends Validator<Double>
	{
		@Override
		public Double validate(ServerCommandSource source, ParsedRule<Double> currentRule, Double newValue, String string)
		{
			return (newValue >= 0 && newValue <= 128) ? newValue : null;
		}
		public String description()
		{
			return "You must choose a value from 0 to 128";
		}
	}

	@Rule(
			desc = "Disable TNT, carpet and part of rail dupers",
			extra = {
					"Attachment block update based dupers will do nothing and redstone component update based dupers can no longer keep their duped block",
					"Dupe bad dig good"
			},
			category = {TIS, BUGFIX, EXPERIMENTAL}
	)
	public static boolean tntDupingFix = false;

	public static final String fakePlayerNameNoExtra = "#none";
	@Rule(
			desc = "Add a name prefix for fake players spawned with /player command",
			extra = {
					"Set it to " + fakePlayerNameNoExtra + " to stop adding a prefix",
					"Which can prevent summoning fake player with illegal names and make player list look nicer"
			},
			options = {fakePlayerNameNoExtra, "bot_"},
			validate = ValidateFakePlayerNameExtra.class,
			strict = false,
			category = {TIS, CARPET_MOD}
	)
	public static String fakePlayerNamePrefix = fakePlayerNameNoExtra;

	@Rule(
			desc = "Add a name suffix for fake players spawned with /player command",
			extra = {
					"Set it to " + fakePlayerNameNoExtra + " to stop adding a suffix"
			},
			options = {fakePlayerNameNoExtra, "_fake"},
			validate = ValidateFakePlayerNameExtra.class,
			strict = false,
			category = {TIS, CARPET_MOD}
	)
	public static String fakePlayerNameSuffix = fakePlayerNameNoExtra;

	private static class ValidateFakePlayerNameExtra extends Validator<String>
	{
		@Override
		public String validate(ServerCommandSource source, ParsedRule<String> currentRule, String newValue, String string)
		{
			return (newValue.equals(fakePlayerNameNoExtra) || Pattern.matches("[a-zA-Z_0-9]{1,16}", newValue)) ? newValue : null;
		}
		public String description()
		{
			return "You must give a string without special characters and with a length from 1 to 16";
		}
	}

	@Rule(
			desc = "Make dragon egg renewable",
			extra = {
					"When a dragon egg is in dragon breath effect cloud it has a possibility to absorb the effect cloud and \"summon\" a new dragon egg",
					"Use with rule \"dispensersFireDragonBreath\" for more ease"
			},
			category = {TIS, FEATURE}
	)
	public static boolean renewableDragonEgg = false;

	@Rule(
			desc = "Dispenser can fire dragon breath bottle to create a dragon breath effect cloud",
			category = {TIS, FEATURE, DISPENSER}
	)
	public static boolean dispensersFireDragonBreath = false;

	@Rule(
			desc = "Ender dragon killed by charged creeper will drop dragon head",
			category = {TIS, FEATURE}
	)
	public static boolean renewableDragonHead = false;

	@Rule(
			desc = "Overwrite HUD loggers update interval (gametick)",
			options = {"1", "5", "20", "100"},
			validate = ValidateHUDLoggerUpdateInterval.class,
			strict = false,
			category = {TIS, CARPET_MOD}
	)
	public static int HUDLoggerUpdateInterval = 20;
	private static class ValidateHUDLoggerUpdateInterval extends Validator<Integer>
	{
		@Override
		public Integer validate(ServerCommandSource source, ParsedRule<Integer> currentRule, Integer newValue, String string)
		{
			return (1 <= newValue && newValue <= 1000) ? newValue : null;
		}
		public String description()
		{
			return "You must give a integer from 1 to 1000";
		}
	}

	@Rule(
			desc = "Make hopper pointing towards wool has infinity speed to suck in or transfer items",
			extra = {
					"Only works when hopperCounters option in Carpet Mod is on"
			},
			category = {TIS, CREATIVE, CARPET_MOD}
	)
	public static boolean hopperCountersUnlimitedSpeed = false;


	@Rule(
			desc = "Phathom killed by shulker will drops an elytra with given possibility",
			extra = "Set it to 0 to disable",
			options = {"0", "0.2", "1"},
			validate = ValidatePossibility.class,
			strict = false,
			category = {TIS, FEATURE}
	)
	public static double renewableElytra = 0.0D;

	@Rule(
			desc = "Disable sand and other gravity block duping using end portal",
			extra = {
					"Gravity block includes sand, anvil, dragon egg and so on",
					"In sand dupers sand will only get teleported to the other dimension"
			},
			category = {TIS, BUGFIX}
	)
	public static boolean sandDupingFix = false;

	@Rule(
			desc = "Disable rail duping using old school pushing lit powered or activator rail method",
			category = {TIS, BUGFIX}
	)
	public static boolean railDupingFix = false;

	@Rule(
			desc = "Enables /raid command for raid tracking",
			category = {TIS, COMMAND}
	)
	public static String commandRaid = "true";




	private static class ValidatePossibility extends Validator<Double>
	{
		@Override
		public Double validate(ServerCommandSource source, ParsedRule<Double> currentRule, Double newValue, String string)
		{
			return (newValue >= 0.0D && newValue <= 1.0D) ? newValue : null;
		}
		public String description()
		{
			return "You must choose a value from 0 to 1";
		}
	}
}

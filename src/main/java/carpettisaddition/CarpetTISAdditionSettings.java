package carpettisaddition;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;
import carpettisaddition.logging.loggers.microtiming.enums.MicroTimingTarget;
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

	public static final double VANILLA_BLOCK_EVENT_PACKET_RANGE = 64.0D;
    @Rule(
            desc = "Set the range where player will receive a block event packet after a block event fires successfully",
            extra = "For piston the packet is used to render the piston movement animation. Decrease it to reduce client's lag",
            validate = Validator.NONNEGATIVE_NUMBER.class,
            options = {"0", "16", "64", "128"},
            strict = false,
            category = {TIS, OPTIMIZATION}
    )
    public static double blockEventPacketRange = VANILLA_BLOCK_EVENT_PACKET_RANGE;

//	Remove due to fabric carpet implement this in 1.4.25
//	public static int structureBlockLimit = 32;

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
			validate = Validator.PROBABILITY.class,
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
			desc = "Enables /raid command for raid listing and tracking",
			category = {TIS, COMMAND}
	)
	public static String commandRaid = "true";

	@Rule(
			desc = "The mobs in lazy chunks will not despawn",
			extra = "This option has no effect in versions before 1.15",
			category = {TIS, EXPERIMENTAL, FEATURE}
	)
	public static boolean keepMobInLazyChunks = false;

	@Rule(
	        desc = "Dispensers and droppers execute without having the itemstack inside decreased",
			extra = "Either dropping and using items do not cost, but dropper transferring item still costs",
			category = {TIS, DISPENSER, CREATIVE}
	)
	public static boolean dispenserNoItemCost = false;

	@Rule(
			desc = "Disable some command to prevent accidentally cheating",
			extra = "Affects command list: /gamemode, /tp, /teleport, /give, /setblock, /summon",
			category = {TIS, SURVIVAL},
			validate = Validator._COMMAND.class  // for notifyPlayersCommandsChanged
	)
	public static boolean opPlayerNoCheat = false;

	@Rule(
			desc = "Randomize the order for redstone dust to emit block updates",
			extra = "It's useful to test if your contraption is locational or not",
			category = {TIS, CREATIVE}
	)
	public static boolean redstoneDustRandomUpdateOrder = false;

	@Rule(
			desc = "Make command blocks on redstone ores execute command instantly instead of scheduling a 1gt delay TileTick event for execution",
			extra = "Only affects normal command blocks",
			category = {TIS, CREATIVE}
	)
	public static boolean instantCommandBlock = false;

	@Rule(
			desc = "Pause or disable light updates",
			extra = {
					"If set to suppressed, no light update can be executed",
					"If set to off, no light update can be scheduled or executed",
					"[WARNING] If set to suppressed or off, new chunks cannot be loaded. Then if the server tries to load chunk for player movement or whatever reason the server will be stuck forever"
			},
			category = {TIS, CREATIVE, EXPERIMENTAL}
	)
	public static LightUpdateOptions lightUpdates = LightUpdateOptions.ON;
	public enum LightUpdateOptions
	{
		ON,
		SUPPRESSED,
		OFF
	}

	@Rule(
			desc = "Enable the function of MicroTick logger",
			extra = {
					"Display redstone components actions, block updates and stacktrace with a wool block",
					"Use /log microTiming to start logging",
					"Might impact the server performance when it's on",
					"EndRods will detect block updates and redstone components will show their actions",
					"- Observer, Piston, EndRod: pointing towards wool",
					"- Repeater, Comparator, Rail, Button, etc.: placed on wool",
					"Beside that, a universal block actions logging method is using EndRod on wool block to point on the block you want to log",
					"Check rule microTimingTarget to see how to switch logging method"
			},
			category = {TIS, CREATIVE}
	)
	public static boolean microTiming = false;

	@Rule(
			desc = "Modify the way to specify events to be logged in microTiming logger",
			extra = {
					"labelled: Logs events labelled with wool",
					"in_range: Logs events within 32m of any player",
					"all: Logs every event. Use with caution"
			},
			category = {TIS, CREATIVE}
	)
	public static MicroTimingTarget microTimingTarget = MicroTimingTarget.LABELLED;

	@Rule(
			desc = "Disable spamming checks on players, including: chat message cooldown, creative item drop cooldown",
			category = {TIS, CREATIVE, SURVIVAL}
	)
	public static boolean antiSpamDisabled = false;

	@Rule(
			desc = "Disable entity collision check before block placement, aka you can place blocks inside entities",
			extra = {
					"Works with creative mode players only"
			},
			category = {TIS, CREATIVE}
	)
	public static boolean blockPlacementIgnoreEntity = false;

	@Rule(
			desc = "Modify how often the chunk tick occurs per chunk per game tick",
			extra = {
					"The default value is 1. Set it to 0 to disables chunk ticks",
					"Affected game phases: thunder, ice and snow, randomtick",
					"With a value of n, in every chunk every game tick, climate things will tick n times, and randomtick will tick n * randomTickSpeed times per chunk section"
			},
			options = {"0", "1", "10", "100", "1000"},
			validate = Validator.NONNEGATIVE_NUMBER.class,
			strict = false,
			category = {TIS, CREATIVE}
	)
	public static int chunkTickSpeed = 1;

	@Rule(
			desc = "Modify the limit of executed tile tick events per game tick",
			options = {"1024", "65536", "2147483647"},
			validate = ValidatePositive.class,
			strict = false,
			category = {TIS, CREATIVE}
	)
	public static int tileTickLimit = 65536;

	@Rule(
			desc = "Whether block changes will cause POI to updates or not",
			extra = {
					"Set it to false to disable POI updates"
			},
			category = {TIS, CREATIVE}
	)
	public static boolean poiUpdates = true;

	@Rule(
			desc = "Overwrite the default fuse duration of TNT",
			extra = "This might also affects the fuse duration of TNT ignited in explosion",
			options = {"0", "80", "32767"},
			validate = ValidateTNTFuseDuration.class,
			strict = false,
			category = {TIS, CREATIVE}
	)
	public static int tntFuseDuration = 80;

	private static class ValidateTNTFuseDuration extends Validator<Integer>
	{
		@Override
		public Integer validate(ServerCommandSource source, ParsedRule<Integer> currentRule, Integer newValue, String string)
		{
			return 0 <= newValue && newValue <= 32767 ? newValue : null;
		}
		public String description()
		{
			return "You must choose a integer from 0 to 32767";
		}
	}

	@Rule(
			desc = "Set it to false to disable entity axis momentum cancellation if it's above 10m/gt when being loaded from disk",
			category = {TIS, EXPERIMENTAL}
	)
	public static boolean entityMomentumLoss = true;

	@Rule(
			desc = "Halve the delay of redstone repeaters upon a redstone ore",
			extra = {
					"The delay will change from 2, 4, 6 or 8 game tick instead of 1, 2, 3 or 4 game tick"
			},
			category = {TIS, CREATIVE}
	)
	public static boolean repeaterHalfDelay = false;

	@Rule(
			desc = "Enables /lifetime command to track entity lifetime and so on",
			extra = {
					"Useful for mob farm debugging etc."
			},
			category = {TIS, COMMAND}
	)
	public static String commandLifeTime = "true";

	@Rule(
			desc = "Optimize fast entity movement by only checking block collisions on current moving axis",
			extra = {
					"Inspired by the fastMovingEntityOptimization rule in carpetmod112",
					"Use with rule optimizedTNT to greatly improve performance in cannons"
			},
			category = {TIS, OPTIMIZATION, EXPERIMENTAL}
	)
	public static boolean optimizedFastEntityMovement = false;

	@Rule(
			desc = "Use a Mixin injection with higher priority for carpet rule optimizedTNT",
			extra = {
					"So the rule optimizedTNT can overwrite lithium's explosion optimization",
					"Of course rule optimizedTNT needs to be on for it to work"
			},
			category = {TIS, OPTIMIZATION, EXPERIMENTAL}
	)
	public static boolean optimizedTNTHighPriority = false;

	@Rule(
			desc = "Optimize entity colliding with entities with hard hit box",
			extra = {
					"It uses an extra separate list to store entities, that have a hard hit box including boat and shulker, in a chunk",
					"It reduces quite a lot of unnecessary iterating when an entity is moving and trying to search entities with hard hit box on the way," +
							"since the world is always not filled with boats and shulkers",
					"Enable it before loading the chunk to make it work. ~20% performance boost in portal mob farms",
					"Might not work with other mods that add new entities"
			},
			category = {TIS, OPTIMIZATION, EXPERIMENTAL}
	)
	public static boolean optimizedHardHitBoxEntityCollision = false;

	@Rule(
			desc = "Allow creative players to open a shulker block even if the shulker box is blocked",
			category = {TIS, CREATIVE}
	)
	public static boolean creativeOpenShulkerBoxForcibly = false;

	@Rule(
			desc = "Disable all block updates and state updates",
			category = {TIS, CREATIVE}
	)
	public static boolean totallyNoBlockUpdate = false;

	@Rule(
			desc = "Tools on the player's main hand is applied to item dropping during the explosion caused by the player",
			extra = {
					"So you can ignite TNT to harvest blocks that require specific tool or enchantment as long as you are holding the right tool",
					"For example, you can harvest ice with silk touch pickaxe, or harvest grass with shears",
					"It also works for any other living entities beside player",
					"Technically this rule applies the main hand item of the causing entity onto the loot table builder during the explosion"
			},
			category = {TIS, FEATURE}
	)
	public static boolean tooledTNT = false;

	@Rule(
			desc = "Disable block destruction by fluid flowing",
			extra = {
					"Fluid will just simple stopped at the state before destroying the block",
					"It's useful to prevent liquid from accidentally flooding your redstone wiring in creative"
			},
			category = {TIS, CREATIVE}
	)
	public static boolean fluidDestructionDisabled = false;

	/*
	 *   Declare rules above this
	 *   General validators down below
	 */

// just use Validator.PROBABILITY
//	private static class ValidatePossibility extends Validator<Double>
//	{
//		@Override
//		public Double validate(ServerCommandSource source, ParsedRule<Double> currentRule, Double newValue, String string)
//		{
//			return (newValue >= 0.0D && newValue <= 1.0D) ? newValue : null;
//		}
//		public String description()
//		{
//			return "You must choose a value from 0 to 1";
//		}
//	}

	private static class ValidatePositive extends Validator<Number>
	{
		@Override
		public Number validate(ServerCommandSource source, ParsedRule<Number> currentRule, Number newValue, String string)
		{
			return newValue.doubleValue() > 0.0D ? newValue : null;
		}
		public String description()
		{
			return "You must choose a positive value";
		}
	}
}

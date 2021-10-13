package carpettisaddition;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;
import carpet.utils.Messenger;
import carpettisaddition.helpers.rule.lightEngineMaxBatchSize.LightBatchSizeChanger;
import carpettisaddition.helpers.rule.synchronizedLightThread.LightThreadSynchronizer;
import carpettisaddition.logging.loggers.microtiming.enums.MicroTimingTarget;
import carpettisaddition.logging.loggers.microtiming.enums.TickDivision;
import carpettisaddition.logging.loggers.microtiming.marker.MicroTimingMarkerManager;
import carpettisaddition.translations.Translator;
import com.google.common.collect.Maps;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static carpet.settings.RuleCategory.*;

public class CarpetTISAdditionSettings
{
    public static final String TIS = "TIS";
    public static final String CARPET_MOD = "carpet_mod";  // _ cannot be replaced by space or you can't /carpet list this

	private static final Translator translator = new Translator("rule");

	@Rule(
			desc = "Disable spamming checks on players, including: chat message cooldown, creative item drop cooldown",
			category = {TIS, CREATIVE, SURVIVAL}
	)
	public static boolean antiSpamDisabled = false;

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

	@Rule(
			desc = "Disable entity collision check before block placement, aka you can place blocks inside entities",
			extra = {
					"Works with creative mode players only"
			},
			category = {TIS, CREATIVE}
	)
	public static boolean blockPlacementIgnoreEntity = false;

	public static final int VANILLA_CHUNK_UPDATE_PACKET_THRESHOLD = 64;
	public static final int MAXIMUM_CHUNK_UPDATE_PACKET_THRESHOLD = 65536;
	@Rule(
			desc = "The threshold which the game will just send an chunk data packet if the amount of block changes is more than it",
			extra = {
					"Increasing this value might reduce network bandwidth usage, and boost client's fps if there are lots of tile entities in a chunk section with a lot of block changes",
					"Set it to really high to simulate 1.16+ behavior, which is no chunk packet but only multiple block change packet",
					"This rule is only available in <1.16"
			},
			validate = ValidateChunkUpdatePacketThreshold.class,
			options = {"64", "4096", "65536"},
			strict = false,
			category = {TIS, OPTIMIZATION, EXPERIMENTAL}
	)
	public static int chunkUpdatePacketThreshold = VANILLA_CHUNK_UPDATE_PACKET_THRESHOLD;
	private static class ValidateChunkUpdatePacketThreshold extends Validator<Integer>
	{
		@Override
		public Integer validate(ServerCommandSource source, ParsedRule<Integer> currentRule, Integer newValue, String string)
		{
			return (newValue >= 2 && newValue <= MAXIMUM_CHUNK_UPDATE_PACKET_THRESHOLD) ? newValue : null;
		}
		public String description()
		{
			return "You must choose a value from 2 to " + MAXIMUM_CHUNK_UPDATE_PACKET_THRESHOLD;
		}
	}

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
			desc = "Enables /lifetime command to track entity lifetime and so on",
			extra = {
					"Useful for mob farm debugging etc."
			},
			category = {TIS, COMMAND}
	)
	public static String commandLifeTime = "true";

	@Rule(
			desc = "Enables /raid command for raid listing and tracking",
			category = {TIS, COMMAND}
	)
	public static String commandRaid = "true";

	@Rule(
			desc = "Enables /refresh command for synchronizing your client to the server",
			category = {TIS, COMMAND}
	)
	public static String commandRefresh = "true";

	@Rule(
			desc = "Allow creative players to open a container even if the container is blocked. e.g. for shulker box",
			category = {TIS, CREATIVE}
	)
	public static boolean creativeOpenContainerForcibly = false;

	@Rule(
			desc = "Dispensers and droppers execute without having the itemstack inside decreased",
			extra = "Either dropping and using items do not cost, but dropper transferring item still costs",
			category = {TIS, DISPENSER, CREATIVE}
	)
	public static boolean dispenserNoItemCost = false;

	@Rule(
			desc = "Dispenser can fire dragon breath bottle to create a dragon breath effect cloud",
			category = {TIS, FEATURE, DISPENSER}
	)
	public static boolean dispensersFireDragonBreath = false;

	@Rule(
			desc = "Remove all enchantment restriction checks inside /enchant command",
			category = {TIS, CREATIVE}
	)
	public static boolean enchantCommandNoRestriction = false;

	@Rule(
			desc = "Set it to false to disable entity axis momentum cancellation if it's above 10m/gt when being loaded from disk",
			category = {TIS, EXPERIMENTAL}
	)
	public static boolean entityMomentumLoss = true;

	public static final double VANILLA_EXPLOSION_PACKET_RANGE = 64.0D;  // sqrt(4096)
	@Rule(
			desc = "Set the range where player will receive an explosion packet when an explosion happens",
			validate = Validator.NONNEGATIVE_NUMBER.class,
			options = {"0", "16", "64", "128", "2048"},
			strict = false,
			category = {TIS, CREATIVE}
	)
	public static double explosionPacketRange = VANILLA_EXPLOSION_PACKET_RANGE;

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
		private final Map<ParsedRule<?>, String> lastDangerousInput = Maps.newHashMap();

		@Override
		public String validate(ServerCommandSource source, ParsedRule<String> currentRule, String newValue, String string)
		{
			if (!newValue.equals(fakePlayerNameNoExtra) && !Pattern.matches("[a-zA-Z_0-9]{1,16}", newValue) && source != null)
			{
				Consumer<BaseText> messenger = msg -> source.sendFeedback(Messenger.s(msg.getString(), "r"), false);
				messenger.accept(translator.advTr("_validator.ValidateFakePlayerNameExtra.warn.found", "Unexpected character found in value \"%1$s\" when applying rule %2$s", newValue, currentRule.name));
				if (!Objects.equals(this.lastDangerousInput.get(currentRule), newValue))
				{
					messenger.accept(translator.advTr("_validator.ValidateFakePlayerNameExtra.warn.blocked", "Re-enter the command again if you do want to use this value"));
					this.lastDangerousInput.put(currentRule, newValue);
					return null;
				}
				messenger.accept(translator.advTr("_validator.ValidateFakePlayerNameExtra.warn.applied", "Accepted anyway cuz you insisted"));
			}
			this.lastDangerousInput.remove(currentRule);
			return newValue;
		}
		public String description()
		{
			return "You must give a string without special characters and with a length from 1 to 16";
		}
	}

	@Rule(
			desc = "Disable block destruction by fluid flowing",
			extra = {
					"Fluid will just simple stopped at the state before destroying the block",
					"It's useful to prevent liquid from accidentally flooding your redstone wiring in creative"
			},
			category = {TIS, CREATIVE}
	)
	public static boolean fluidDestructionDisabled = false;

	@Rule(
			desc = "Make hopper pointing towards wool has infinity speed to suck in or transfer items with no cooldown",
			extra = {
					"Only works when hopperCounters option in Carpet Mod is on"
			},
			category = {TIS, CREATIVE, CARPET_MOD}
	)
	public static boolean hopperCountersUnlimitedSpeed = false;

	@Rule(
			desc = "Hopper with wool block on top outputs item infinitely without having its item decreased",
			category = {TIS, CREATIVE}
	)
	public static boolean hopperNoItemCost = false;

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
			desc = "Make command blocks on redstone ores execute command instantly instead of scheduling a 1gt delay TileTick event for execution",
			extra = "Only affects normal command blocks",
			category = {TIS, CREATIVE}
	)
	public static boolean instantCommandBlock = false;

	@Rule(
			desc = "The mobs in lazy chunks will not despawn",
			extra = "This option only have effects between Minecraft 1.15 and 1.16",
			category = {TIS, EXPERIMENTAL, FEATURE}
	)
	public static boolean keepMobInLazyChunks = false;

	/**
	 * Ported from fabric carpet 1.4.23
	 */
	@Rule(
			desc = "Changes maximum light tasks batch size",
			extra = {
					"Allows for a higher light suppression tolerance",
					"Setting it to 5 - Default limit defined by the game"
			},
			category = {EXPERIMENTAL, OPTIMIZATION},  // no TIS since it's a fabric-carpet backport
			strict = false,
			options = {"5", "50", "100", "200"},
			validate = LightBatchValidator.class
	)
	public static int lightEngineMaxBatchSize = 5;

	public static class LightBatchValidator extends Validator<Integer>
	{
		@Override
		public Integer validate(ServerCommandSource serverCommandSource, ParsedRule<Integer> parsedRule, Integer newValue, String string)
		{
			if (newValue > 0)
			{
				LightBatchSizeChanger.setSize(newValue);
				return newValue;
			}
			return null;
		}
	}

	@Rule(
			desc = "The sampling duration of light queue logger in game tick",
			extra = {
					"Affects all data except the queue size displayed in the logger"
			},
			validate = ValidatePositive.class,
			options = {"1", "20", "60", "100", "6000"},
			strict = false,
			category = {TIS}
	)
	public static int lightQueueLoggerSamplingDuration = 60;

	@Rule(
			desc = "Pause or disable light updates",
			extra = {
					"If set to suppressed, no light update can be executed which simulates light suppressor",
					"If set to ignored, no light update can be scheduled. It's useful for creating light errors",
					"If set to off, no light update can be scheduled or executed",
					"[WARNING] If set to suppressed or off, new chunks cannot be loaded. Then if the server tries to load chunk for player movement or whatever reason the server will be stuck forever"
			},
			category = {TIS, CREATIVE, EXPERIMENTAL},
			validate = ValidateLightUpdates.class
	)
	public static LightUpdateOptions lightUpdates = LightUpdateOptions.ON;

	private static class ValidateLightUpdates extends Validator<Enum<LightUpdateOptions>>
	{
		@Override
		public Enum<LightUpdateOptions> validate(ServerCommandSource source, ParsedRule<Enum<LightUpdateOptions>> currentRule, Enum<LightUpdateOptions> newValue, String string)
		{
			return LightThreadSynchronizer.checkRuleSafety(source, synchronizedLightThread, (LightUpdateOptions)newValue) ? newValue : null;
		}
	}

	public enum LightUpdateOptions
	{
		// Regular vanilla behavior
		ON(true, true),
		// Enqueue tasks, but never execute them. Might blocks the game forever
		SUPPRESSED(true, false),
		// Ignore all incoming tasks except ones created in method light, but already enqueued ones can be executed
		// TODO: Further testing to make sure it doesnt block the server
		IGNORED(false, true),
		// Ignore all incoming tasks and do not execute any tasks. Might blocks the game forever
		OFF(false, false);

		private final boolean shouldEnqueue;
		private final boolean shouldExecute;

		LightUpdateOptions(boolean shouldEnqueue, boolean shouldExecute)
		{
			this.shouldEnqueue = shouldEnqueue;
			this.shouldExecute = shouldExecute;
		}

		/**
		 * ON, SUPPRESSED: true
		 * OFF, IGNORE: false
		 */
		public boolean shouldEnqueueLightTask()
		{
			return this.shouldEnqueue;
		}

		/**
		 * ON, IGNORE: true
		 * OFF, SUPPRESSED: false
		 */
		public boolean shouldExecuteLightTask()
		{
			return this.shouldExecute;
		}
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
			desc = "Allow player to right click with dye item to mark a block to be logged by microTiming logger",
			extra = {
					"You need to subscribe to microTiming logger for marking or displaying blocks",
					"Right click with the same dye to switch the marker to end rod mode with which block update information will be logged additionally. Right click again to remove the marker",
					"Right click a marker with slime ball item to make it movable. It will move to the corresponding new position when the attaching block is moved by a piston",
					"Use `/carpet microTimingDyeMarker clear` to remove all markers",
					"You can create a named marker by using a renamed dye item. Marker name will be shown in logging message as well",
					"You can see boxes at marked blocks with fabric-carpet installed on your client. " +
							"With carpet-tis-addition installed the marker name could also be seen through blocks",
			},
			options = {"false", "true", "clear"},
			validate = ValidateMicroTimingDyeMarker.class,
			category = {TIS, CREATIVE}
	)
	public static String microTimingDyeMarker = "true";

	private static class ValidateMicroTimingDyeMarker extends Validator<String>
	{
		@Override
		public String validate(ServerCommandSource source, ParsedRule<String> currentRule, String newValue, String string)
		{
			if ("clear".equals(newValue))
			{
				MicroTimingMarkerManager.getInstance().clear();
				if (source != null)
				{
					Messenger.m(source, "w " + MicroTimingMarkerManager.getInstance().tr("cleared", "Marker cleared"));
				}
				return currentRule.get();
			}
			return newValue;
		}
	}

	@Rule(
			desc = "Modify the way to specify events to be logged in microTiming logger. Events labelled with dye marker are always logged",
			extra = {
					"labelled: Logs events labelled with wool",
					"in_range: Logs events within 32m of any player",
					"all: Logs every event. Use with caution",
					"marker_only: Logs event labelled with dye marker only. Use it with rule microTimingDyeMarker"
			},
			category = {TIS, CREATIVE}
	)
	public static MicroTimingTarget microTimingTarget = MicroTimingTarget.LABELLED;

	@Rule(
			desc = "Determine the way to divide game ticks",
			extra = {
					"world_timer: Divides at Overworld timer increment",
					"player_action: Divides at the beginning of player action"
			},
			category = {TIS, CREATIVE}
	)
	public static TickDivision microTimingTickDivision = TickDivision.WORLD_TIMER;

	@Rule(
			desc = "Disable some command to prevent accidentally cheating",
			extra = "Affects command list: /gamemode, /tp, /teleport, /give, /setblock, /summon",
			category = {TIS, SURVIVAL},
			validate = Validator._COMMAND.class  // for notifyPlayersCommandsChanged
	)
	public static boolean opPlayerNoCheat = false;

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
			desc = "Use a Mixin injection with higher priority for carpet rule optimizedTNT",
			extra = {
					"So the rule optimizedTNT can overwrite lithium's explosion optimization",
					"Of course rule optimizedTNT needs to be on for it to work"
			},
			category = {TIS, OPTIMIZATION, EXPERIMENTAL}
	)
	public static boolean optimizedTNTHighPriority = false;

	@Rule(
			desc = "Whether block changes will cause POI to updates or not",
			extra = {
					"Set it to false to disable POI updates"
			},
			category = {TIS, CREATIVE}
	)
	public static boolean poiUpdates = true;

	@Rule(
			desc = "Disable rail duping using old school pushing lit powered or activator rail method",
			category = {TIS, BUGFIX}
	)
	public static boolean railDupingFix = false;

	@Rule(
			desc = "Randomize the order for redstone dust to emit block updates",
			extra = "It's useful to test if your contraption is locational or not",
			category = {TIS, CREATIVE}
	)
	public static boolean redstoneDustRandomUpdateOrder = false;

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
			desc = "Ender dragon killed by charged creeper will drop dragon head",
			category = {TIS, FEATURE}
	)
	public static boolean renewableDragonHead = false;


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
			desc = "Halve the delay of redstone repeaters upon a redstone ore",
			extra = {
					"The delay will change from 2, 4, 6 or 8 game tick instead of 1, 2, 3 or 4 game tick"
			},
			category = {TIS, CREATIVE}
	)
	public static boolean repeaterHalfDelay = false;

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
			desc = "Structure block do not preserve existed fluid when placing waterlogged-able blocks",
			extra = "Has a side effect of suppressing bug MC-130584 happening",
			category = {TIS, CREATIVE, BUGFIX}
	)
	public static boolean structureBlockDoNotPreserveFluid = false;

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
			return (newValue > 0 && newValue <= 65536) ? newValue : null;
		}
		public String description()
		{
			return "You must choose a value from 1 to 127";
		}
	}

	/**
	 * Ported from fabric carpet 1.4.25
	 */
	@Rule(
			desc = "Customizable Structure Block outline render distance",
			extra = "Required on client to work properly",
			options = {"96", "192", "2048"},
			category = {CREATIVE, CLIENT},
			strict = false,
			validate = Validator.NONNEGATIVE_NUMBER.class
	)
	public static double structureBlockOutlineDistance = 96.0D;

	@Rule(
			desc = "Synchronize lighting thread with the server thread",
			extra = {
					"so the light thread will not lag behind the main thread and get desynchronized",
					"The server will wait until all lighting tasks to be done at the beginning of each world ticking",
					"With this rule you can safely /tick warp without potential light suppression or lighting desynchronization"
			},
			category = {TIS, CREATIVE, EXPERIMENTAL},
			validate = ValidateSynchronizedLightThread.class
	)
	public static boolean synchronizedLightThread = false;

	private static class ValidateSynchronizedLightThread extends Validator<Boolean>
	{
		@Override
		public Boolean validate(ServerCommandSource source, ParsedRule<Boolean> currentRule, Boolean newValue, String string)
		{
			return LightThreadSynchronizer.checkRuleSafety(source, newValue, lightUpdates) ? newValue : null;
		}
	}

	@Rule(
			desc = "Modify the limit of executed tile tick events per game tick",
			options = {"1024", "65536", "2147483647"},
			validate = ValidatePositive.class,
			strict = false,
			category = {TIS, CREATIVE}
	)
	public static int tileTickLimit = 65536;

	@Rule(
			desc = "Disable TNT, carpet and part of rail dupers",
			extra = {
					"Attachment block update based dupers will do nothing and redstone component update based dupers can no longer keep their duped block",
					"Dupe bad dig good"
			},
			category = {TIS, BUGFIX, EXPERIMENTAL}
	)
	public static boolean tntDupingFix = false;

	@Rule(
			desc = "Overwrite the default fuse duration of TNT",
			extra = "This might also affect the fuse duration of TNT ignited in explosion",
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
			desc = "Prevent TNT blocks from being ignited from redstone",
			extra = "You can still use explosion etc. to ignite a tnt",
			category = {TIS, CREATIVE}
	)
	public static boolean tntIgnoreRedstoneSignal = false;

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
			desc = "Disable all block updates and state updates",
			category = {TIS, CREATIVE}
	)
	public static boolean totallyNoBlockUpdate = false;

	@Rule(
			desc = "Disable turtle egg trampled to broken",
			category = {TIS, CREATIVE}
	)
	public static boolean turtleEggTrampledDisabled = false;

	@Rule(
			desc = "Enable visualize projectile logger",
			extra = {
					"Try /log projectiles visualize"
			},
			category = {TIS, CREATIVE}
	)
	public static boolean visualizeProjectileLoggerEnabled = false;

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

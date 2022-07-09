package carpettisaddition;

//#if MC < 11600
import carpettisaddition.helpers.rule.lightEngineMaxBatchSize.LightBatchSizeChanger;
//#endif

import carpet.settings.ParsedRule;
import carpet.settings.Validator;
import carpettisaddition.helpers.rule.synchronizedLightThread.LightThreadSynchronizer;
import carpettisaddition.logging.loggers.microtiming.enums.MicroTimingTarget;
import carpettisaddition.logging.loggers.microtiming.enums.TickDivision;
import carpettisaddition.logging.loggers.microtiming.marker.MicroTimingMarkerManager;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.MixinUtil;
import carpettisaddition.utils.settings.Rule;
import carpettisaddition.utils.settings.Validators;
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

	@Rule(categories = {TIS, CREATIVE, SURVIVAL})
	public static boolean antiSpamDisabled = false;

	public static final double VANILLA_BLOCK_EVENT_PACKET_RANGE = 64.0D;
	@Rule(
			validators = Validators.NonNegativeNumber.class,
			options = {"0", "16", "64", "128"},
			strict = false,
			categories = {TIS, OPTIMIZATION}
	)
	public static double blockEventPacketRange = VANILLA_BLOCK_EVENT_PACKET_RANGE;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean blockPlacementIgnoreEntity = false;

	@Rule(categories = {TIS, BUGFIX})
	public static boolean cauldronBlockItemInteractFix = false;

	public static final int VANILLA_CHUNK_UPDATE_PACKET_THRESHOLD = 64;
	public static final int MAXIMUM_CHUNK_UPDATE_PACKET_THRESHOLD = 65536;
	@Rule(
			validators = ValidateChunkUpdatePacketThreshold.class,
			options = {"64", "4096", "65536"},
			strict = false,
			categories = {TIS, OPTIMIZATION, EXPERIMENTAL}
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
			options = {"0", "1", "10", "100", "1000"},
			validators = Validators.NonNegativeNumber.class,
			strict = false,
			categories = {TIS, CREATIVE}
	)
	public static int chunkTickSpeed = 1;

	@Rule(categories = {TIS, BUGFIX})
	public static boolean clientSettingsLostOnRespawnFix = false;

	@Rule(categories = {TIS, COMMAND})
	public static String commandLifeTime = "true";

	@Rule(categories = {TIS, COMMAND, EXPERIMENTAL})
	public static String commandManipulate = "false";

	@Rule(categories = {TIS, COMMAND})
	public static String commandRaid = "true";

	@Rule(categories = {TIS, COMMAND})
	public static String commandRefresh = "true";

	@Rule(categories = {TIS, COMMAND, CREATIVE})
	public static String commandRemoveEntity = "ops";

	@Rule(categories = {TIS, CREATIVE})
	public static boolean creativeOpenContainerForcibly = false;

	@Rule(categories = {TIS, DISPENSER, CREATIVE})
	public static boolean dispenserNoItemCost = false;

	@Rule(categories = {TIS, FEATURE, DISPENSER})
	public static boolean dispensersFireDragonBreath = false;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean enchantCommandNoRestriction = false;

	@Rule(categories = {TIS, EXPERIMENTAL})
	public static boolean entityMomentumLoss = true;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean entityPlacementIgnoreCollision = false;

	@Rule(
			options = {"-1", "16", "64"},
			strict = false,
			categories = {TIS, CREATIVE}
	)
	public static int entityTrackerDistance = -1;

	@Rule(
			options = {"-1", "1"},
			strict = false,
			categories = {TIS, CREATIVE}
	)
	public static int entityTrackerInterval = -1;

	public static final double VANILLA_EXPLOSION_PACKET_RANGE = 64.0D;  // sqrt(4096)
	@Rule(
			validators = Validators.NonNegativeNumber.class,
			options = {"0", "16", "64", "128", "2048"},
			strict = false,
			categories = {TIS, CREATIVE}
	)
	public static double explosionPacketRange = VANILLA_EXPLOSION_PACKET_RANGE;

	@Rule(
			categories = {TIS, CREATIVE}
	)
	public static boolean failSoftBlockStateParsing = false;

	public static final String fakePlayerNameNoExtra = "#none";
	@Rule(
			options = {fakePlayerNameNoExtra, "bot_"},
			validators = ValidateFakePlayerNameExtra.class,
			strict = false,
			categories = {TIS, CARPET_MOD}
	)
	public static String fakePlayerNamePrefix = fakePlayerNameNoExtra;

	@Rule(
			options = {fakePlayerNameNoExtra, "_fake"},
			validators = ValidateFakePlayerNameExtra.class,
			strict = false,
			categories = {TIS, CARPET_MOD}
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
				Consumer<BaseText> messenger = msg -> Messenger.tell(source, Messenger.s(msg.getString(), "r"));
				messenger.accept(translator.tr(
						"_validator.ValidateFakePlayerNameExtra.warn.found", newValue,
						//#if MC >= 11901
						//$$ currentRule.name()
						//#else
						currentRule.name
						//#endif
				));
				if (!Objects.equals(this.lastDangerousInput.get(currentRule), newValue))
				{
					messenger.accept(translator.tr("_validator.ValidateFakePlayerNameExtra.warn.blocked"));
					this.lastDangerousInput.put(currentRule, newValue);
					return null;
				}
				messenger.accept(translator.tr("_validator.ValidateFakePlayerNameExtra.warn.applied"));
			}
			this.lastDangerousInput.remove(currentRule);
			return newValue;
		}
		public String description()
		{
			return "You must give a string without special characters and with a length from 1 to 16";
		}
	}

	@Rule(categories = {TIS, CREATIVE})
	public static boolean farmlandTrampledDisabled = false;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean fluidDestructionDisabled = false;

	@Rule(categories = {TIS, CREATIVE, CARPET_MOD})
	public static boolean hopperCountersUnlimitedSpeed = false;

	@Rule(categories = {TIS, CREATIVE, COMMAND})
	public static boolean hopperNoItemCost = false;

	@Rule(
			options = {"1", "5", "20", "100"},
			validators = ValidateHUDLoggerUpdateInterval.class,
			strict = false,
			categories = {TIS, CARPET_MOD}
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

	@Rule(categories = {TIS, CREATIVE})
	public static boolean instantCommandBlock = false;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean itemEntitySkipMovementDisabled = false;

	@Rule(categories = {TIS, FEATURE, EXPERIMENTAL})
	public static boolean keepMobInLazyChunks = false;

	@Rule(categories = {TIS, FEATURE, EXPERIMENTAL})
	public static boolean largeBarrel = false;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean lifeTimeTrackerConsidersMobcap = true;

	/**
	 * Ported from fabric carpet 1.4.23 to mc <1.16
	 */
	//#if MC < 11600
	@Rule(
			categories = {EXPERIMENTAL, OPTIMIZATION},  // no TIS since it's a fabric-carpet backport
			strict = false,
			options = {"5", "50", "100", "200"},
			validators = LightBatchValidator.class
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
	//#endif

	@Rule(
			validators = ValidatePositive.class,
			options = {"1", "20", "60", "100", "6000"},
			strict = false,
			categories = {TIS}
	)
	public static int lightQueueLoggerSamplingDuration = 60;

	@Rule(
			categories = {TIS, CREATIVE, EXPERIMENTAL},
			validators = ValidateLightUpdates.class
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
		@SuppressWarnings("BooleanMethodIsAlwaysInverted")
		public boolean shouldExecuteLightTask()
		{
			return this.shouldExecute;
		}
	}

	@Rule(categories = {TIS, CREATIVE})
	public static boolean microTiming = false;

	@Rule(
			options = {"false", "true", "clear"},
			validators = ValidateMicroTimingDyeMarker.class,
			categories = {TIS, CREATIVE}
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
					Messenger.tell(source, MicroTimingMarkerManager.getInstance().getTranslator().tr("cleared"));
				}
				return currentRule.get();
			}
			return newValue;
		}
	}

	@Rule(categories = {TIS, CREATIVE})
	public static MicroTimingTarget microTimingTarget = MicroTimingTarget.LABELLED;

	@Rule(categories = {TIS, CREATIVE})
	public static TickDivision microTimingTickDivision = TickDivision.WORLD_TIMER;

	public static final double VANILLA_MINECART_TAKE_PASSENGER_MIN_VELOCITY = 0.1D;  // sqrt(0.01)
	@Rule(
			options = {"0", "0.1", "NaN"},
			strict = false,
			categories = {TIS, CREATIVE}
	)
	public static double minecartTakePassengerMinVelocity = VANILLA_MINECART_TAKE_PASSENGER_MIN_VELOCITY;

	@Rule(categories = {TIS, CARPET_MOD})
	public static boolean mobcapsDisplayIgnoreMisc = false;

	@Rule(categories = {TIS, SURVIVAL, COMMAND})
	public static boolean opPlayerNoCheat = false;

	@Rule(categories = {TIS, OPTIMIZATION, EXPERIMENTAL})
	public static boolean optimizedFastEntityMovement = false;

	@Rule(categories = {TIS, OPTIMIZATION, EXPERIMENTAL})
	public static boolean optimizedHardHitBoxEntityCollision = false;

	@Rule(categories = {TIS, OPTIMIZATION, EXPERIMENTAL})
	public static boolean optimizedTNTHighPriority = false;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean poiUpdates = true;

	@Rule(categories = {TIS, CARPET_MOD})
	public static boolean persistentLoggerSubscription = false;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean preciseEntityPlacement = false;

	@Rule(categories = {TIS, BUGFIX})
	public static boolean railDupingFix = false;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean redstoneDustRandomUpdateOrder = false;

	@Rule(categories = {TIS, FEATURE})
	public static boolean renewableDragonEgg = false;

	@Rule(categories = {TIS, FEATURE})
	public static boolean renewableDragonHead = false;


	@Rule(
			options = {"0", "0.2", "1"},
			//#if MC >= 11500
			validators = Validators.Probablity.class,
			//#else
			//$$ validators = ValidatePossibility.class,
			//#endif
			strict = false,
			categories = {TIS, FEATURE}
	)
	public static double renewableElytra = 0.0D;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean repeaterHalfDelay = false;

	@Rule(categories = {TIS, BUGFIX})
	public static boolean sandDupingFix = false;

	public static final int VANILLA_SNOW_MELT_MIN_LIGHT_LEVEL = 12;
	@Rule(
			options = {"0", "10", "12"},
			strict = false,
			validators = Validators.NonNegativeNumber.class,
			categories = {TIS, CREATIVE}
	)
	public static int snowMeltMinLightLevel = VANILLA_SNOW_MELT_MIN_LIGHT_LEVEL;

	@Rule(categories = {TIS, CREATIVE, BUGFIX})
	public static boolean structureBlockDoNotPreserveFluid = false;

	/**
	 * Ported from fabric carpet 1.4.25 to mc <1.16
	 */
	//#if MC < 11600
	@Rule(
			validators = ValidateStructureBlockLimit.class,
			options = {"32", "64", "96", "127"},
			strict = false,
			categories = {TIS, CREATIVE}
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
	//#endif

	/**
	 * Ported from fabric carpet 1.4.25 to mc <1.16
	 */
	//#if MC < 11600
	@Rule(
			options = {"96", "192", "2048"},
			categories = {
					CREATIVE,
					//#if MC >= 11500
					CLIENT,
					//#endif
			},
			strict = false,
			validators = Validator.NONNEGATIVE_NUMBER.class
	)
	public static double structureBlockOutlineDistance = 96.0D;
	//#endif

	@Rule(
			categories = {TIS, CREATIVE, EXPERIMENTAL},
			validators = ValidateSynchronizedLightThread.class
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
			options = {"1024", "65536", "2147483647"},
			validators = ValidatePositive.class,
			strict = false,
			categories = {TIS, CREATIVE}
	)
	public static int tileTickLimit = 65536;

	@Rule(categories = {TIS, BUGFIX, EXPERIMENTAL})
	public static boolean tntDupingFix = false;

	@Rule(
			options = {"0", "80", "32767"},
			validators = ValidateTNTFuseDuration.class,
			strict = false,
			categories = {TIS, CREATIVE}
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

	@Rule(categories = {TIS, CREATIVE})
	public static boolean tntIgnoreRedstoneSignal = false;

	@Rule(categories = {TIS, FEATURE})
	public static boolean tooledTNT = false;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean totallyNoBlockUpdate = false;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean turtleEggTrampledDisabled = false;

	/**
	 * TISCM debugging rule
	 * - translation: Use fabric-carpet's rule "language" as the target langauge when translating text for a player
	 * - optimizedFastEntityMovement: Apply optimizedFastEntityMovement's logic to all entities while ignoring the velocity of the entity
	 *   by default the rule only applies to entities travel faster than a threshold value
	 * - mixin_audit: Triggers MixinEnvironment.getCurrentEnvironment().audit() which force-load all mixin targeted classes
	 */
	@Rule(
			options = {"false"},
			validators = ValidateUltraSecretSetting.class,
			strict = false,
			categories = {TIS, EXPERIMENTAL}
	)
	public static String ultraSecretSetting = "false";
	private static class ValidateUltraSecretSetting extends Validator<String>
	{
		@Override
		public String validate(ServerCommandSource source, ParsedRule<String> currentRule, String newValue, String string)
		{
			if (newValue.equals("mixin_audit"))
			{
				MixinUtil.audit(source);
				return currentRule.get();
			}
			return newValue;
		}
	}

	@Rule(categories = {TIS, CREATIVE})
	public static boolean visualizeProjectileLoggerEnabled = false;

	@Rule(
			options = {"-64", "-512", "-4096"},
			validators = ValidateNegative.class,
			strict = false,
			categories = {TIS, CREATIVE}
	)
	public static double voidRelatedAltitude = -64.0D;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean witherSpawnedSoundDisabled = false;

	@Rule(
			options = {"0", "1", "8", "32"},
			validators = ValidateXPTrackingDistance.class,
			strict = false,
			categories = {TIS, CREATIVE}
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

	// just use Validator.PROBABILITY in 1.15+
	// it doesn't exist in fabric-carpet 1.14.4 though
	//#if MC < 11500
	//$$ private static class ValidatePossibility extends Validator<Double>
	//$$ {
	//$$ 	@Override
	//$$ 	public Double validate(ServerCommandSource source, ParsedRule<Double> currentRule, Double newValue, String string)
	//$$ 	{
	//$$ 		return (newValue >= 0.0D && newValue <= 1.0D) ? newValue : null;
	//$$ 	}
	//$$ 	public String description()
	//$$ 	{
	//$$ 		return "You must choose a value from 0 to 1";
	//$$ 	}
	//$$ }
	//#endif

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

	private static class ValidateNegative extends Validator<Number>
	{
		@Override
		public Number validate(ServerCommandSource source, ParsedRule<Number> currentRule, Number newValue, String string)
		{
			return newValue.doubleValue() < 0.0D ? newValue : null;
		}
		public String description()
		{
			return "You must choose a negative value";
		}
	}
}

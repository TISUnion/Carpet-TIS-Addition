package carpettisaddition;

import carpet.settings.ParsedRule;
import carpettisaddition.helpers.rule.synchronizedLightThread.LightThreadSynchronizer;
import carpettisaddition.logging.loggers.microtiming.enums.MicroTimingTarget;
import carpettisaddition.logging.loggers.microtiming.enums.TickDivision;
import carpettisaddition.logging.loggers.microtiming.marker.MicroTimingMarkerManager;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingRuleListener;
import carpettisaddition.settings.Rule;
import carpettisaddition.settings.validator.*;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.MixinUtil;
import com.google.common.collect.Maps;
import net.minecraft.text.BaseText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static carpet.settings.RuleCategory.*;

//#if MC >= 11900
//$$ import carpettisaddition.helpers.rule.instantBlockUpdaterReintroduced.InstantBlockUpdaterReintroducedRuleListener;
//#endif

//#if MC < 11600
import carpettisaddition.helpers.rule.lightEngineMaxBatchSize.LightBatchSizeChanger;
//#endif

public class CarpetTISAdditionSettings
{
	public static final String TIS = "TIS";
	public static final String TISCM_PROTOCOL = "TISCM_PROTOCOL";
	public static final String CARPET_MOD = "carpet_mod";  // _ cannot be replaced by space, or you can't /carpet list this

	//#if MC < 11500
	//$$ private static final String CLIENT = "client";
	//#endif

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

	private static class ValidateChunkUpdatePacketThreshold extends RangedNumberValidator<Integer>
	{
		public ValidateChunkUpdatePacketThreshold()
		{
			super(2, MAXIMUM_CHUNK_UPDATE_PACKET_THRESHOLD);
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
	public static boolean creativeNetherWaterPlacement = false;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean creativeOpenContainerForcibly = false;

    @Rule(categories = {TIS, FEATURE})
    public static boolean deobfuscateCrashReportStackTrace = false;

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

	@Rule(categories = {TIS, CREATIVE})
	public static boolean explosionNoEntityInfluence = false;

	public static final double VANILLA_EXPLOSION_PACKET_RANGE = 64.0D;  // sqrt(4096)
	@Rule(
			validators = Validators.NonNegativeNumber.class,
			options = {"0", "16", "64", "128", "2048"},
			strict = false,
			categories = {TIS, CREATIVE}
	)
	public static double explosionPacketRange = VANILLA_EXPLOSION_PACKET_RANGE;

	@Rule(categories = {TIS, CREATIVE})
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

	private static class ValidateFakePlayerNameExtra extends AbstractCheckerValidator<String>
	{
		private final Map<ParsedRule<?>, String> lastDangerousInput = Maps.newHashMap();

		@Override
		protected boolean validateContext(ValidationContext<String> ctx)
		{
			if (!ctx.inputValue.equals(fakePlayerNameNoExtra) && !Pattern.matches("[a-zA-Z_0-9]{1,16}", ctx.inputValue) && ctx.source != null)
			{
				Consumer<BaseText> messenger = msg -> Messenger.tell(ctx.source, Messenger.s(msg.getString(), "r"));
				messenger.accept(tr("fake_player_name_extra.warn.found", ctx.inputValue, ctx.ruleName()));
				if (!Objects.equals(this.lastDangerousInput.get(ctx.rule), ctx.inputValue))
				{
					messenger.accept(tr("fake_player_name_extra.warn.blocked"));
					this.lastDangerousInput.put(ctx.rule, ctx.inputValue);
					return false;
				}
				messenger.accept(tr("fake_player_name_extra.warn.applied"));
			}
			this.lastDangerousInput.remove(ctx.rule);
			return true;
		}

		@Override
		public BaseText errorMessage(ValidationContext<String> ctx)
		{
			return tr("fake_player_name_extra.message");
		}
	}

	@Rule(categories = {TIS, CREATIVE})
	public static boolean farmlandTrampledDisabled = false;

	@Rule(categories = {TIS, CREATIVE, COMMAND})
	public static String fillCommandModeEnhance = "true";

	//#if MC >= 11900
	//$$ @Rule(categories = {TIS, CREATIVE})
	//$$ public static boolean flattenTriangularDistribution = false;
	//#endif

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
	private static class ValidateHUDLoggerUpdateInterval extends RangedNumberValidator<Integer>
	{
		public ValidateHUDLoggerUpdateInterval()
		{
			super(1, 1000);
		}
	}

	//#if MC >= 11900
	//$$ @Rule(categories = {TIS, CREATIVE}, validators = InstantBlockUpdaterReintroducedRuleListener.class)
	//$$ public static boolean instantBlockUpdaterReintroduced = false;
	//#endif

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

	public static class LightBatchValidator extends Validators.PositiveNumber<Integer>
	{
		@Override
		public void onRuleSet(ValidationContext<Integer> ctx, @NotNull Integer newValue)
		{
			LightBatchSizeChanger.setSize(newValue);
		}
	}
	//#endif

	@Rule(
			validators = Validators.PositiveNumber.class,
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

	private static class ValidateLightUpdates extends AbstractCheckerValidator<LightUpdateOptions>
	{
		@Override
		protected boolean validateContext(ValidationContext<LightUpdateOptions> ctx)
		{
			return LightThreadSynchronizer.checkRuleSafety(ctx.source, synchronizedLightThread, ctx.inputValue);
		}
	}

	public enum LightUpdateOptions
	{
		// Regular vanilla behavior
		ON(true, true),
		// Enqueue tasks, but never execute them. Might blocks the game forever
		SUPPRESSED(true, false),
		// Ignore all incoming tasks except ones created in method light, but already enqueued ones can be executed
		IGNORED(false, true),
		// Ignore all incoming tasks and do not execute any tasks. Might block the game forever
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

	@Rule(categories = {TIS, CREATIVE}, validators = MicroTimingRuleListener.class)
	public static boolean microTiming = false;

	@Rule(
			options = {"false", "true", "clear"},
			validators = ValidateMicroTimingDyeMarker.class,
			categories = {TIS, CREATIVE}
	)
	public static String microTimingDyeMarker = "true";

	private static class ValidateMicroTimingDyeMarker extends AbstractValidator<String>
	{
		@Override
		protected @Nullable String validate(ValidationContext<String> ctx)
		{
			if ("clear".equals(ctx.inputValue))
			{
				MicroTimingMarkerManager.getInstance().clear();
				if (ctx.source != null)
				{
					Messenger.tell(ctx.source, MicroTimingMarkerManager.getInstance().getTranslator().tr("cleared"));
				}
				return ctx.rule.get();
			}
			return ctx.inputValue;
		}
	}

	@Rule(categories = {TIS, CREATIVE},  validators = MicroTimingTargetValidator.class)
	public static MicroTimingTarget microTimingTarget = MicroTimingTarget.MARKER_ONLY;

	public static class MicroTimingTargetValidator extends RuleChangeListener<MicroTimingTarget>
	{
		@Override
		public void onRuleSet(ValidationContext<MicroTimingTarget> ctx, @NotNull MicroTimingTarget newValue)
		{
			if (newValue != MicroTimingTarget.MARKER_ONLY)
			{
				ctx.optionalSource().ifPresent(MicroTimingTarget::deprecatedWarning);
			}
		}
	}

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

	@Rule(categories = {TIS, CREATIVE})
	public static boolean observerNoDetection = false;

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
			validators = Validators.Probability.class,
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
	private static class ValidateStructureBlockLimit extends RangedNumberValidator<Integer>
	{
		public ValidateStructureBlockLimit()
		{
			super(1, 65536);
		}
	}
	//#endif

	/**
	 * Ported from fabric carpet 1.4.25 to mc <1.16
	 */
	//#if MC < 11600
	@Rule(
			options = {"96", "192", "2048"},
			categories = {CREATIVE, CLIENT},
			strict = false,
			validators = Validators.NonNegativeNumber.class
	)
	public static double structureBlockOutlineDistance = 96.0D;
	//#endif

	@Rule(
			categories = {TIS, CREATIVE, EXPERIMENTAL},
			validators = ValidateSynchronizedLightThread.class
	)
	public static boolean synchronizedLightThread = false;

	private static class ValidateSynchronizedLightThread extends AbstractCheckerValidator<Boolean>
	{
		@Override
		protected boolean validateContext(ValidationContext<Boolean> ctx)
		{
			return LightThreadSynchronizer.checkRuleSafety(ctx.source, ctx.inputValue, lightUpdates);
		}
	}

	@Rule(categories = {TIS, TISCM_PROTOCOL})
	public static boolean syncServerMsptMetricsData = false;

	@Rule(
			options = {"1024", "65536", "2147483647"},
			validators = Validators.PositiveNumber.class,
			strict = false,
			categories = {TIS, CREATIVE}
	)
	public static int tileTickLimit = 65536;

	@Rule(categories = {TIS, TISCM_PROTOCOL})
	public static boolean tiscmNetworkProtocol = false;

	@Rule(categories = {TIS, BUGFIX, EXPERIMENTAL})
	public static boolean tntDupingFix = false;

	@Rule(
			options = {"0", "80", "32767"},
			validators = ValidateTNTFuseDuration.class,
			strict = false,
			categories = {TIS, CREATIVE}
	)
	public static int tntFuseDuration = 80;

	private static class ValidateTNTFuseDuration extends RangedNumberValidator<Integer>
	{
		public ValidateTNTFuseDuration()
		{
			super(0, (int)Short.MAX_VALUE);
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
	private static class ValidateUltraSecretSetting extends AbstractValidator<String>
	{
		@Override
		protected @Nullable String validate(ValidationContext<String> ctx)
		{
			if (ctx.inputValue.equals("mixin_audit"))
			{
				MixinUtil.audit(ctx.source);
				return ctx.rule.get();
			}
			return ctx.inputValue;
		}
	}

	@Rule(categories = {TIS, CREATIVE})
	public static boolean undeadDontBurnInSunlight = false;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean visualizeProjectileLoggerEnabled = false;

	public static final double VANILLA_VOID_DAMAGE_AMOUNT = 4.0;
	@Rule(
			options = {"0", "4", "1000"},
			validators = Validators.NonNegativeNumber.class,
			strict = false,
			categories = {TIS, CREATIVE}
	)
	public static double voidDamageAmount = VANILLA_VOID_DAMAGE_AMOUNT;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean voidDamageIgnorePlayer = false;

	@Rule(
			options = {"-64", "-512", "-4096"},
			validators = Validators.NegativeNumber.class,
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
	private static class ValidateXPTrackingDistance extends RangedNumberValidator<Double>
	{
		public ValidateXPTrackingDistance()
		{
			super(0.0D, 128.0D);
		}
	}

	/**
	 * Do the same thing as rule updateSuppressionCrashFix from fabric carpet 1.4.50
	 * TODO: find a way to make both rules work at the same time without breaking any functionalities. (@Redirect conflicts)
	 */
	//#if MC < 11700
	@Rule(categories = {TIS, BUGFIX})
	public static boolean yeetUpdateSuppressionCrash = false;
	//#endif
}

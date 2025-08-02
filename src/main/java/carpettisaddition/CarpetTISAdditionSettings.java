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

package carpettisaddition;

import carpettisaddition.commands.xcounter.HopperXpCountersRuleListener;
import carpettisaddition.helpers.rule.fakePlayerNameExtra.FakePlayerNameExtraValidator;
import carpettisaddition.helpers.rule.synchronizedLightThread.LightThreadSynchronizer;
import carpettisaddition.helpers.rule.updateSuppressionSimulator.UpdateSuppressionSimulator;
import carpettisaddition.helpers.rule.voidDamageIgnorePlayer.VoidDamageIgnorePlayerValidator;
import carpettisaddition.logging.loggers.microtiming.enums.MicroTimingTarget;
import carpettisaddition.logging.loggers.microtiming.enums.TickDivision;
import carpettisaddition.logging.loggers.microtiming.marker.MicroTimingDyeMarkerRuleValidator;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingRuleListener;
import carpettisaddition.settings.Rule;
import carpettisaddition.settings.validator.*;
import carpettisaddition.utils.MixinUtils;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static carpet.settings.RuleCategory.*;

//#if MC >= 11900
//$$ import carpettisaddition.helpers.rule.instantBlockUpdaterReintroduced.InstantBlockUpdaterReintroducedRuleListener;
//#endif

//#if MC < 11600
import carpettisaddition.helpers.rule.lightEngineMaxBatchSize.LightBatchSizeChanger;
//#endif

@SuppressWarnings("unused")
public class CarpetTISAdditionSettings
{
	public static boolean isLoadingRulesFromConfig = false;

	// ============================== TISCM Rule Categories ==============================

	public static final String TIS = "TIS";
	public static final String TISCM_PROTOCOL = "TISCM_protocol";
	public static final String LOGGER = "logger";  // see carpettisaddition.mixins.carpet.tweaks.loggerRestriction.ParsedRuleMixin
	public static final String CARPET_MOD = "carpet_mod";  // _ cannot be replaced by space, or you can't /carpet list this
	public static final String PORTING = "porting";

	//#if MC < 11500
	//$$ private static final String CLIENT = "client";
	//#endif

	// ============================== TISCM Rules ==============================

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

	@Rule(categories = {TIS, CREATIVE})
	public static boolean breedingCooldownDisabled = false;

	@Rule(categories = {TIS, BUGFIX})
	public static boolean cauldronBlockItemInteractFix = false;

	@Rule(categories = {TIS, CLIENT})
	public static boolean chatMessageLengthLimitUnlocked = false;

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
	public static String commandRaycast = "ops";

	@Rule(categories = {TIS, COMMAND})
	public static String commandRefresh = "true";

	@Rule(categories = {TIS, COMMAND, CREATIVE})
	public static String commandRemoveEntity = "ops";

	@Rule(categories = {TIS, COMMAND, CREATIVE})
	public static String commandSleep = "ops";

	@Rule(categories = {TIS, COMMAND, TISCM_PROTOCOL})
	public static String commandSpeedTest = "false";

	@Rule(categories = {TIS, CREATIVE})
	public static boolean creativeInstantTame = false;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean creativeNetherWaterPlacement = false;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean creativeNoItemCooldown = false;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean creativeOpenContainerForcibly = false;

	@Rule(categories = {TIS, CLIENT})
	public static boolean debugNbtQueryNoPermission = false;

	@Rule(categories = {TIS, FEATURE})
	public static boolean deobfuscateCrashReportStackTrace = false;

	@Rule(categories = {TIS, DISPENSER, CREATIVE})
	public static boolean dispenserNoItemCost = false;

	@Rule(categories = {TIS, FEATURE, DISPENSER})
	public static boolean dispensersFireDragonBreath = false;

	//#if MC >= 12000
	//$$ @Rule(categories = {TIS, FEATURE, PORTING})
	//$$ public static boolean dustTrapdoorReintroduced = false;
	//#endif

	@Rule(categories = {TIS, CREATIVE})
	public static boolean enchantCommandNoRestriction = false;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean endPortalOpenedSoundDisabled = false;

	@Rule(categories = {TIS, BUGFIX})
	public static boolean entityBrainMemoryUnfreedFix = false;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean entityInstantDeathRemoval = false;

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
			validators = FakePlayerNameExtraValidator.class,
			strict = false,
			categories = {TIS, CARPET_MOD}
	)
	public static String fakePlayerNamePrefix = fakePlayerNameNoExtra;

	@Rule(
			options = {fakePlayerNameNoExtra, "_fake"},
			validators = FakePlayerNameExtraValidator.class,
			strict = false,
			categories = {TIS, CARPET_MOD}
	)
	public static String fakePlayerNameSuffix = fakePlayerNameNoExtra;

	@Rule(categories = {TIS, CARPET_MOD})
	public static boolean fakePlayerTicksLikeRealPlayer = false;

	@Rule(categories = {TIS, CARPET_MOD}, validators = PermissionLevelValidator.class)
	public static String fakePlayerRemoteSpawning = "true";

	@Rule(categories = {TIS, CREATIVE})
	public static boolean farmlandTrampledDisabled = false;

	@Rule(categories = {TIS, CREATIVE, COMMAND})
	public static String fillCommandModeEnhance = "true";

	//#if MC >= 11802 && MC < 12105
	//$$ @Rule(categories = {TIS, BUGFIX})
	//$$ public static boolean fortressNetherBricksPackSpawningFix = false;
	//#endif

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
			categories = {TIS, CREATIVE, COMMAND},
			validators = HopperXpCountersRuleListener.class
	)
	public static boolean hopperXpCounters = false;

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
	//$$ @Rule(categories = {TIS, CREATIVE, PORTING}, validators = InstantBlockUpdaterReintroducedRuleListener.class)
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
			validators = ValidateLightBatch.class
	)
	public static int lightEngineMaxBatchSize = 5;

	public static class ValidateLightBatch extends Validators.PositiveNumber<Integer>
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

	@Rule(categories = {TIS, LOGGER})
	public static String loggerMovement = "ops";

	@Rule(
			categories = {TIS, COMMAND, EXPERIMENTAL},
			validators = Validators.PositiveNumber.class,
			options = {"1000", "1000000", "1000000000"},
			strict = false
	)
	public static int manipulateBlockLimit = 1000000;

	@Rule(categories = {TIS, CREATIVE}, validators = MicroTimingRuleListener.class)
	public static boolean microTiming = false;

	@Rule(
			options = {"false", "true", "clear"},
			validators = MicroTimingDyeMarkerRuleValidator.class,
			categories = {TIS, CREATIVE}
	)
	public static String microTimingDyeMarker = "true";

	@Rule(categories = {TIS, CREATIVE},  validators = ValidateMicroTimingTarget.class)
	public static MicroTimingTarget microTimingTarget = MicroTimingTarget.MARKER_ONLY;

	public static class ValidateMicroTimingTarget extends RuleChangeListener<MicroTimingTarget>
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

	@Rule(categories = {TIS, FEATURE, PORTING})
	public static boolean minecartFullDropBackport = false;

	@Rule(categories = {TIS, CREATIVE, FEATURE})
	public static boolean minecartPlaceableOnGround = false;

	public static final double VANILLA_MINECART_TAKE_PASSENGER_MIN_VELOCITY = 0.1D;  // sqrt(0.01)
	@Rule(
			options = {"0", "0.1", "NaN"},
			strict = false,
			categories = {TIS, CREATIVE}
	)
	public static double minecartTakePassengerMinVelocity = VANILLA_MINECART_TAKE_PASSENGER_MIN_VELOCITY;

	@Rule(categories = {TIS, CARPET_MOD})
	public static boolean mobcapsDisplayIgnoreMisc = false;

	//#if MC >= 11900
	//$$ @Rule(categories = {TIS, FEATURE})
	//$$ public static boolean moveableReinforcedDeepslate = false;
	//#endif

	@Rule(categories = {TIS, FEATURE, PORTING})
	public static boolean naturalSpawningUse13Heightmap = false;

	@Rule(categories = {TIS, FEATURE, PORTING})
	public static boolean naturalSpawningUse13HeightmapExtra = false;

	//#if 12105 <= MC && MC < 12106
	//$$ @Rule(categories = {TIS, FEATURE, PORTING})
	//$$ public static boolean netherPortalEntityInteractionCheckUseFullBlockShape = false;
	//#endif

	public static final int VANILLA_NETHER_PORTAL_MAX_SIZE = 21;
	@Rule(
			categories = {TIS, CREATIVE},
			validators = ValidateNetherPortalMaxSize.class,
			options = {"21", "64", "128", "384"},
			strict = false
	)
	public static int netherPortalMaxSize = VANILLA_NETHER_PORTAL_MAX_SIZE;
	private static class ValidateNetherPortalMaxSize extends RangedNumberValidator<Integer>
	{
		public ValidateNetherPortalMaxSize()
		{
			super(2, 384);
		}
	}

	@Rule(
			categories = {TIS, CREATIVE},
			validators = OptionalPercentValidator.class,
			options = {"-1", "0", "50", "100"},
			strict = false
	)
	public static int oakBalloonPercent = -1;

	//#if MC < 12100
	@Rule(categories = {TIS, FEATURE, PORTING})
	public static boolean obsidianPlatformBlockBreakerBackport = false;
	//#endif

	@Rule(categories = {TIS, CREATIVE})
	public static boolean observerNoDetection = false;

	@Rule(categories = {TIS, SURVIVAL, COMMAND})
	public static boolean opPlayerNoCheat = false;

	@Rule(categories = {TIS, OPTIMIZATION, EXPERIMENTAL})
	public static boolean optimizedFastEntityMovement = false;

	@Rule(
			categories = {TIS, OPTIMIZATION, EXPERIMENTAL},
			restrictions = @Restriction(conflict = @Condition(ModIds.async))  // https://github.com/TISUnion/Carpet-TIS-Addition/issues/182
	)
	public static boolean optimizedHardHitBoxEntityCollision = false;

	@Rule(categories = {TIS, OPTIMIZATION, EXPERIMENTAL})
	public static boolean optimizedTNTHighPriority = false;

	//#if MC >= 11600
	//$$ @Rule(categories = {TIS, FEATURE, PORTING})
	//$$ public static boolean overspawningReintroduced = false;
	//#endif

	@Rule(categories = {TIS, CREATIVE})
	public static boolean poiUpdates = true;

	@Rule(categories = {TIS, CARPET_MOD})
	public static boolean persistentLoggerSubscription = false;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean preciseEntityPlacement = false;

	//#if MC < 11602
	@Rule(categories = {TIS, BUGFIX})
	public static boolean railDupingFix = false;
	//#endif

	@Rule(categories = {TIS, CREATIVE})
	public static boolean redstoneDustRandomUpdateOrder = false;

	//#if MC >= 12002
	//$$ @Rule(categories = {TIS, FEATURE, PORTING})
	//$$ public static boolean redstoneDustRepeaterComparatorIgnoreUpwardsStateUpdate = false;
	//#endif

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

	//#if MC >= 12002
	//$$ @Rule(categories = {TIS, FEATURE, PORTING})
	//$$ public static boolean shulkerBoxCCEReintroduced = false;
	//#endif

	//#if MC < 11700
	@Rule(categories = {TIS, FEATURE, PORTING})
	public static boolean shulkerBoxContentDropBackport = false;
	//#endif

	public static final int VANILLA_SNOW_MELT_MIN_LIGHT_LEVEL = 12;
	@Rule(
			options = {"0", "10", "12"},
			strict = false,
			validators = Validators.NonNegativeNumber.class,
			categories = {TIS, CREATIVE}
	)
	public static int snowMeltMinLightLevel = VANILLA_SNOW_MELT_MIN_LIGHT_LEVEL;

	@Rule(
			categories = {TIS, CREATIVE},
			validators = OptionalProbablyValidator.class,
			options = {"-1", "0", "0.5", "1"},
			strict = false
	)
	public static double spawnBabyProbably = -1;

	@Rule(
			categories = {TIS, CREATIVE},
			validators = OptionalProbablyValidator.class,
			options = {"-1", "0", "0.5", "1"},
			strict = false
	)
	public static double spawnJockeyProbably = -1;

	@Rule(
			options = {"10", "100", "1024", "10240"},
			strict = false,
			validators = Validators.PositiveNumber.class,
			categories = {TIS, COMMAND}
	)
	public static int speedTestCommandMaxTestSize = 10;

	@Rule(categories = {TIS, COMMAND})
	public static boolean stopCommandDoubleConfirmation = false;

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

	//#if MC >= 12003
	//$$ @Rule(categories = {TIS, CARPET_MOD, COMMAND, PORTING})
	//$$ public static boolean tickCommandCarpetfied = false;
	//$$
	//$$ @Rule(categories = {TIS, CARPET_MOD, COMMAND})
	//$$ public static boolean tickCommandEnhance = false;
	//$$
	//$$ public static final String VANILLA_TICK_COMMAND_PERMISSION = "3";
	//$$ @Rule(categories = {TIS, CARPET_MOD, COMMAND})
	//$$ public static String tickCommandPermission = VANILLA_TICK_COMMAND_PERMISSION;
	//$$
	//$$ @Rule(categories = {TIS, CARPET_MOD, COMMAND, PORTING})
	//$$ public static boolean tickFreezeCommandToggleable = false;
	//$$
	//$$ @Rule(categories = {TIS, CARPET_MOD, COMMAND, PORTING})
	//$$ public static boolean tickProfilerCommandsReintroduced = false;
	//$$
	//$$ @Rule(categories = {TIS, CARPET_MOD, COMMAND, PORTING})
	//$$ public static boolean tickWarpCommandAsAnAlias = false;
	//#endif

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

	public static final int VANILLA_TNT_FUSE_DURATION = 80;
	@Rule(
			options = {"0", "80", "32767"},
			validators = ValidateTNTFuseDuration.class,
			strict = false,
			categories = {TIS, CREATIVE}
	)
	public static int tntFuseDuration = VANILLA_TNT_FUSE_DURATION;

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
	public static boolean toughWitherRose = false;

	@Rule(categories = {TIS, CREATIVE})
	public static boolean turtleEggTrampledDisabled = false;

	/**
	 * TISCM debugging rule
	 * - translation: Use fabric-carpet's rule "language" as the target language when translating text for a player
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
				MixinUtils.audit(ctx.source);
				return ctx.rule.get();
			}
			// stupid COMMAND category will add a _COMMAND_LEVEL_VALIDATOR to the rule which is bad,
			// so we'll rather do the command tree sync ourselves
			if (ctx.source != null)
			{
				//#if MC >= 11901
				//$$ carpet.utils.CommandHelper.notifyPlayersCommandsChanged(CarpetTISAdditionServer.minecraft_server);
				//#else
				if (carpet.CarpetServer.settingsManager != null)
				{
					carpet.CarpetServer.settingsManager.notifyPlayersCommandsChanged();
				}
				//#endif
			}
			return ctx.inputValue;
		}
	}

	@Rule(categories = {TIS, CREATIVE})
	public static boolean undeadDontBurnInSunlight = false;

	@Rule(
            categories = {TIS, CREATIVE},
			validators = UpdateSuppressionSimulatorValidator.class,
            options = {"false", "true", "StackOverflowError", "OutOfMemoryError", "ClassCastException", "IllegalArgumentException"}
	)
	public static String updateSuppressionSimulator = "false";
	private static class UpdateSuppressionSimulatorValidator extends AbstractCheckerValidator<String>
	{
		@Override
		protected boolean validateValue(String value)
		{
            return UpdateSuppressionSimulator.checkRule(value);
		}

		@Override
		public void onRuleSet(ValidationContext<String> ctx, String newValue)
		{
			UpdateSuppressionSimulator.acceptRule(newValue);
		}
	}

	//#if MC >= 12006
	//$$ @Rule(categories = {TIS, CREATIVE})
	//$$ public static boolean vaultBlacklistDisabled = false;
	//#endif

	@Rule(categories = {TIS, CREATIVE})
	public static ViolentNetherPortalCreationOptions violentNetherPortalCreation = ViolentNetherPortalCreationOptions.FALSE;

	public enum ViolentNetherPortalCreationOptions
	{
		FALSE, REPLACEABLE, ALL
	}

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

	@Rule(
			options = {"false", "true", "creative", "spectator", "creative,spectator"},
			validators = VoidDamageIgnorePlayerValidator.class,
			strict = false,
			categories = {TIS, CREATIVE}
	)
	public static String voidDamageIgnorePlayer = "false";

	@Rule(
			options = {"-64", "-512", "-4096"},
			validators = Validators.NegativeNumber.class,
			strict = false,
			categories = {TIS, CREATIVE}
	)
	public static double voidRelatedAltitude = -64.0D;

	@Rule(categories = {TIS, FEATURE, PORTING})
	public static boolean wetExplosionReintroduced;

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

	@Rule(categories = {TIS})
	public static boolean yeetAsyncTaskExecutionDelay = false;

	@Rule(categories = {TIS})
	public static boolean yeetIdleMspt = false;

	//#if MC >= 11900
	//$$ @Rule(categories = {TIS})
	//$$ public static boolean yeetOutOfOrderChatKick = false;
	//#endif

	@Rule(categories = {TIS, BUGFIX})
	public static boolean yeetUpdateSuppressionCrash = false;

	// ============================== TISCM Rules end ==============================

	/**
	 * Some rule checks to ensure that the worlds can be loaded normally
	 */
	public static void onWorldLoadingStarted()
	{
		if (!lightUpdates.shouldExecuteLightTask())
		{
			CarpetTISAdditionMod.LOGGER.warn("The rule lightUpdates is set to {} before the worlds are loaded", lightUpdates);
			CarpetTISAdditionMod.LOGGER.warn("This might cause the server to be blocked indefinitely");
		}
	}
}

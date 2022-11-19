package carpettisaddition.commands.lifetime.spawning;

import carpettisaddition.CarpetTISAdditionSettings;

import java.util.function.Supplier;

public enum SpawningType
{
	/**
	 * The mob is newly added to the world for the first time
	 */
	ADDED_TO_WORLD(() -> false),
	/**
	 * The mob is already in the world, and now it's added to the mobcap
	 */
	ADDED_TO_MOBCAP(() -> CarpetTISAdditionSettings.lifeTimeTrackerConsidersMobcap);

	private final Supplier<Boolean> validSupplier;

	SpawningType(Supplier<Boolean> validSupplier)
	{
		this.validSupplier = validSupplier;
	}

	public boolean isDoubleRecordSupported()
	{
		return this.validSupplier.get();
	}
}

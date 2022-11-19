package carpettisaddition.commands.lifetime.removal;

import carpettisaddition.CarpetTISAdditionSettings;

import java.util.function.Supplier;

public enum RemovalType
{
	/**
	 * The mob is removed from the world and no longer exists
	 */
	REMOVED_FROM_WORLD(() -> true),
	/**
	 * The mob is removed from the mobcap, but still exists in the world
	 */
	REMOVED_FROM_MOBCAP(() -> CarpetTISAdditionSettings.lifeTimeTrackerConsidersMobcap);

	private final Supplier<Boolean> validSupplier;

	RemovalType(Supplier<Boolean> validSupplier)
	{
		this.validSupplier = validSupplier;
	}

	public boolean isValid()
	{
		return this.validSupplier.get();
	}
}

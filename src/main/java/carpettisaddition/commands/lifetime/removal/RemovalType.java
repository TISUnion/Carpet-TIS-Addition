package carpettisaddition.commands.lifetime.removal;

import carpettisaddition.CarpetTISAdditionSettings;

import java.util.function.Supplier;

public enum RemovalType
{
	REMOVED_FROM_WORLD(() -> true),
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

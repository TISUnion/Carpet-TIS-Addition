package carpettisaddition.logging.loggers.damage.interfaces;

import carpettisaddition.logging.loggers.damage.DamageLogger;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface DamageLoggerTarget
{
	Optional<DamageLogger.Tracker> getDamageTracker();

	void setDamageTracker(@Nullable DamageLogger.Tracker tracker);
}

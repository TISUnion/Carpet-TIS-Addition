package carpettisaddition.logging.loggers.movement;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface MovementLoggerTarget
{
	Optional<MovementLogger.Tracker> getMovementTracker();

	void setMovementTracker(@Nullable MovementLogger.Tracker tracker);
}

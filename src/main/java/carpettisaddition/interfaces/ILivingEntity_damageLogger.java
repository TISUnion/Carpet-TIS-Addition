package carpettisaddition.interfaces;

import carpettisaddition.logging.loggers.damage.DamageLogger;

import java.util.Optional;

public interface ILivingEntity_damageLogger
{
	Optional<DamageLogger> getDamageLogger();

	void setDamageLogger(DamageLogger damageLogger);
}

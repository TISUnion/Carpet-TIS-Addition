package carpettisaddition.logging.loggers.damage.interfaces;

import carpettisaddition.logging.loggers.damage.DamageLogger;

import java.util.Optional;

public interface ILivingEntity
{
	Optional<DamageLogger> getDamageLogger();

	void setDamageLogger(DamageLogger damageLogger);
}

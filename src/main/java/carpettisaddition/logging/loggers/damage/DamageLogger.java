package carpettisaddition.logging.loggers.damage;

import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.logging.loggers.damage.interfaces.DamageLoggerTarget;
import carpettisaddition.logging.loggers.damage.modifyreasons.Modification;
import carpettisaddition.logging.loggers.damage.modifyreasons.ModifyReason;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class DamageLogger extends AbstractLogger
{
	public static final String NAME = "damage";
	private static final DamageLogger INSTANCE = new DamageLogger();

	private DamageLogger()
	{
		super(NAME, false);
	}

	public static DamageLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	public @Nullable String[] getSuggestedLoggingOption()
	{
		return new String[]{"all", "players", "me", "->creeper", "vex->", "me->zombie", "Steve", "@e[distance=..10]"};
	}

	public static boolean isLoggerActivated()
	{
		return TISAdditionLoggerRegistry.__damage;
	}

	public static void create(LivingEntity entity, DamageSource source, float amount)
	{
		if (isLoggerActivated())
		{
			DamageLoggerTarget damageLoggerTarget = (DamageLoggerTarget)entity;
			if (!damageLoggerTarget.getDamageTracker().isPresent())
			{
				damageLoggerTarget.setDamageTracker(getInstance().new Tracker(entity, source, amount));
			}
		}
	}

	public class Tracker
	{
		private final LivingEntity entity;
		private final float initialHealth;
		private final DamageSource damageSource;
		private final float initialAmount;
		private float currentAmount;
		private final List<Modification> modificationList = Lists.newArrayList();
		private boolean valid;

		public Tracker(LivingEntity entity, DamageSource damageSource, float initialAmount)
		{
			this.entity = entity;
			this.initialHealth = entity.getHealth();
			this.damageSource = damageSource;
			this.initialAmount = initialAmount;
			this.currentAmount = initialAmount;
			this.valid = true;
		}

		public boolean isValid()
		{
			return this.valid;
		}

		public void modifyDamage(float newAmount, ModifyReason reason)
		{
			// to avoid spamming
			if (this.damageSource.isFire() && (this.entity.isFireImmune() || this.entity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)))
			{
				this.valid = false;
				return;
			}
			if (newAmount != this.currentAmount)
			{
				this.modificationList.add(new Modification(this.currentAmount, newAmount, reason));
				this.currentAmount = newAmount;
			}
			if (reason == ModifyReason.INVULNERABLE)  // no spam for creative player
			{
				this.valid = false;
			}
		}

		private BaseText[] verifyAndProduceMessage(String option, PlayerEntity player, Entity from, Entity to, Supplier<BaseText[]> messageFuture)
		{
			OptionParser parser = new OptionParser(option);
			if (parser.accepts(player, from, to))
			{
				return messageFuture.get();
			}
			return null;
		}

		private BaseText getAmountText(@Nullable String style, float amount)
		{
			String display = String.format("%.2f", amount);
			String detail = String.format("%.6f", amount);
			return Messenger.fancy(
					style,
					Messenger.s(display),
					Messenger.s(detail),
					new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, detail)
			);
		}

		public void flush(float finalAmount, float remainingHealth)
		{
			if (!isLoggerActivated() || !this.isValid())
			{
				return;
			}
			this.valid = false;
			Entity attacker = this.damageSource.getAttacker();
			Entity source = this.damageSource.getSource();
			LivingEntity target = this.entity;
			DamageLogger.this.log((option, player) ->
					this.verifyAndProduceMessage(option, player, attacker, target, () -> {
						List<Object> lines = Lists.newArrayList();
						lines.add(Messenger.s(" "));
						BaseText targetName = Messenger.entity("b", target);
						List<Object> sourceHoverTextList = Lists.newArrayList();
						if (source != null)
						{
							sourceHoverTextList.add(Messenger.c(tr("source"), "w : ", Messenger.entity(source)));
						}
						if (attacker != null)
						{
							if (!sourceHoverTextList.isEmpty())
							{
								sourceHoverTextList.add("w \n");
							}
							sourceHoverTextList.add(Messenger.c(tr("attacker"), "w : ", Messenger.entity(attacker)));
						}
						lines.add(tr(
								"header_message",
								targetName,
								getAmountText("r", this.initialAmount),
								Messenger.fancy(
										"w",
										Messenger.s(this.damageSource.getName()),
										sourceHoverTextList.isEmpty() ? null : Messenger.c(sourceHoverTextList.toArray(new Object[0])),
										attacker != null ? new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.tp(attacker)) : null
								),
								getAmountText(null, this.initialHealth)
						));
						for (Modification modification : this.modificationList)
						{
							float oldAmount = modification.getOldAmount();
							float newAmount = modification.getNewAmount();
							float delta = Math.abs(newAmount - oldAmount);
							String sig = newAmount > oldAmount ? "+" : "-";
							String radio = oldAmount != 0.0F ? String.format("%.1f%%", 100.0F * delta / oldAmount) : "N/A%";
							lines.add(Messenger.c(
									"g  - ",
									getAmountText("r", oldAmount), "g  -> ", getAmountText(newAmount > oldAmount ? "r" : "d", newAmount),
									String.format("g  (%s", sig),
									Messenger.hover(getAmountText("g", delta), Messenger.s(String.format("%s%.6f", sig, delta))),
									String.format("g , %s%s)", sig, radio),
									tr("due_to", modification.getReason().toText())
							));
						}
						lines.add(tr(
								"footer_message",
								targetName,
								getAmountText(finalAmount > 0.0F ? "r" : "w", finalAmount),
								getAmountText(remainingHealth > 0 ? "l" : "r", remainingHealth)
						));
						return lines.stream().map(Messenger::c).toArray(BaseText[]::new);
					})
			);
		}
	}
}

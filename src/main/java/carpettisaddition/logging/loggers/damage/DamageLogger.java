package carpettisaddition.logging.loggers.damage;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.interfaces.ILivingEntity_damageLogger;
import carpettisaddition.logging.ExtensionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.logging.loggers.damage.modifyreasons.Modification;
import carpettisaddition.logging.loggers.damage.modifyreasons.ModifyReason;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;

import java.util.List;
import java.util.function.Supplier;

public class DamageLogger extends AbstractLogger
{
	public static final String NAME = "damage";
	private static final Translator TRANSLATOR = (new DamageLogger(null, null, 0)).getTranslator();

	private final LivingEntity entity;
	private final DamageSource damageSource;
	private final float initialAmount;
	private float currentAmount;
	private final List<Modification> modificationList = Lists.newArrayList();
	private boolean valid;

	public DamageLogger(LivingEntity entity, DamageSource damageSource, float initialAmount)
	{
		super(NAME);
		this.entity = entity;
		this.damageSource = damageSource;
		this.initialAmount = initialAmount;
		this.currentAmount = initialAmount;
		this.valid = true;
	}

	public static boolean isLoggerActivated()
	{
		return ExtensionLoggerRegistry.__damage;
	}

	public static Translator getStaticTranslator()
	{
		return TRANSLATOR;
	}

	public boolean isValid()
	{
		return this.valid;
	}

	public static void create(LivingEntity entity, DamageSource source, float amount)
	{
		if (DamageLogger.isLoggerActivated())
		{
			ILivingEntity_damageLogger iEntity = (ILivingEntity_damageLogger)entity;
			if (!iEntity.getDamageLogger().isPresent())
			{
				iEntity.setDamageLogger(new DamageLogger(entity, source, amount));
			}
		}
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

	private static BaseText[] verifyAndProduceMessage(String option, PlayerEntity player, Entity from, Entity to, Supplier<BaseText[]> messageFuture)
	{
		if ("all".equals(option) || ("players".equals(option) && (from instanceof PlayerEntity || to instanceof PlayerEntity)) || ("me".equals(option) && (from == player || to == player)))
		{
			return messageFuture.get();
		}
		return null;
	}

	private static BaseText getAmountText(String style, float amount)
	{
		String display = String.format("%.2f", amount);
		String detail = String.format("%.6f", amount);
		return TextUtil.getFancyText(
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
		LoggerRegistry.getLogger(NAME).log((option, player) ->
				verifyAndProduceMessage(option, player, attacker, target, () -> {
					List<Object> lines = Lists.newArrayList();
					lines.add(Messenger.s(" "));
					BaseText sourceName = TextUtil.attachClickEvent(
							(BaseText)target.getDisplayName(),
							new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.getTeleportCommand(target))
					);
					List<Object> sourceHoverTextList = Lists.newArrayList();
					if (source != null)
					{
						sourceHoverTextList.add(Messenger.c(String.format("w %s: ", this.tr("Source")), source.getName()));
					}
					if (attacker != null)
					{
						if (!sourceHoverTextList.isEmpty())
						{
							sourceHoverTextList.add("w \n");
						}
						sourceHoverTextList.add(Messenger.c(String.format("w %s: ", this.tr("Attacker")), attacker.getName()));
					}
					lines.add(Messenger.c(
							sourceName,
							"g  " + this.tr("is receiving"),
							TextUtil.getSpaceText(),
							getAmountText("r", this.initialAmount),
							TextUtil.getSpaceText(),
							"g " + this.tr("damage"),
							String.format("g , %s: ", this.tr("damage type")),
							TextUtil.getFancyText(
									"w",
									Messenger.s(this.damageSource.getName()),
									sourceHoverTextList.isEmpty() ? null : Messenger.c(sourceHoverTextList.toArray(new Object[0])),
									attacker != null ? new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.getTeleportCommand(attacker)) : null
							)
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
								getAmountText("r", oldAmount),
								"g  -> ",
								getAmountText(newAmount > oldAmount ? "r" : "d", newAmount),
								String.format("g  (%s", sig),
								TextUtil.attachHoverText(getAmountText("g", delta), Messenger.s(String.format("%s%.6f", sig, delta))),
								String.format("g , %s%s) %s", sig, radio, this.tr("due to")),
								TextUtil.getSpaceText(),
								modification.getReason().toText()
						));
					}
					lines.add(Messenger.c(
							sourceName,
							"w  " + this.tr("actually received"),
							TextUtil.getSpaceText(),
							getAmountText(finalAmount > 0.0F ? "r" : "w", finalAmount),
							TextUtil.getSpaceText(),
							String.format("g %s, ", this.tr("damage")),
							String.format("w %s: ", this.tr("Remaining health")),
							getAmountText(remainingHealth > 0 ? "l" : "r", remainingHealth)
					));
					return lines.stream().map(Messenger::c).toArray(BaseText[]::new);
				})
		);
	}
}

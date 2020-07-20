package carpettisaddition.logging.logHelpers;

import carpet.utils.Messenger;
import carpettisaddition.utils.Util;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.BaseText;


public class ItemLogHelper extends EntityLogHelper<ItemEntity>
{
	public static ItemLogHelper inst = new ItemLogHelper();

	public ItemLogHelper()
	{
		super("item");
	}
	/*

	public static void onItemDie(ItemEntity item, DamageSource source, float amount)
	{
		inst.__onEntityDie(item, source, amount);
	}
	public static void onItemDespawn(ItemEntity item)
	{
		inst.__onEntityDespawn(item);
	}

	@Override
	protected BaseText getNameText(ItemEntity item)
	{
		BaseText text = super.getNameText(item);
		text.append("(").append(Util.getTranslatedName(item.getStack().getTranslationKey())).append(")");
		return text;
	}

	@Override
	protected BaseText getNameTextHoverText(ItemEntity item)
	{
		return Messenger.s(String.format("%s: %d", tr("Item stack size"), item.getStack().getCount()));
	}
	 */
}

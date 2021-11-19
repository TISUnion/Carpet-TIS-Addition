package carpettisaddition.commands.info.entity;

import carpettisaddition.mixins.command.info.entity.*;
import carpettisaddition.utils.DimensionWrapper;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;

/**
 * Make /info entity work again
 */
public class EntityInfoPorting
{
	/**
	 * From gnembon's carpet mod 1.13
	 */
	public static List<BaseText> entityInfo(Entity e, World source_world)
	{
		List<BaseText> lst = Lists.newArrayList();
		World world = e.getEntityWorld();
		lst.add(Messenger.c("w " + EntityInfoAccessor.entity_short_string(e)));
		if (e.hasVehicle()) { lst.add(Messenger.c("w  - Rides: ", "wb "+e.getVehicle().getDisplayName().getString())); }
		if (e.hasPassengers())
		{
			List<Entity> passengers = e.getPassengerList();
			if (passengers.size() == 1)
			{
				lst.add(Messenger.c("w  - Is being ridden by: ", "wb "+passengers.get(0).getDisplayName().getString()));
			}
			else
			{
				lst.add(Messenger.c("w  - Is being ridden by:"));
				for (Entity ei: passengers)
				{
					lst.add(Messenger.c("wb    * "+ ei.getDisplayName().getString()));
				}
			}
		}
		lst.add(Messenger.c(String.format("w  - Height: %.2f, Width: %.2f, Eye height: %.2f",e.getHeight(), e.getWidth(), e.getStandingEyeHeight())));
		lst.add(Messenger.c("w  - Age: ", "wb " + EntityInfoAccessor.makeTime(e.age)));
		if (!DimensionWrapper.of(source_world).equals(DimensionWrapper.of(e)))
		{
			lst.add(Messenger.c("w  - Dimension: ", "wb "+e.world.getRegistryKey().getValue()));
		}
		if (e.getFireTicks() > 0) { lst.add(Messenger.c("w  - Fire for ","wb "+e.getFireTicks(),"w  ticks")); }
		if (e.isFireImmune() ) { lst.add(Messenger.c("w  - Immune to fire")); }
		if (((EntityAccessor)e).getNetherPortalCooldown() > 0) { lst.add(Messenger.c("w  - Portal cooldown for ","wb "+((EntityAccessor)e).getNetherPortalCooldown()," ticks")); }
		if (e.isInvulnerable()) { lst.add(Messenger.c("w  - Invulnerable")); }
		if (e.isImmuneToExplosion()) { lst.add(Messenger.c("w  - Immune to explosions")); }

		if (e instanceof ItemEntity)
		{
			ItemEntity ei = (ItemEntity)e;
			ItemStack stack = ei.getStack();// getEntityItem();
			String stackname = stack.getCount()>1?String.format("%dx%s",stack.getCount(), stack.getName().getString()):stack.getName().getString();
			lst.add(Messenger.c("w  - Content: ", "wb "+stackname));
			lst.add(Messenger.c("w  - Despawn Timer: ", "wb " + EntityInfoAccessor.makeTime(((ItemEntityAccessor)ei).getItemAge())));
		}
		if (e instanceof ExperienceOrbEntity)
		{
			ExperienceOrbEntity exp = (ExperienceOrbEntity)e;
			lst.add(Messenger.c("w  - Despawn Timer: ", "wb "+EntityInfoAccessor.makeTime(((ExperienceOrbEntityAccessor)exp).getOrbAge())));
			lst.add(Messenger.c("w  - Xp Value: ", "wb "+exp.getExperienceAmount()));
		}
		if (e instanceof ItemFrameEntity)
		{
			ItemFrameEntity eif = (ItemFrameEntity)e;
			lst.add(Messenger.c("w  - Content: ", "wb "+eif.getHeldItemStack().getName()));
			lst.add(Messenger.c("w  - Rotation: ", "wb "+eif.getRotation()));
		}
		if (e instanceof PaintingEntity)
		{
			PaintingEntity ep = (PaintingEntity)e;
			lst.add(Messenger.c("w  - Art: ", "wb "+ep.motive.toString()));
		}

		if (e instanceof LivingEntity)  // EntityLivingBase
		{
			LivingEntity elb = (LivingEntity)e;
			lst.add(Messenger.c("w  - Despawn timer: ", "wb "+EntityInfoAccessor.makeTime(elb.getDespawnCounter())));

			lst.add(Messenger.c(String.format("w  - Health: %.2f/%.2f", elb.getHealth(), elb.getMaxHealth())));
			if (elb.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).getValue() > 0.0)
			{
				lst.add(Messenger.c(String.format("w  - Armour: %.1f",elb.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).getValue())));
			}
			if (elb.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).getValue() > 0.0)
			{
				lst.add(Messenger.c(String.format("w  - Toughness: %.1f",elb.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).getValue())));
			}
			//lst.add(Messenger.c(String.format("w  - Base speed: %.1fb/s",get_speed(elb.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()))));



			Collection<StatusEffectInstance> potions = elb.getStatusEffects();
			if (!potions.isEmpty())
			{
				lst.add(Messenger.c("w  - Potion effects:"));
				for (StatusEffectInstance pe : potions)
				{
					lst.add(Messenger.c(String.format("w    * %s%s %s",
							pe.getTranslationKey().substring(7),
							(pe.getAmplifier()>1)?String.format("x%d",pe.getAmplifier()):"",
							EntityInfoAccessor.makeTime(pe.getDuration()))));
				}
			}
			ItemStack mainhand = elb.getMainHandStack();
			if (!(mainhand.isEmpty()))
			{
				lst.add(Messenger.c("w  - Main hand: ", "wb "+EntityInfoAccessor.display_item(mainhand)));
			}
			ItemStack offhand = elb.getOffhandStack();
			if (!(offhand.isEmpty()))
			{
				lst.add(Messenger.c("w  - Off hand: ", "wb "+EntityInfoAccessor.display_item(offhand)));
			}
			String armour = "";
			for (ItemStack armourpiece: elb.getArmorItems())
			{
				if (!(armourpiece.isEmpty()))
				{
					armour += String.format("\n   * %s", EntityInfoAccessor.display_item(armourpiece));
				}
			}
			if (!("".equals(armour)))
			{
				lst.add(Messenger.c("w  - Armour:"+ armour));
			}
			if (e instanceof MobEntity)  // EntityLiving
			{
				MobEntity el = (MobEntity)elb;
				lst.add(Messenger.c(String.format("w  - Follow range: %.1f",el.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).getValue())));

				lst.add(Messenger.c(String.format("w  - Movement speed factor: %.2f",el.getMoveControl().getSpeed())));


				LivingEntity target_elb = el.getTarget();
				if (target_elb != null)
				{
					lst.add(Messenger.c("w  - Attack target: ", "wb "+EntityInfoAccessor.entity_short_string(target_elb)));
				}
				if (el.canPickUpLoot())
				{
					lst.add(Messenger.c("w  - Can pick up loot"));
				}
				if (el.isPersistent())
				{
					lst.add((Messenger.c("w  - Won't despawn")));
				}

				if (e instanceof WitherEntity)
				{
					WitherEntity ew = (WitherEntity)e;
					Entity etarget = world.getEntityById(ew.getTrackedEntityId(0));
					lst.add(Messenger.c("w  - Head 1 target: ", "wb "+EntityInfoAccessor.entity_short_string(etarget) ));
					etarget = world.getEntityById(ew.getTrackedEntityId(1));
					lst.add(Messenger.c("w  - Head 2 target: ", "wb "+EntityInfoAccessor.entity_short_string(etarget) ));
					etarget = world.getEntityById(ew.getTrackedEntityId(2));
					lst.add(Messenger.c("w  - Head 3 target: ", "wb "+EntityInfoAccessor.entity_short_string(etarget) ));
				}

				if (e instanceof PathAwareEntity)  // EntityCreature
				{
					PathAwareEntity ec = (PathAwareEntity) e;
					if (ec.hasPositionTarget())
					{
						BlockPos pos = ec.getPositionTarget();
						lst.add(Messenger.c("w  - Home position: ","wb "+(int)ec.getPositionTargetRange(),"w  blocks around ", Messenger.coord("wb", pos)));
					}

					if (e instanceof PassiveEntity)  // EntityAgeable
					{
						PassiveEntity eage = (PassiveEntity) e;
						if (eage.getBreedingAge() < 0)
						{
							lst.add(Messenger.c("w  - Time till adulthood: ", "wb "+EntityInfoAccessor.makeTime(-eage.getBreedingAge())));
						}
						if (eage.getBreedingAge() > 0)
						{
							lst.add(Messenger.c("w  - Mating cooldown: ", "wb "+EntityInfoAccessor.makeTime(eage.getBreedingAge())));
						}
						if (e instanceof VillagerEntity)
						{
							VillagerEntity ev = (VillagerEntity) e;

							SimpleInventory vinv = ev.getInventory();
							String inventory_content = "";
							for (int i = 0; i < ((SimpleInventoryAccessor)vinv).getStacks().size(); ++i)
							{
								ItemStack vstack = vinv.getStack(i);
								if (!vstack.isEmpty())
								{
									inventory_content += String.format("\n   * %d: %s", i, EntityInfoAccessor.display_item(vstack));
								}
							}
							if (!("".equals(inventory_content)))
							{
								lst.add(Messenger.c("w  - Inventory:"+ inventory_content));
							}
//							if (ev.getWealth()>0)
//							{
//								lst.add(Messenger.c("w  - Wealth: ", "lb "+ev.getWealth()+" emeralds" ));
//							}
						}
						if (e instanceof HorseBaseEntity)
						{
							HorseBaseEntity ah = (HorseBaseEntity) e;
							lst.add(Messenger.c(String.format("w  - Horse Speed: %.2f b/s (%.1f%%%%)",
									EntityInfoAccessor.get_speed(elb.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).getValue()),
									EntityInfoAccessor.get_horse_speed_percent(elb.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).getValue())
							)));
							lst.add(Messenger.c(String.format("w  - Horse Jump: %.2f b/s (%.1f%%%%)",
									EntityInfoAccessor.get_horse_jump(ah.getJumpStrength()),
									EntityInfoAccessor.get_horse_jump_percent(ah.getJumpStrength())
							)));
						}
					}
					if (e instanceof HostileEntity)
					{
						lst.add(Messenger.c(String.format("w  - Base attack: %.1f",elb.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue())));
						if (e instanceof ZombieVillagerEntity)
						{
							ZombieVillagerEntityAccessor ezv = (ZombieVillagerEntityAccessor) e;
							if (ezv.getConversionTimer() > 0)
							{
								lst.add(Messenger.c("w  - Convert to villager in: ","wb "+EntityInfoAccessor.makeTime(ezv.getConversionTimer())));
							}
						}
					}
				}
				if (e instanceof SlimeEntity)
				{
					lst.add(Messenger.c(String.format("w  - Base attack: %.1f",((SlimeEntityAccessor)e).invokeGetDamageAmount())));
				}
			}
		}

		return lst;
	}
}

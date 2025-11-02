/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpettisaddition.commands.info.entity;

import carpettisaddition.mixins.command.info.entity.*;
import carpettisaddition.utils.EntityUtils;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.SharedMonsterAttributes;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

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
	public static List<BaseComponent> entityInfo(Entity e, Level source_world)
	{
		List<BaseComponent> lst = Lists.newArrayList();
		Level world = EntityUtils.getEntityWorld(e);
		lst.add(Messenger.c("w " + EntityInfoUtil.entity_short_string(e)));
		if (e.isPassenger()) { lst.add(Messenger.c("w  - Rides: ", "wb "+e.getVehicle().getDisplayName().getString())); }
		if (e.isVehicle())
		{
			List<Entity> passengers = e.getPassengers();
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
		lst.add(Messenger.c(String.format("w  - Height: %.2f, Width: %.2f, Eye height: %.2f",e.getBbHeight(), e.getBbWidth(), e.getEyeHeight())));
		lst.add(Messenger.c("w  - Age: ", "wb " + EntityInfoUtil.makeTime(e.tickCount)));
		if (!DimensionWrapper.of(source_world).equals(DimensionWrapper.of(e)))
		{
			lst.add(Messenger.c("w  - Dimension: ", "wb "+DimensionWrapper.of(e).getIdentifierString()));
		}
		if (e.getRemainingFireTicks() > 0) { lst.add(Messenger.c("w  - Fire for ","wb "+e.getRemainingFireTicks(),"w  ticks")); }
		if (e.fireImmune() ) { lst.add(Messenger.c("w  - Immune to fire")); }

		int netherPortalCooldown =
				//#if MC >= 11600
				//$$ ((EntityAccessor)e).getNetherPortalCooldown();
				//#else
				e.changingDimensionDelay;
				//#endif
		if (netherPortalCooldown> 0) { lst.add(Messenger.c("w  - Portal cooldown for ","wb "+netherPortalCooldown," ticks")); }

		if (e.isInvulnerable()) { lst.add(Messenger.c("w  - Invulnerable")); }

		// TODO: find a good way to make it work in 1.20.3+, where Entity#isImmuneToExplosion requires an "Explosion" object as the arg
		//#if MC < 12003
		if (e.ignoreExplosion()) { lst.add(Messenger.c("w  - Immune to explosions")); }
		//#endif

		if (e instanceof ItemEntity)
		{
			ItemEntity ei = (ItemEntity)e;
			ItemStack stack = ei.getItem();// getEntityItem();
			String stackname = stack.getCount()>1?String.format("%dx%s",stack.getCount(), stack.getHoverName().getString()):stack.getHoverName().getString();
			lst.add(Messenger.c("w  - Content: ", "wb "+stackname));
			lst.add(Messenger.c("w  - Despawn Timer: ", "wb " + EntityInfoUtil.makeTime(((ItemEntityAccessor)ei).getAge())));
		}
		if (e instanceof ExperienceOrb)
		{
			ExperienceOrb exp = (ExperienceOrb)e;
			lst.add(Messenger.c("w  - Despawn Timer: ", "wb "+EntityInfoUtil.makeTime(((ExperienceOrbEntityAccessor)exp).getOrbAge())));
			lst.add(Messenger.c("w  - Xp Value: ", "wb "+exp.getValue()));
		}
		if (e instanceof ItemFrame)
		{
			ItemFrame eif = (ItemFrame)e;
			lst.add(Messenger.c("w  - Content: ", "wb "+eif.getItem().getHoverName()));
			lst.add(Messenger.c("w  - Rotation: ", "wb "+eif.getRotation()));
		}
		if (e instanceof Painting)
		{
			Painting ep = (Painting)e;
			String motive =
					//#if MC >= 11900
					//$$ ep.getVariant().toString();
					//#else
					ep.motive.toString();
					//#endif
			lst.add(Messenger.c("w  - Art: ", "wb "+motive));
		}

		if (e instanceof LivingEntity)  // EntityLivingBase
		{
			LivingEntity elb = (LivingEntity)e;
			lst.add(Messenger.c("w  - Despawn timer: ", "wb "+EntityInfoUtil.makeTime(elb.getNoActionTime())));

			lst.add(Messenger.c(String.format("w  - Health: %.2f/%.2f", elb.getHealth(), elb.getMaxHealth())));
			if (elb.getAttribute(SharedMonsterAttributes.ARMOR).getValue() > 0.0)
			{
				lst.add(Messenger.c(String.format("w  - Armour: %.1f",elb.getAttribute(SharedMonsterAttributes.ARMOR).getValue())));
			}
			if (elb.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getValue() > 0.0)
			{
				lst.add(Messenger.c(String.format("w  - Toughness: %.1f",elb.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getValue())));
			}
			//lst.add(Messenger.c(String.format("w  - Base speed: %.1fb/s",get_speed(elb.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()))));



			Collection<MobEffectInstance> potions = elb.getActiveEffects();
			if (!potions.isEmpty())
			{
				lst.add(Messenger.c("w  - Potion effects:"));
				for (MobEffectInstance pe : potions)
				{
					lst.add(Messenger.c(String.format("w    * %s%s %s",
							pe.getDescriptionId().substring(7),
							(pe.getAmplifier()>1)?String.format("x%d",pe.getAmplifier()):"",
							EntityInfoUtil.makeTime(pe.getDuration()))));
				}
			}
			ItemStack mainhand = elb.getMainHandItem();
			if (!(mainhand.isEmpty()))
			{
				lst.add(Messenger.c("w  - Main hand: ", "wb "+EntityInfoUtil.display_item(elb, mainhand)));
			}
			ItemStack offhand = elb.getOffhandItem();
			if (!(offhand.isEmpty()))
			{
				lst.add(Messenger.c("w  - Off hand: ", "wb "+EntityInfoUtil.display_item(elb, offhand)));
			}
			String armour = "";
			//#if MC >= 12105
			//$$ for (EquipmentSlot equipmentSlot : EquipmentSlot.values())
			//$$ {
			//$$ 	if (equipmentSlot.isArmorSlot())
			//$$ 	{
			//$$ 		ItemStack armourpiece = elb.getEquippedStack(equipmentSlot);
			//$$ 		if (!(armourpiece.isEmpty()))
			//$$ 		{
			//$$ 			armour += String.format("\n   * %s", EntityInfoUtil.display_item(elb, armourpiece));
			//$$ 		}
			//$$ 	}
			//$$ }
			//#else
			for (ItemStack armourpiece: elb.getArmorSlots())
			{
				if (!(armourpiece.isEmpty()))
				{
					armour += String.format("\n   * %s", EntityInfoUtil.display_item(elb, armourpiece));
				}
			}
			//#endif
			if (!("".equals(armour)))
			{
				lst.add(Messenger.c("w  - Armour:"+ armour));
			}
			if (e instanceof Mob)  // EntityLiving
			{
				Mob el = (Mob)elb;
				lst.add(Messenger.c(String.format("w  - Follow range: %.1f",el.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getValue())));

				lst.add(Messenger.c(String.format("w  - Movement speed factor: %.2f",el.getMoveControl().getSpeedModifier())));


				LivingEntity target_elb = el.getTarget();
				if (target_elb != null)
				{
					lst.add(Messenger.c("w  - Attack target: ", "wb "+EntityInfoUtil.entity_short_string(target_elb)));
				}
				if (el.canPickUpLoot())
				{
					lst.add(Messenger.c("w  - Can pick up loot"));
				}
				if (el.isPersistenceRequired())
				{
					lst.add((Messenger.c("w  - Won't despawn")));
				}

				if (e instanceof WitherBoss)
				{
					WitherBoss ew = (WitherBoss)e;
					Entity etarget = world.getEntity(ew.getAlternativeTarget(0));
					lst.add(Messenger.c("w  - Head 1 target: ", "wb "+EntityInfoUtil.entity_short_string(etarget) ));
					etarget = world.getEntity(ew.getAlternativeTarget(1));
					lst.add(Messenger.c("w  - Head 2 target: ", "wb "+EntityInfoUtil.entity_short_string(etarget) ));
					etarget = world.getEntity(ew.getAlternativeTarget(2));
					lst.add(Messenger.c("w  - Head 3 target: ", "wb "+EntityInfoUtil.entity_short_string(etarget) ));
				}

				if (e instanceof PathfinderMob)  // EntityCreature
				{
					PathfinderMob ec = (PathfinderMob) e;
					if (ec.hasRestriction())
					{
						BlockPos pos = ec.getRestrictCenter();
						lst.add(Messenger.c("w  - Home position: ","wb "+(int)ec.getRestrictRadius(),"w  blocks around ", Messenger.coord("wb", pos)));
					}

					if (e instanceof AgableMob)  // EntityAgeable
					{
						AgableMob eage = (AgableMob) e;
						if (eage.getAge() < 0)
						{
							lst.add(Messenger.c("w  - Time till adulthood: ", "wb "+EntityInfoUtil.makeTime(-eage.getAge())));
						}
						if (eage.getAge() > 0)
						{
							lst.add(Messenger.c("w  - Mating cooldown: ", "wb "+EntityInfoUtil.makeTime(eage.getAge())));
						}
						if (e instanceof Villager)
						{
							Villager ev = (Villager) e;

							SimpleContainer vinv = ev.getInventory();
							String inventory_content = "";
							for (int i = 0; i < vinv.getContainerSize(); ++i)
							{
								ItemStack vstack = vinv.getItem(i);
								if (!vstack.isEmpty())
								{
									inventory_content += String.format("\n   * %d: %s", i, EntityInfoUtil.display_item(ev, vstack));
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
						if (e instanceof AbstractHorse)
						{
							AbstractHorse ah = (AbstractHorse) e;
							lst.add(Messenger.c(String.format("w  - Horse Speed: %.2f b/s (%.1f%%%%)",
									EntityInfoUtil.get_speed(elb.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()),
									EntityInfoUtil.get_horse_speed_percent(elb.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue())
							)));

							double strength =
									//#if MC >= 12005
									//$$ ah.getAttributeValue(Attributes.JUMP_STRENGTH);
									//#else
									ah.getCustomJump();
									//#endif
							lst.add(Messenger.c(String.format("w  - Horse Jump: %.2f b/s (%.1f%%%%)",
									EntityInfoUtil.get_horse_jump(strength),
									EntityInfoUtil.get_horse_jump_percent(strength)
							)));
						}
					}
					if (e instanceof Monster)
					{
						lst.add(Messenger.c(String.format("w  - Base attack: %.1f",elb.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue())));
						if (e instanceof ZombieVillager)
						{
							ZombieVillagerEntityAccessor ezv = (ZombieVillagerEntityAccessor) e;
							if (ezv.getConversionTimer() > 0)
							{
								lst.add(Messenger.c("w  - Convert to villager in: ","wb "+EntityInfoUtil.makeTime(ezv.getConversionTimer())));
							}
						}
					}
				}
				if (e instanceof Slime)
				{
					lst.add(Messenger.c(String.format("w  - Base attack: %.1f",((SlimeEntityAccessor)e).invokeGetDamageAmount())));
				}
			}
		}

		return lst;
	}
}

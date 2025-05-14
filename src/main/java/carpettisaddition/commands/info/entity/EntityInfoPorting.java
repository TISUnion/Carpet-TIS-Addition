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
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.inventory.BasicInventory;
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
		World world = EntityUtils.getEntityWorld(e);
		lst.add(Messenger.c("w " + EntityInfoUtil.entity_short_string(e)));
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
		lst.add(Messenger.c("w  - Age: ", "wb " + EntityInfoUtil.makeTime(e.age)));
		if (!DimensionWrapper.of(source_world).equals(DimensionWrapper.of(e)))
		{
			lst.add(Messenger.c("w  - Dimension: ", "wb "+DimensionWrapper.of(e).getIdentifierString()));
		}
		if (e.getFireTicks() > 0) { lst.add(Messenger.c("w  - Fire for ","wb "+e.getFireTicks(),"w  ticks")); }
		if (e.isFireImmune() ) { lst.add(Messenger.c("w  - Immune to fire")); }

		int netherPortalCooldown =
				//#if MC >= 11600
				//$$ ((EntityAccessor)e).getNetherPortalCooldown();
				//#else
				e.netherPortalCooldown;
				//#endif
		if (netherPortalCooldown> 0) { lst.add(Messenger.c("w  - Portal cooldown for ","wb "+netherPortalCooldown," ticks")); }

		if (e.isInvulnerable()) { lst.add(Messenger.c("w  - Invulnerable")); }

		// TODO: find a good way to make it work in 1.20.3+, where Entity#isImmuneToExplosion requires an "Explosion" object as the arg
		//#if MC < 12003
		if (e.isImmuneToExplosion()) { lst.add(Messenger.c("w  - Immune to explosions")); }
		//#endif

		if (e instanceof ItemEntity)
		{
			ItemEntity ei = (ItemEntity)e;
			ItemStack stack = ei.getStack();// getEntityItem();
			String stackname = stack.getCount()>1?String.format("%dx%s",stack.getCount(), stack.getName().getString()):stack.getName().getString();
			lst.add(Messenger.c("w  - Content: ", "wb "+stackname));
			lst.add(Messenger.c("w  - Despawn Timer: ", "wb " + EntityInfoUtil.makeTime(((ItemEntityAccessor)ei).getAge())));
		}
		if (e instanceof ExperienceOrbEntity)
		{
			ExperienceOrbEntity exp = (ExperienceOrbEntity)e;
			lst.add(Messenger.c("w  - Despawn Timer: ", "wb "+EntityInfoUtil.makeTime(((ExperienceOrbEntityAccessor)exp).getOrbAge())));
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
			lst.add(Messenger.c("w  - Despawn timer: ", "wb "+EntityInfoUtil.makeTime(elb.getDespawnCounter())));

			lst.add(Messenger.c(String.format("w  - Health: %.2f/%.2f", elb.getHealth(), elb.getMaximumHealth())));
			if (elb.getAttributeInstance(EntityAttributes.ARMOR).getValue() > 0.0)
			{
				lst.add(Messenger.c(String.format("w  - Armour: %.1f",elb.getAttributeInstance(EntityAttributes.ARMOR).getValue())));
			}
			if (elb.getAttributeInstance(EntityAttributes.ARMOR_TOUGHNESS).getValue() > 0.0)
			{
				lst.add(Messenger.c(String.format("w  - Toughness: %.1f",elb.getAttributeInstance(EntityAttributes.ARMOR_TOUGHNESS).getValue())));
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
							EntityInfoUtil.makeTime(pe.getDuration()))));
				}
			}
			ItemStack mainhand = elb.getMainHandStack();
			if (!(mainhand.isEmpty()))
			{
				lst.add(Messenger.c("w  - Main hand: ", "wb "+EntityInfoUtil.display_item(elb, mainhand)));
			}
			ItemStack offhand = elb.getOffHandStack();
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
			for (ItemStack armourpiece: elb.getArmorItems())
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
			if (e instanceof MobEntity)  // EntityLiving
			{
				MobEntity el = (MobEntity)elb;
				lst.add(Messenger.c(String.format("w  - Follow range: %.1f",el.getAttributeInstance(EntityAttributes.FOLLOW_RANGE).getValue())));

				lst.add(Messenger.c(String.format("w  - Movement speed factor: %.2f",el.getMoveControl().getSpeed())));


				LivingEntity target_elb = el.getTarget();
				if (target_elb != null)
				{
					lst.add(Messenger.c("w  - Attack target: ", "wb "+EntityInfoUtil.entity_short_string(target_elb)));
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
					lst.add(Messenger.c("w  - Head 1 target: ", "wb "+EntityInfoUtil.entity_short_string(etarget) ));
					etarget = world.getEntityById(ew.getTrackedEntityId(1));
					lst.add(Messenger.c("w  - Head 2 target: ", "wb "+EntityInfoUtil.entity_short_string(etarget) ));
					etarget = world.getEntityById(ew.getTrackedEntityId(2));
					lst.add(Messenger.c("w  - Head 3 target: ", "wb "+EntityInfoUtil.entity_short_string(etarget) ));
				}

				if (e instanceof MobEntityWithAi)  // EntityCreature
				{
					MobEntityWithAi ec = (MobEntityWithAi) e;
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
							lst.add(Messenger.c("w  - Time till adulthood: ", "wb "+EntityInfoUtil.makeTime(-eage.getBreedingAge())));
						}
						if (eage.getBreedingAge() > 0)
						{
							lst.add(Messenger.c("w  - Mating cooldown: ", "wb "+EntityInfoUtil.makeTime(eage.getBreedingAge())));
						}
						if (e instanceof VillagerEntity)
						{
							VillagerEntity ev = (VillagerEntity) e;

							BasicInventory vinv = ev.getInventory();
							String inventory_content = "";
							for (int i = 0; i < vinv.getInvSize(); ++i)
							{
								ItemStack vstack = vinv.getInvStack(i);
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
						if (e instanceof HorseBaseEntity)
						{
							HorseBaseEntity ah = (HorseBaseEntity) e;
							lst.add(Messenger.c(String.format("w  - Horse Speed: %.2f b/s (%.1f%%%%)",
									EntityInfoUtil.get_speed(elb.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).getValue()),
									EntityInfoUtil.get_horse_speed_percent(elb.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).getValue())
							)));

							double strength =
									//#if MC >= 12005
									//$$ ah.getAttributeValue(EntityAttributes.GENERIC_JUMP_STRENGTH);
									//#else
									ah.getJumpStrength();
									//#endif
							lst.add(Messenger.c(String.format("w  - Horse Jump: %.2f b/s (%.1f%%%%)",
									EntityInfoUtil.get_horse_jump(strength),
									EntityInfoUtil.get_horse_jump_percent(strength)
							)));
						}
					}
					if (e instanceof HostileEntity)
					{
						lst.add(Messenger.c(String.format("w  - Base attack: %.1f",elb.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).getValue())));
						if (e instanceof ZombieVillagerEntity)
						{
							ZombieVillagerEntityAccessor ezv = (ZombieVillagerEntityAccessor) e;
							if (ezv.getConversionTimer() > 0)
							{
								lst.add(Messenger.c("w  - Convert to villager in: ","wb "+EntityInfoUtil.makeTime(ezv.getConversionTimer())));
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

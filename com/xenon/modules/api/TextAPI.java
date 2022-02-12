package com.xenon.modules.api;

import static net.minecraft.client.renderer.GlStateManager.alphaFunc;
import static net.minecraft.client.renderer.GlStateManager.color;
import static net.minecraft.client.renderer.GlStateManager.depthMask;
import static net.minecraft.client.renderer.GlStateManager.disableAlpha;
import static net.minecraft.client.renderer.GlStateManager.disableBlend;
import static net.minecraft.client.renderer.GlStateManager.disableDepth;
import static net.minecraft.client.renderer.GlStateManager.disableLighting;
import static net.minecraft.client.renderer.GlStateManager.disableRescaleNormal;
import static net.minecraft.client.renderer.GlStateManager.disableTexture2D;
import static net.minecraft.client.renderer.GlStateManager.enableAlpha;
import static net.minecraft.client.renderer.GlStateManager.enableBlend;
import static net.minecraft.client.renderer.GlStateManager.enableDepth;
import static net.minecraft.client.renderer.GlStateManager.enableLighting;
import static net.minecraft.client.renderer.GlStateManager.enableRescaleNormal;
import static net.minecraft.client.renderer.GlStateManager.enableTexture2D;
import static net.minecraft.client.renderer.GlStateManager.popMatrix;
import static net.minecraft.client.renderer.GlStateManager.pushMatrix;
import static net.minecraft.client.renderer.GlStateManager.rotate;
import static net.minecraft.client.renderer.GlStateManager.scale;
import static net.minecraft.client.renderer.GlStateManager.shadeModel;
import static net.minecraft.client.renderer.GlStateManager.translate;
import static net.minecraft.client.renderer.GlStateManager.tryBlendFuncSeparate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Multimap;
import com.xenon.XenonClient;
import com.xenon.util.readability.Hook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StringUtils;

/**
 * 
 * @author VoidAlchemist
 *
 */
public class TextAPI {

	private static final Pattern beggar = Pattern.compile("p+l+[zs]{2,}");
	private static final Pattern beggar2 = Pattern.compile("g+i+(v+e?m+e+)|(m+e+)");
	private static final String[] insults = new String[] {"moron", "jerk", "gay"};
	private static final Pattern insults1 = Pattern.compile("n+o{2,}b+");
	private static final Pattern insults2 = Pattern.compile("t+o+x+i+[ck]+");
	private static final Pattern loli = Pattern.compile("k+a+w+a+[iy]+");
	private static final Pattern loli1 = Pattern.compile("c+u+t+e+");
	private static final Pattern egirl = Pattern.compile("e[-_]?g+i+r+l");
	
	/**
	 * Handles the chat message before actually displaying it to the ChatGui.
	 * To cancel the event, just return <code>null</code>.
	 * @param component
	 * @return
	 */
	@Hook("net.minecraft.client.network.NetHandlerPlayClient#handleChat(S02PacketChat) -> line 857")
	public static IChatComponent onClientChatReceived(IChatComponent component) {
		String s = StringUtils.stripControlCodes(component.getFormattedText()).replaceAll("\\s", "");
		if (s.isEmpty() || (XenonClient.instance.settings.chatFilter && shouldFilter(s)))	return null;
		
		if (XenonClient.instance.settings.customnameEnabled) {
			String msg = component.getFormattedText();
			String username = Minecraft.getMinecraft().getSession().getUsername();
			if (msg.contains(username)) {
				msg = msg.replace(username, XenonClient.instance.settings.customName);
				return new ChatComponentText(msg).setChatStyle(component.getChatStyle());
			}
		}
		
		return component;
	}
	
	/**
	 * 
	 * @param s
	 * @return if s should be filtered
	 */
	private static boolean shouldFilter(String s) {
		String s1 = s.toLowerCase(Locale.ROOT);
		for (String insult : insults)
			if (s1.contains(insult))
				return true;
		return beggar.matcher(s1).find() || beggar2.matcher(s1).find() || insults1.matcher(s1).find() || insults2.matcher(s1).find()
				|| loli.matcher(s1).find() || loli1.matcher(s1).find() || egirl.matcher(s1).find();
	}
	
	/**
	 * Original is {@link net.minecraft.client.renderer.entity.RendererLivingEntity#renderName(EntityLivingBase, double, double, double)}.
	 * Only changed 1 line for the user's custom name to be rendered.
	 * @param <T>
	 * @param instance
	 * @param entity
	 * @param x
	 * @param y
	 * @param z
	 */
	@Hook("net.minecraft.client.renderer.entity.RendererLivingEntity#renderName(...) -> line 643")
	public static <T extends EntityLivingBase> void  renderLivingName(RendererLivingEntity<T> instance, T entity, double x, double y, double z) {
		if (instance.canRenderName(entity))
        {
            double d0 = entity.getDistanceSqToEntity(instance.renderManager.livingPlayer);
            float f = entity.isSneaking() ? instance.NAME_TAG_RANGE_SNEAK : instance.NAME_TAG_RANGE;

            if (d0 < (double)(f * f))
            {
                String s = entity instanceof EntityPlayer &&
                		entity.getDisplayName().getFormattedText().contains(Minecraft.getMinecraft().getSession().getUsername()) &&
                		XenonClient.instance.settings.customnameEnabled ? 
                				XenonClient.instance.settings.customName : entity.getDisplayName().getFormattedText();
                float f1 = 0.02666667F;
                alphaFunc(516, 0.1F);

                if (entity.isSneaking())
                {
                    FontRenderer fontrenderer = instance.getFontRendererFromRenderManager();
                    pushMatrix();
                    translate((float)x, (float)y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F), (float)z);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    rotate(-instance.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    rotate(instance.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    scale(-0.02666667F, -0.02666667F, 0.02666667F);
                    translate(0.0F, 9.374999F, 0.0F);
                    disableLighting();
                    depthMask(false);
                    enableBlend();
                    disableTexture2D();
                    tryBlendFuncSeparate(770, 771, 1, 0);
                    int i = fontrenderer.getStringWidth(s) / 2;
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                    worldrenderer.pos((double)(-i - 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((double)(-i - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((double)(i + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((double)(i + 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    tessellator.draw();
                    enableTexture2D();
                    depthMask(true);
                    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
                    enableLighting();
                    disableBlend();
                    color(1.0F, 1.0F, 1.0F, 1.0F);
                    popMatrix();
                }
                else
                {
                    instance.renderOffsetLivingLabel(entity, x, y - (entity.isChild() ? (double)(entity.height / 2.0F) : 0.0D), z, s, 0.02666667F, d0);
                }
            }
        }
	}
	
	
	/**
	 * Vanilla tooltip drawing method with a scrolling feature implemented.
	 * As the scrolled amount shall be reset when the player swap GUI, it was necessary adding 2 fields <code>scrollX</code>
	 * and <code>scrollY</code> to {@link net.minecraft.client.gui.GuiScreen}.
	 * @param instance
	 * @param font
	 * @param textLines
	 * @param x
	 * @param y
	 */
	@Hook("net.minecraft.client.gui.GuiScreen#drawHoveringText(...) -> line 198")
	public static void drawHoveringText(GuiScreen instance, FontRenderer font, List<String> textLines, int x, int y) {
		if (!textLines.isEmpty())
        {
            disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            disableLighting();
            disableDepth();
            int tooltipWidth = 0;

            for (String s : textLines)
            {
                int j = font.getStringWidth(s);

                if (j > tooltipWidth)
                    tooltipWidth = j;
            }

            int tooltipX = x + 12;
            int tooltipY = y - 12;
            int tooltipHeight = 8;

            if (textLines.size() > 1)
                tooltipHeight += 2 + (textLines.size() - 1) * 10;

            /*Scrolling mechanics start*/
            label1:
            if (!(instance instanceof GuiContainerCreative)) {
            	int wheel = Mouse.getDWheel();
            	
            	if (wheel == 0)
            		break label1;
            	
            	boolean positive = wheel > 0;
            	
            	if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            		if (positive)
            			instance.scrollX -= 10;
            		else
            			instance.scrollX += 10;	
            	}else {
            		if (positive && tooltipY + instance.scrollY < 0)
            			instance.scrollY += 10;
            		else if (!positive && tooltipY + tooltipHeight + instance.scrollY > instance.height)
            			instance.scrollY -= 10;
            	}
            }
            
            tooltipY += instance.scrollY;
            tooltipX += instance.scrollX;
            /*Scrolling mechanics end*/
            

            instance.zLevel = 300.0F;
            instance.itemRender.zLevel = 300.0F;
            
            // Don't try too much to understand these coordinates. 
            // Just take the vanilla method and cache every single int that is calculated more than 2 times.
            int x1 = tooltipX - 3;
            int y1 = tooltipY - 3;
            int x2 = tooltipX + tooltipWidth;
            int y2 = tooltipY + tooltipHeight;
            
            int x3 = x2 + 3;
            int y3 = y2 + 3;
            
            Tessellator t = Tessellator.getInstance();
            WorldRenderer w = t.getWorldRenderer();
            
            //int l = 0xF0100010;-267386864
            disableTexture2D();
            enableBlend();
            disableAlpha();
            tryBlendFuncSeparate(770, 771, 1, 0);
            shadeModel(7425);
            color(0.062745098f, 0f, 0.062745098f, 0.9411764706f);
            drawRect(w, instance.zLevel, x1, tooltipY - 4, x3, y1);
            t.draw();
            drawRect(w, instance.zLevel, x1, y3, x3, y2 + 4);
            t.draw();
            drawRect(w, instance.zLevel, x1, y1, x3, y3);
            t.draw();
            drawRect(w, instance.zLevel, tooltipX - 4, y1, x1, y3);
            t.draw();
            drawRect(w, instance.zLevel, x3, y1, x2 + 4, y3);
            t.draw();
            
            drawGradientRect(w, instance.zLevel, x1, y1 + 1, x1 + 1, y3 - 1, 
            		0.3137254902f, 0f, 1f, 0.3137254902f,
            		0.1568627451f, 0f, 0.4980392157f, 0.3137254902f);
            t.draw();
            drawGradientRect(w, instance.zLevel, x2 + 2, y1 + 1, x3, y3 - 1,
            		0.3137254902f, 0f, 1f, 0.3137254902f,
            		0.1568627451f, 0f, 0.4980392157f, 0.3137254902f);
            t.draw();
            
            color(0.3137254902f, 0f, 1f, 0.3137254902f);
            drawRect(w, (int)instance.zLevel, x1, y1, x3, y1 + 1);
            t.draw();
            
            color(0.1568627451f, 0f, 0.4980392157f, 0.3137254902f);
            drawRect(w, (int)instance.zLevel, x1, y2 + 2, x3, y3);
            t.draw();
		
            color(1f, 1f, 1f, 1f);
            shadeModel(7424);
            disableBlend();
            enableAlpha();
            enableTexture2D();
            
            for (int k1 = 0; k1 < textLines.size(); ++k1)
            {
                font.drawStringWithShadow(textLines.get(k1), (float)tooltipX, (float)tooltipY, -1);

                if (k1 == 0)
                    tooltipY += 2;

                tooltipY += 10;
            }

            instance.zLevel = 0.0F;
            instance.itemRender.zLevel = 0.0F;
            enableLighting();
            enableDepth();
            RenderHelper.enableStandardItemLighting();
            enableRescaleNormal();
        }
	}
	
	private static void drawRect(WorldRenderer w, double z, double x, double y, double x2, double y2) {
		w.begin(7, DefaultVertexFormats.POSITION);
        w.pos(x, y, z).endVertex();
        w.pos(x, y2, z).endVertex();
        w.pos(x2, y2, z).endVertex();
        w.pos(x2, y, z).endVertex();
	}
	
	private static void drawGradientRect(WorldRenderer w, double z, double left, double top, double right, double bottom, float red1, 
			float green1, float blue1, float alpha1, 
			float red2, float green2, float blue2, float alpha2)
    {
        w.begin(7, DefaultVertexFormats.POSITION_COLOR);
        w.pos(right, top, z).color(red1, green1, blue1, alpha1).endVertex();
        w.pos(left, top, z).color(red1, green1, blue1, alpha1).endVertex();
        w.pos(left, bottom, z).color(red2, green2, blue2, alpha2).endVertex();
        w.pos(right, bottom, z).color(red2, green2, blue2, alpha2).endVertex();
    }
	
	
	@Hook("net.minecraft.item.ItemStack#getTooltip() -> line 651")
	public static List<String> getTooltip(ItemStack stack, EntityPlayer player) {		
		List<String> list = new ArrayList<>();
        String s = stack.getDisplayName();
        Item item = stack.getItem();
        
        boolean hasCompound = stack.hasTagCompound();
        boolean unbreakable = hasCompound && stack.getTagCompound().getBoolean("Unbreakable");
        
        int gearScore = item.getItemEnchantability();
        list.add(null);	//empty slot at index 0. will get filled later (ItemName)
        list.add(null);	//empty slot at index 1. will get filled later (ItemName)
        
        if (item instanceof ItemArmor)
        	gearScore += handleArmor(list, (ItemArmor) item);
        else if (item instanceof ItemTool)
        	gearScore += handleTool(list, (ItemTool)item);
        
        gearScore += handleModifiers(list, stack.getAttributeModifiers());
        
        if (hasCompound)
        {
        	NBTTagList nbttaglist = stack.getEnchantmentTagList();
        	
        	gearScore += handleEnch(list, nbttaglist);
            

            if (stack.stackTagCompound.hasKey("display", 10))
            {
                NBTTagCompound nbttagcompound = stack.stackTagCompound.getCompoundTag("display");
                list.add("");
                if (nbttagcompound.hasKey("color", 3))
                	list.add("Color: #" + Integer.toHexString(nbttagcompound.getInteger("color")).toUpperCase());

                if (nbttagcompound.getTagId("Lore") == 9)
                {
                    NBTTagList nbttaglist1 = nbttagcompound.getTagList("Lore", 8);

                    if (nbttaglist1.tagCount() > 0)
                    	for (int j1 = 0; j1 < nbttaglist1.tagCount(); ++j1)
                            list.add(nbttaglist1.getStringTagAt(j1));
                        
                }
            }
        }
        
        
        
        
        item.addInformation(stack, player, list, true);
        
        
        list.add("");
        
        /*Durability*/
        if (stack.isItemStackDamageable() && !unbreakable) {
        	int dura = stack.getMaxDamage() - stack.getItemDamage();
        	int maxDura = stack.getMaxDamage();
        	gearScore += dura;
        	String prefix;
        	
        	if (dura > (int)((float)maxDura * 0.75f))
        		prefix = "\u00A7aPerfect ";
        	else if (dura > (maxDura >> 1))
        		prefix = "\u00A7eUsed ";
        	else if (dura > (int)((float)maxDura * 0.25f))
        		prefix = "\u00A7cDamaged ";
        	else
        		prefix = "\u00A74Worn out ";
        	
        	list.add(prefix + dura + " / " + maxDura);
        }
        
        
    	
    	/*Rarity*/
        String rarity;
        String nameColor;
        if (unbreakable) {
        	rarity = "\u00A7c\u00A7lSPECIAL";
        	nameColor = "\u00A7c";
        }
        else if (gearScore > 2000) {
        	rarity = "\u00A7d\u00A7lMYTHIC";
        	nameColor = "\u00A7d";
        }else if (gearScore > 1000) {
        	rarity = "\u00A76\u00A7lLEGENDARY";
        	nameColor = "\u00A76";
        }else if (gearScore > 500) {
        	rarity = "\u00A75\u00A7lEPIC";
        	nameColor = "\u00A75";
        }else if (gearScore > 200) {
        	rarity = "\u00A7a\u00A7lUNCOMMON";
        	nameColor = "\u00A7A";
        }else {
        	rarity = "\u00A7f\u00A7lCOMMON";
        	nameColor = "\u00A7f";
        }
        	
        	
        String name = nameColor + stack.getDisplayName() + "\u00A7r";
        list.set(0, name);
        list.set(1, gearScore > 0 ? "Gear Score: \u00A7d"+gearScore : "");
        
        list.add(rarity);
        list.add(EnumChatFormatting.DARK_GRAY + Item.itemRegistry.getNameForObject(item).toString());
        return list;
	}
	
	
	private static int handleArmor(List<String> lines, ItemArmor item) {		
		int protec = item.getArmorMaterial().getDamageReductionAmount(item.armorType) * 4;
		lines.add("\u00A77Defense: \u00A7a+"+protec+"%");
		
		return protec * 30;
	}
	
	private static int handleTool(List<String> lines, ItemTool item) {
		int eff = (int) item.efficiencyOnProperMaterial;
		lines.add("\u00A77Dig Efficiency: \u00A7a+"+eff);
		return eff * 50;
	}
	
	private static int handleEnch(List<String> lines, NBTTagList enchantList) {
		if (enchantList == null)
			return 0;
		
		int gearScore = 0;
		for (int j = 0; j < enchantList.tagCount(); ++j)
        {
            int k = enchantList.getCompoundTagAt(j).getShort("id");
            int level = enchantList.getCompoundTagAt(j).getShort("lvl");
            
            Enchantment ench = Enchantment.getEnchantmentById(k);
            if (ench == null) continue;
            String s = ench.getTranslatedName(level);
            if (ench == Enchantment.sharpness) {
            	s += " (+"+((float)level * 1.25f)+"\u2694)";
            	gearScore += level * 50;
            }else if (ench == Enchantment.smite) {
            	s += " (+"+((float)level * 2.5f)+"\u2694 undead)";
            	gearScore += level * 40;
            }else if (ench == Enchantment.baneOfArthropods) {
            	s += " (+"+((float)level * 2.5f)+"\u2694 arthropods)";
            	gearScore += level * 30;
            }else if (ench == Enchantment.fireAspect){
            	s += " (+"+(level * 4)+"s on fire)";
            	gearScore += level * 70;
            }else if (ench == Enchantment.knockback) {
            	s += " (+"+((float)level * 0.3f)+" horizontal velocity)";
            	gearScore += level * 60;
            }else if (ench == Enchantment.power) {
            	s += " (+"+((float)level * 0.5f)+"\u27B6)";
            	gearScore += level * 150;
            }else if (ench == Enchantment.flame) {
            	s += " (+100s arrow on fire)";
            	gearScore += 200;
            }else if (ench == Enchantment.punch) {
            	gearScore += level * 100;
            }else if (ench == Enchantment.infinity) {
            	s += " (no arrow consumption)";
            	gearScore += 1000;
            }else if (ench == Enchantment.unbreaking) {
            	s += " (+"+(int)(((float)level/(float)(level + 1)) * 100f)+"% chance to not damage the item)";
            	gearScore += level * 15;
            }else if (ench == Enchantment.lure) {
            	s += " (-"+(level*5)+"s max catch time)";
            	gearScore += level * 15;
            }else if (ench == Enchantment.respiration) {
            	s += " (+"+(int)(((float)level/(float)(level+1))*100f)+"% chance not to consume oxygen)";
            	gearScore += level * 50;
            }else if (ench == Enchantment.aquaAffinity) {
            	s += " (no water digging slowdown)";
            	gearScore += 150;
            }else if (ench == Enchantment.depthStrider) {
            	switch(level) {
        		case 0:
        			break;
        		case 1:
        			s += " (-33% water slowness)";
        			gearScore += 30;
        			break;
        		case 2:
        			s += " (-67% water slowness)";
        			gearScore += 70;
        			break;
        		default:
        			s += " (no water slowness)";
        			gearScore += 150;
        		}
            }else if (ench == Enchantment.thorns) {
            	s += " (+"+(int)((float)level * 15f)+"% chance to deal thorn damage)";
            	gearScore += level * 100;
            }else if (ench == Enchantment.looting) {
            	gearScore += level * 60;
            }else if (ench == Enchantment.efficiency) {
            	s += " (+"+(level*level + 1)+"\u2E15)";
            	gearScore += level * 100;
            }else if (ench instanceof EnchantmentProtection) {
            	gearScore += level * 150;
            }
            lines.add(s);
        }
		
		return gearScore;
	}
	
	private static final HashMap<String, String> attributesmapping = new HashMap<String, String>(10);
	
	static {
		attributesmapping.put("generic.attackDamage", "Damage: \u00A7c");
		attributesmapping.put("generic.movementSpeed", "Speed: \u00A7a");
		attributesmapping.put("generic.knockbackResistance", "Knockback Resistance: \u00A7c");
		attributesmapping.put("generic.followRange", "Follow Range: \u00A7a");
		attributesmapping.put("generic.maxHealth", "Health: \u00A7a");
		attributesmapping.put("zombie.spawnReinforcements", "Spawn Reinforcements: \u00A7a");
		attributesmapping.put("horse.jumpStrength", "Horse Jump Strength: \u00A7a");
	}
	
	/**
	 * @see net.minecraft.item.ItemStack#getTooltip(EntityPlayer, boolean)
	 * @param lines
	 * @param modifs
	 * @return
	 */
	private static int handleModifiers(List<String> lines, Multimap<String, AttributeModifier> modifs) {
		int score = 0;
		
		if (modifs == null || modifs.isEmpty())	return score;
		
		StringBuilder builder = new StringBuilder();
		String gray = EnumChatFormatting.GRAY.toString();
		for (Entry<String, String> entry : attributesmapping.entrySet()) {
			Collection<AttributeModifier> collec = modifs.get(entry.getKey());
			if (collec != null && !collec.isEmpty()) {
				AttributeModifier mod = (AttributeModifier)collec.toArray()[0];
				builder.setLength(0);
				builder.append(gray);
				builder.append(entry.getValue());
				builder.append("+");
				double amount = mod.getAmount();
				score += ((int)amount - 2)*40;
				builder.append(ItemStack.DECIMALFORMAT.format(amount));
				int op = mod.getOperation();
				if (op == 1)
					builder.append("%");
				else if (op == 2)
					builder.append("% base");
				
				lines.add(builder.toString());
			}
		}
		lines.add("");
		return score;
	}
	
}

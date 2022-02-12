package com.xenon.modules.api;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.xenon.XenonClient;
import com.xenon.util.RenderUtils;
import com.xenon.util.readability.Hook;
import com.xenon.util.readability.Static;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

/**
 * 
 * @author VoidAlchemist
 * @see net.minecraft.client.gui.GuiIngame
 */
@Static
public class GuiIngameAPI{

	/**
	 * 
	 * @param instance
	 * @param mc
	 * @param sr
	 */
	@Hook("net.minecraft.client.gui.GuiIngame#renderGameOverlay(float partialTicks) -> line 149")
	public static void handlePumpkinOverlay(GuiIngame instance, Minecraft mc, ScaledResolution sr) {
		ItemStack stack = mc.thePlayer.inventory.armorItemInSlot(3);
		
		if (mc.gameSettings.thirdPersonView == 0 && stack != null && stack.getItem() == Item.getItemFromBlock(Blocks.pumpkin) && 
				XenonClient.instance.settings.pumpkinOverlay)
			
			instance.renderPumpkinOverlay(sr);
	}
	
	/**
	 * 
	 * @param instance
	 * @param mc
	 */
	@Hook("net.minecraft.client.gui.GuiIngame#renderGameOverlay(float partialTicks) -> line ")
	public static void handleBossBar(GuiIngame instance, Minecraft mc) {
		if (XenonClient.instance.settings.bossBar) {
			mc.mcProfiler.startSection("bossHealth");
	        instance.renderBossHealth();
	        mc.mcProfiler.endSection();
		}
	}
	
	/**
	 * 
	 * @param instance
	 * @param sr
	 * @param startX
	 */
	@Hook("net.minecraft.client.gui.GuiIngame#renderGameOverlay(float partialTicks) -> line 219")
	public static void handleHorseJumpBar(GuiIngame instance, Minecraft mc, ScaledResolution sr, int startX) {
		if (XenonClient.instance.settings.horsejumpbarScale <= 0.2f)
			return;
		else if (XenonClient.instance.settings.horsejumpbarScale == 1f)
			instance.renderHorseJumpBar(sr, startX);
		else
			renderHorseJumpBar(instance, sr, mc, XenonClient.instance.settings.horsejumpbarScale);
	}
	
	/**
	 * @see #handleHorseJumpBar(GuiIngame, Minecraft, ScaledResolution, int)
	 * @param instance
	 * @param sr
	 * @param mc
	 * @param scale
	 */
	public static void renderHorseJumpBar(GuiIngame instance, ScaledResolution sr, Minecraft mc, float scale)
    {
		GlStateManager.pushMatrix();
		GlStateManager.scale(scale, scale, scale);
		float scaleInverse = 1f/scale;
		
        mc.mcProfiler.startSection("jumpBar");
        mc.getTextureManager().bindTexture(Gui.icons);
        float f = mc.thePlayer.getHorseJumpPower();
        int j = (int)(f * 183f);
        int k = sr.getScaledHeight() - 32 + 3;
        int x = (sr.getScaledWidth() >> 1) - (int)(91f * scale);
        instance.drawTexturedModalRect(rescale(x, scaleInverse), rescale(k, scaleInverse), 0, 84, 182, 5);

        if (j > 0)
        {
            instance.drawTexturedModalRect(rescale(x, scaleInverse), rescale(k, scaleInverse), 0, 89, j, 5);
        }

        mc.mcProfiler.endSection();
        GlStateManager.popMatrix();
    }
	
	/**
	 * 
	 * @param instance
	 * @param mc
	 * @param midScreenX
	 * @param midScreenY
	 * @param partialTicks
	 */
	@Hook("net.minecraft.client.gui.GuiIngame#renderGameOverlay(float partialTicks) -> line 245")
	public static void handleRecordPlaying(GuiIngame instance, Minecraft mc, int screenWidth, int screenHeight, float partialTicks) {
		if (instance.recordPlayingUpFor > 0 && XenonClient.instance.settings.discDisplayed)
        {
            mc.mcProfiler.startSection("overlayMessage");
            float f2 = (float)instance.recordPlayingUpFor - partialTicks;
            int l1 = (int)(f2 * 255.0F / 20.0F);

            if (l1 > 255)
            {
                l1 = 255;
            }

            if (l1 > 8)
            {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(screenWidth / 2), (float)(screenHeight - 68), 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                int l = 16777215;

                if (instance.recordIsPlaying)
                {
                    l = MathHelper.hsvToRGB(f2 / 50.0F, 0.7F, 0.6F) & 16777215;
                }

                instance.getFontRenderer().drawString(instance.recordPlaying, -instance.getFontRenderer().getStringWidth(instance.recordPlaying) / 2, -4, l + (l1 << 24 & -16777216));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }

            mc.mcProfiler.endSection();
        }
	}
	
	/**
	 * 
	 * @param instance
	 * @param mc
	 * @param screenWidth
	 * @param screenHeight
	 * @param partialTicks
	 */
	@Hook("net.minecraft.client.gui.GuiIngame#renderGameOverlay(float partialTicks) -> line 247")
	public static void handleTitle(GuiIngame instance, Minecraft mc, int screenWidth, int screenHeight, float partialTicks) {
		if (instance.titlesTimer > 0 && XenonClient.instance.settings.titleScale > 0)
        {
			float newScale = XenonClient.instance.settings.titleScale;
			
            mc.mcProfiler.startSection("titleAndSubtitle");
            float f3 = (float)instance.titlesTimer - partialTicks;
            int alpha;

            if (XenonClient.instance.settings.titlealpha < 247)
            	alpha = XenonClient.instance.settings.titlealpha;
            
            else {
            	alpha = 255;
            	if (instance.titlesTimer > instance.titleFadeOut + instance.titleDisplayTime)
                {
                    float f4 = (float)(instance.titleFadeIn + instance.titleDisplayTime + instance.titleFadeOut) - f3;
                    alpha = (int)(f4 * 255.0F / (float)instance.titleFadeIn);
                }

                if (instance.titlesTimer <= instance.titleFadeOut)
                {
                    alpha = (int)(f3 * 255.0F / (float)instance.titleFadeOut);
                }

                alpha = MathHelper.clamp_int(alpha, 0, 255);
            }
            
            

            if (alpha > 8)
            {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(screenWidth / 2), (float)(screenHeight / 2), 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.pushMatrix();
                float realScale = newScale * 4f;
                GlStateManager.scale(realScale, realScale, realScale);
                
                int finalColor = alpha << 24 | 0x00FFFFFF;
                
                instance.getFontRenderer().drawString(instance.displayedTitle, (float)(-instance.getFontRenderer().getStringWidth(instance.displayedTitle) / 2), -10.0F, finalColor, true);
                
                GlStateManager.scale(0.5f, 0.5f, 0.5f);
                
                instance.getFontRenderer().drawString(instance.displayedSubTitle, (float)(-instance.getFontRenderer().getStringWidth(instance.displayedSubTitle) / 2), 5.0F, finalColor, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }

            mc.mcProfiler.endSection();
        }
	}
	
	/**
	 * Handles drawing scoreboard and the chat.
	 * @param instance
	 * @param mc
	 * @param sr
	 * @param screenWidth
	 * @param screenHeight
	 */
	@Hook("net.minecraft.client.gui.GuiIngame#renderGameOverlay(float partialTicks) -> line 249")
	public static void handleScoreboard(GuiIngame instance, Minecraft mc, ScaledResolution sr, int screenWidth, int screenHeight) {
		Scoreboard scoreboard = mc.theWorld.getScoreboard();
		ScoreObjective scoreobjective1;
        
        if (XenonClient.instance.settings.scoreboardScale > 0) {
        	ScoreObjective scoreobjective = null;
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(mc.thePlayer.getName());

            if (scoreplayerteam != null)
            {
                int i1 = scoreplayerteam.getChatFormat().getColorIndex();

                if (i1 >= 0)
                    scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
            }

            scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);

            if (scoreobjective1 != null)
            {
            	if (XenonClient.instance.settings.scoreboardScale != 1)
            		renderScoreboardScaled(instance.getFontRenderer(), instance, scoreobjective1, sr, 1f/XenonClient.instance.settings.scoreboardScale);
            	else
            		instance.renderScoreboard(scoreobjective1, sr);
            }
                
        }
        

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, (float)(screenHeight - 48), 0.0F);
        mc.mcProfiler.startSection("chat");
        instance.persistantChatGUI.drawChat(instance.updateCounter);
        mc.mcProfiler.endSection();
        GlStateManager.popMatrix();
        scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(0);

        if (mc.gameSettings.keyBindPlayerList.isKeyDown() && (!mc.isIntegratedServerRunning() || mc.thePlayer.sendQueue.getPlayerInfoMap().size() > 1 || scoreobjective1 != null))
        {
            instance.overlayPlayerList.updatePlayerList(true);
            instance.overlayPlayerList.renderPlayerlist(screenWidth, scoreboard, scoreobjective1);
        }
        else
        {
            instance.overlayPlayerList.updatePlayerList(false);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
	}
	
	/**
	 * Copy paste of {@link net.minecraft.client.gui.GuiIngame#renderScoreboard(ScoreObjective, ScaledResolution)}
	 * @see #handleScoreboard(GuiIngame, Minecraft, ScaledResolution, int, int)
	 * @param fr
	 * @param g
	 * @param objective
	 * @param scaledRes
	 * @param scaleInverse
	 */
	private static void renderScoreboardScaled(FontRenderer fr, Gui g, ScoreObjective objective, ScaledResolution scaledRes, float scaleInverse) {
		float scaleFactor = XenonClient.instance.settings.scoreboardScale;
		
		int newFONT_HEIGHT = (int)((float)fr.FONT_HEIGHT * scaleFactor);
		
		GlStateManager.pushMatrix();
        GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);
        
		Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> collection = scoreboard.getSortedScores(objective);
        List<Score> list = Lists.newArrayList(Iterables.filter(collection, new Predicate<Score>()
        {
            public boolean apply(Score p_apply_1_)
            {
                return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
            }

        }));

        if (list.size() > 15)
        {
            collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
        }
        else
        {
            collection = list;
        }

        int i = (int)((float)fr.getStringWidth(objective.getDisplayName()) * scaleFactor);

        for (Score score : collection)
        {
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
            i = Math.max(i, (int)((float)fr.getStringWidth(s) * scaleFactor));
        }

        int i1 = collection.size() * newFONT_HEIGHT;
        int j1 = scaledRes.getScaledHeight() / 2 + i1 / 3;
        int k1 = 3;
        int l1 = scaledRes.getScaledWidth() - i - k1;
        int j = 0;

        
        for (Score score1 : collection)
        {
            ++j;
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
            String s2 = EnumChatFormatting.RED + "" + score1.getScorePoints();
            int k = j1 - j * newFONT_HEIGHT;
            int krescale = rescale(j1, scaleInverse) - j * fr.FONT_HEIGHT;
            
            int l = scaledRes.getScaledWidth() - k1 + 2;
            
            
            g.drawRect(rescale(l1 - 2, scaleInverse), krescale, rescale(l, scaleInverse), krescale + fr.FONT_HEIGHT, 1342177280);
            
            
            fr.drawString(s1, rescale(l1, scaleInverse), krescale, 553648127);
            fr.drawString(s2, rescale(l, scaleInverse) - fr.getStringWidth(s2), krescale, 553648127);

            if (j == collection.size())
            {
                String s3 = objective.getDisplayName();
                g.drawRect(rescale(l1 - 2, scaleInverse), krescale - fr.FONT_HEIGHT - rescale(1, scaleInverse), rescale(l, scaleInverse), (int) (krescale - scaleInverse), 1610612736);
                g.drawRect(rescale(l1 - 2, scaleInverse), (int) (krescale - scaleInverse), rescale(l, scaleInverse), krescale, 1342177280);
                
                fr.drawString(s3, rescale(l1 + i / 2, scaleInverse) - fr.getStringWidth(s3) / 2, krescale - fr.FONT_HEIGHT, 553648127);
            }
        }
        GlStateManager.popMatrix();
	}
	
	/**
	 * @see #renderScoreboardScaled(FontRenderer, Gui, ScoreObjective, ScaledResolution, float)
	 * @param init
	 * @param factor
	 * @return the init value rescaled correctly
	 */
	public static int rescale(int init, float factor){
		return (int)((float)init * factor);
	}
	
	/**
	 * Draws all the non-vanilla stuff to screen.
	 * @param instance : Gui instance used to draw textures, rectangles, etc.
	 * @param mc
	 * @param sr
	 */
	@Hook("net.minecraft.client.gui.GuiIngame#renderGameOverlay(float partialTicks) -> line 251")
	public static void handleAddons(Gui instance, Minecraft mc, ScaledResolution sr) {
		drawFPS(mc, sr);
		BlockPos posPlayer = new BlockPos(MathHelper.floor_double(mc.getRenderViewEntity().posX), MathHelper.floor_double(mc.getRenderViewEntity().getEntityBoundingBox().minY), MathHelper.floor_double(mc.getRenderViewEntity().posZ));
		drawPlayerPos(mc, sr, posPlayer);
		drawBiome(mc, sr, posPlayer);
		RenderUtils.resetTextureState();
		drawPotions(mc, instance, sr);
		drawArmorStatus(mc, instance, sr);
		drawKeystrokes(mc, instance, sr);
		
		RenderUtils.resetTextureState();
	}
	
	/**
	 * Draws FPS information overlay.
	 * @see #handleAddons()
	 * @param mc
	 * @param sr
	 */
	private static void drawFPS(Minecraft mc, ScaledResolution sr) {
		if (XenonClient.instance.settings.fpsScale <= 0.02f)	return;
		float scaleInverse = 1f/XenonClient.instance.settings.fpsScale;
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(XenonClient.instance.settings.fpsScale, XenonClient.instance.settings.fpsScale, XenonClient.instance.settings.fpsScale);
		RenderUtils.font.drawString(mc.getDebugFPS()+" fps", rescale(XenonClient.instance.settings.fpsxy.getAbsX(sr), scaleInverse), rescale(XenonClient.instance.settings.fpsxy.getAbsY(sr), scaleInverse), 0xFFFFFFFF);
		GlStateManager.popMatrix();
	}
	
	/**
	 * Draws player position information overlay.
	 * @see #handleAddons()
	 * @see net.minecraft.client.gui.GuiOverlayDebug#call()
	 * @param mc
	 * @param sr
	 * @param actualPos
	 */
	private static void drawPlayerPos(Minecraft mc, ScaledResolution sr, BlockPos pos) {
		if (XenonClient.instance.settings.posScale <= 0.02f)	return;
		float scaleInverse = 1f/XenonClient.instance.settings.posScale;
		GlStateManager.pushMatrix();
		GlStateManager.scale(XenonClient.instance.settings.posScale, XenonClient.instance.settings.posScale, XenonClient.instance.settings.posScale);
		int x = rescale(XenonClient.instance.settings.posxy.getAbsX(sr), scaleInverse);
		int y = rescale(XenonClient.instance.settings.posxy.getAbsY(sr), scaleInverse);
		RenderUtils.font.drawString("x: "+pos.getX(), x, y, 0xFFFFFFFF);
		RenderUtils.font.drawString("y: "+pos.getY(), x, y + 9 * XenonClient.instance.settings.posScale, 0xFFFFFFFF);
		RenderUtils.font.drawString("z: "+pos.getZ(), x, y + 18 * XenonClient.instance.settings.posScale, 0xFFFFFFFF);
		GlStateManager.popMatrix();
	}
	
	
	/**
	 * Draws biome information overlay.
	 * @see #handleAddons(Minecraft, ScaledResolution)
	 * @see net.minecraft.client.gui.GuiOverlayDebug#call()
	 * @param mc
	 * @param sr
	 * @param pos
	 */
	private static void drawBiome(Minecraft mc, ScaledResolution sr, BlockPos pos) {
		if (XenonClient.instance.settings.biomeScale <= 0.02f)	return;
		
		float scaleInverse = 1f/XenonClient.instance.settings.biomeScale;
		GlStateManager.pushMatrix();
		GlStateManager.scale(XenonClient.instance.settings.biomeScale, XenonClient.instance.settings.biomeScale, XenonClient.instance.settings.biomeScale);
		RenderUtils.font.drawString(mc.theWorld.getChunkFromBlockCoords(pos).getBiome(pos, mc.theWorld.getWorldChunkManager()).biomeName,
				rescale(XenonClient.instance.settings.biomexy.getAbsX(sr), scaleInverse),
				rescale(XenonClient.instance.settings.biomexy.getAbsY(sr), scaleInverse), 0xFFFFFFFF);
		GlStateManager.popMatrix();
	}
	
	/**
	 * Draws all active potion effect information overlay.
	 * @see #handleAddons(Gui, Minecraft, ScaledResolution)
	 * @see net.minecraft.client.renderer.InventoryEffectRenderer#drawActivePotionEffects()
	 * @param mc
	 * @param gui
	 * @param sr
	 */
	private static void drawPotions(Minecraft mc, Gui gui, ScaledResolution sr) {
		if (XenonClient.instance.settings.potionoverlayScale <= 0.02f)	return;
		
		final float scale = XenonClient.instance.settings.potionoverlayScale;
		final float scaleInverse = 1f/scale;
		GlStateManager.pushMatrix();
		GlStateManager.scale(scale, scale, scale);
		
		int baseX = XenonClient.instance.settings.potionoverlayxy.getAbsX(sr);
		int baseY = XenonClient.instance.settings.potionoverlayxy.getAbsY(sr);
		baseX = rescale(baseX, scaleInverse);
		baseY = rescale(baseY, scaleInverse);
		
		int delta5 = (int)(5f * scale);
		int delta20 = (int)(20f * scale);
		
		for (PotionEffect potef : mc.thePlayer.getActivePotionEffects())
		{
			Potion pot = Potion.potionTypes[potef.getPotionID()];

            mc.getTextureManager().bindTexture(GuiContainer.inventoryBackground);
            
            if (pot.hasStatusIcon())
            {
                int i = pot.getStatusIconIndex();
                gui.drawTexturedModalRect(baseX, baseY, 0 + i % 8 * 18, 198 + i / 8 * 18, 18, 18);
            }
            baseY += delta5;
            int x = baseX + delta20;
            String name = I18n.format(pot.getName(), new Object[0]);
            
            int amplifierindex = potef.getAmplifier();
            // Potion effect real level is  potef.getAmplifier() + 1.
            if (amplifierindex >= 0 && amplifierindex < 20)
            name += " "+RenderUtils.roman[amplifierindex];
            
            mc.fontRendererObj.drawString(name, rescale(x, scaleInverse), baseY, 0xFFFFFFFF, false);
            mc.fontRendererObj.drawString(pot.getDurationString(potef), rescale(x + delta5 + mc.fontRendererObj.getStringWidth(name), scaleInverse), baseY, 0xFFFFFFFF, false);
            baseY += (int) (15f * scale);
		}
		
		GlStateManager.popMatrix();
	}
	
	/**
	 * Draws armor & held item on screen.
	 * @see #handleAddons(Gui, Minecraft, ScaledResolution)
	 * @param mc
	 * @param gui
	 * @param sr
	 */
	private static void drawArmorStatus(Minecraft mc, Gui gui, ScaledResolution sr) {
		if (XenonClient.instance.settings.armorstatusScale <= 0.02f)	return;
		
		final float scale = XenonClient.instance.settings.armorstatusScale;
		final float scaleInverse = 1f/scale;
		GlStateManager.pushMatrix();
		GlStateManager.scale(scale, scale, scale);
		
		int x = XenonClient.instance.settings.armorstatusxy.getAbsX(sr);
		int y = XenonClient.instance.settings.armorstatusxy.getAbsY(sr);
		int rescaledX = rescale(x, scaleInverse);
		
		ItemStack holding = mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem);
		if (holding != null)	renderItemInGui(holding, mc, rescaledX, rescale(y, scaleInverse));
		y += (int)(16f * scale);
		
		if (mc.thePlayer.inventory.armorInventory.length > 0)
			for (int i=3; i >= 0; i--)
			{
				ItemStack is = mc.thePlayer.inventory.armorItemInSlot(i);
				if ( is != null )
					renderItemInGui(is, mc, rescaledX, rescale(y + (int)(16f*(float)(3-i)*scale), scaleInverse));
			}
		
		GlStateManager.popMatrix();	
	}
	
	/**
	 * @see #drawArmorStatus(Minecraft, Gui, ScaledResolution)
	 * @param s
	 * @param m
	 * @param x
	 * @param y
	 */
	private static void renderItemInGui(ItemStack s, Minecraft m, int x, int y)
	{
		
		if (XenonClient.instance.settings.armorstatusDurability && s.getItem().isDamageable() )
		{
			double prc = (1 - ((double)s.getItemDamage() / (double)s.getMaxDamage()))*100;
			String se = Math.round(prc)+" %";
			m.fontRendererObj.drawString(se, x - m.fontRendererObj.getStringWidth(se) - 2, y + 5, 0xFFFFFFFF);
		}
		RenderHelper.enableGUIStandardItemLighting();	// Allows the correct lighting (Cf Unpleasant lands' API where the items in GUI weren't rendering well.)
		m.getRenderItem().renderItemAndEffectIntoGUI(s, x, y);
	}
	
	/**
	 * @see #handleAddons(Gui, Minecraft, ScaledResolution)
	 * @param mc
	 * @param gui
	 * @param sr
	 */
	private static void drawKeystrokes(Minecraft mc, Gui gui, ScaledResolution sr) {
		if (XenonClient.instance.settings.keystrokesScale <= 0.01f)	return;
		
		float scaleInverse = 1f/XenonClient.instance.settings.keystrokesScale;
		GlStateManager.pushMatrix();
		GlStateManager.scale(XenonClient.instance.settings.keystrokesScale, XenonClient.instance.settings.keystrokesScale, XenonClient.instance.settings.keystrokesScale);
		
		int x = XenonClient.instance.settings.keystrokesxy.getAbsX(sr);
		int y = XenonClient.instance.settings.keystrokesxy.getAbsY(sr);
		int rescaledX = rescale(x, scaleInverse), rescaledY = rescale(y, scaleInverse);
		
		if (Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown())	Gui.drawRect(rescaledX + 21, rescaledY, rescaledX + 41, rescaledY + 20, 0xaaF0F0F0);
		else	Gui.drawRect(rescaledX + 21, rescaledY, rescaledX + 41, rescaledY + 20, 0xaa505050);
		
		gui.drawCenteredString(mc.fontRendererObj, "W", rescaledX + 31, rescaledY + 7, 0xFFFFFF);
		
		rescaledY += 21;
		
		if (Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown())	Gui.drawRect(rescaledX, rescaledY, rescaledX + 20, rescaledY + 20, 0xaaF0F0F0);
		else	Gui.drawRect(rescaledX, rescaledY, rescaledX + 20, rescaledY + 20, 0xaa505050);
		
		gui.drawCenteredString(mc.fontRendererObj, "A", rescaledX + 10, rescaledY + 7, 0xFFFFFF);
		
		rescaledX += 21;
		
		if (Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown())	Gui.drawRect(rescaledX, rescaledY, rescaledX + 20, rescaledY + 20, 0xaaF0F0F0);
		else	Gui.drawRect(rescaledX, rescaledY, rescaledX + 20, rescaledY + 20, 0xaa505050);
		
		gui.drawCenteredString(mc.fontRendererObj, "S", rescaledX + 10, rescaledY + 7, 0xFFFFFF);
		
		rescaledX += 21;
		
		if (Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown())	Gui.drawRect(rescaledX, rescaledY, rescaledX + 20, rescaledY + 20, 0xaaF0F0F0);
		else	Gui.drawRect(rescaledX, rescaledY, rescaledX + 20, rescaledY + 20, 0xaa505050);
		
		gui.drawCenteredString(mc.fontRendererObj, "D", rescaledX + 10, rescaledY + 7, 0xFFFFFF);
		
		rescaledY += 21;
		rescaledX -= 10;
		
		if (Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown())	Gui.drawRect(rescaledX, rescaledY, rescaledX + 30, rescaledY + 20, 0xaaF0F0F0);
		else	Gui.drawRect(rescaledX, rescaledY, rescaledX + 30, rescaledY + 20, 0xaa505050);
		
		gui.drawCenteredString(mc.fontRendererObj, "RC", rescaledX + 15, rescaledY + 5, 0xFFFFFF);
		
		rescaledX -= 32;
		
		if (Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown())	Gui.drawRect(rescaledX, rescaledY, rescaledX + 30, rescaledY + 20, 0xaaF0F0F0);
		else	Gui.drawRect(rescaledX, rescaledY, rescaledX + 30, rescaledY + 20, 0xaa505050);
		
		gui.drawCenteredString(mc.fontRendererObj, "LC", rescaledX + 15, rescaledY + 5, 0xFFFFFF);
		
		
		if (XenonClient.instance.settings.keystrokesCPS) {
			long time = System.currentTimeMillis();
			XenonClient.instance.cpsCounter.call(time);
			
			int Rcps = XenonClient.instance.cpsCounter.getRCPS(time);
			int Lcps = XenonClient.instance.cpsCounter.getLCPS(time);
			
			if (Rcps > 0)
				RenderUtils.font.drawCenteredTextScaled(Integer.toString(Rcps), rescaledX + 47, rescaledY + 13, 0xFFFFFFFF, 0.6d);
			if (Lcps > 0)
				RenderUtils.font.drawCenteredTextScaled(Integer.toString(Lcps), rescaledX + 15, rescaledY + 13, 0xFFFFFFFF, 0.6d);
			
			RenderUtils.resetTextureState();
		}
		
		GlStateManager.popMatrix();
	}
	
	@Hook("net.minecraft.client.gui.GuiIngame#renderPlayerStats(ScaledResolution) -> line 701")
	public static void handleSaturation(GuiIngame instance, EntityPlayer player, int k1, int j1, int l, float saturation) {
		if (XenonClient.instance.settings.saturation && saturation != 0.0f)
			for (int l6 = 0; (float)l6 < saturation / 2.0F; ++l6)
            {
                int k7 = k1 - 10;
                int j8 = 16;
                int j9 = 0;

                if (player.isPotionActive(Potion.hunger))
                {
                    j8 += 36;
                    j9 = 13;
                }

                if (saturation <= 0.0F && (float)instance.updateCounter % (saturation * 3.0F + 1.0F) == 0.0F)
                    k7 = k1 + (instance.rand.nextInt(3) - 1);

                int j10 = j1 - l6 * 8 - 9;
                instance.drawTexturedModalRect(j10, k7, 16 + j9 * 9, 27, 9, 9);

                if ((float)(l6 * 2 + 1) < saturation)
                	instance.drawTexturedModalRect(j10, k7, j8 + 36, 27, 9, 9);

                if ((float)(l6 * 2 + 1) == saturation)
                	instance.drawTexturedModalRect(j10, k7, j8 + 45, 27, 9, 9);
            }
	}
}

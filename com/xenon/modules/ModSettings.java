package com.xenon.modules;

import com.xenon.util.readability.Hook;

public class ModSettings {	
	public boolean discordRP = true;
	@Hook("net.minecraft.client.renderer.entity.RendererLivingEntity#canRenderName(T) -> line 650")
	public boolean showOwnName = true;
	public boolean speedFOV = true;
	@Hook("net.minecraft.client.gui.GuiScreen#drawWorldBackground() -> line 675")
	public boolean darkBackground = true;
	public boolean toggleSneak = true;
	public boolean toggleSprint = true;
	public boolean bossBar = true;
	@Hook("net.minecraft.client.entity.EntityPlayerSP#onLivingUpdate() -> line 763")
	public boolean nausea = true;
	public boolean pumpkinOverlay = true;
	public boolean discDisplayed = true;
	public boolean saturation = true;
	@Hook("net.minecraft.client.renderer.ItemRenderer#renderItemInFirstPerson(float partialTicks) -> line 393"
			+ "com.xenon.modules.api.OldItemsAPI#handleLayerHeldItem() -> line 38"
			+ "net.minecraft.client.model.ModelBiped#setRotationAngles(...) -> line 175"
			+ "net.minecraft.client.multiplayer.PlayerControllerMP(...) -> line 289"
			+ "com.xenon.modules.api.MinecraftAPI#sendClickBlockToController(...) -> line 28")
	public boolean oldBlockhit = true;
	public boolean oldBow = true;
	public boolean oldFishingRod = true;
	@Hook("net.minecraft.client.gui.GuiIngame#renderPlayerStats(ScaledResolution) -> line 626")
	public boolean oldhearts = true;
	@Hook("net.minecraft.client.renderer.entity.RendererLivingEntity#setBrightness(...) -> line 422")
	public boolean oldDamages = true;
	public boolean oldSneak = true;
	public boolean oldHitreg = false;
	public float horsejumpbarScale = 1;
	public int flyBoost = 1;
	
	public float fpsScale = 1;
	public ScreenPos fpsxy = new ScreenPos(0.01d, 0.05d);
	
	public float posScale = 1;
	public ScreenPos posxy = new ScreenPos(0.01d, 0.07d);
	
	public float biomeScale = 1;
	public ScreenPos biomexy = new ScreenPos(0.01d, 0.03d);
	
	
	public float armorstatusScale = 1;
	public boolean armorstatusDurability = true;
	public ScreenPos armorstatusxy = new ScreenPos(0.95d, 0.6d);
	
	public float keystrokesScale = 1;
	public boolean keystrokesCPS = true;
	public ScreenPos keystrokesxy = new ScreenPos(0.7d, 0.4d);
	
	public float potionoverlayScale = 1;
	public ScreenPos potionoverlayxy = new ScreenPos(0.01d, 0.04d);
	
	public float scoreboardScale = 1;
	public float titleScale = 1;
	public int titlealpha = 255;
	
	/**
	 * Don't use this field directly
	 */
	@Hook("com.xenon.modules.api.GlintEnchantAPI")
	public int glintColor = -8372020;	//base vanilla glint color
	/**
	 * Don't use this field directly
	 */
	@Hook("com.xenon.modules.api.GlintEnchantAPI")
	public boolean glintChroma = false;
	@Hook("com.xenon.modules.api.TextAPI#onClientChatReceived(IChatComponent) -> line 95")
	public boolean chatFilter = true;
	@Hook("com.xenon.modules.api.TextAPI")
	public boolean customnameEnabled = true;
	public String customName = "\u00A7ahello world";
	
	public float reequipTime = 1;
	@Hook("net.minecraft.item.ItemStack#getTooltip(...) -> line 650")
	public boolean enhancedToolTip = false;
	@Hook("net.minecraft.client.gui.GuiNewChat#drawChat(int) -> line 81")
	public boolean chatBackground = true;
	/**
	 * Allows to hide capes other than Xenon's ones. Faster.
	 */
	@Hook("com.xenon.client.cape.CapeManager#doRenderCape(...) -> line 36")
	public boolean showCapes = false;
	@Hook("net.minecraft.client.entity.AbstractClientPlayer#AbstractClientPlayer() -> line 51")
	public boolean capeParticles = true;
}

package com.xenon.modules.api;

import com.xenon.XenonClient;
import com.xenon.util.readability.Hook;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class OldAnimationsAPI {
	
	@Hook("net.minecraft.client.renderer.entity.layers.LayerHeldItem#doRenderLayer(...) -> line 19")
	public static void handleLayerHeldItem(LayerHeldItem instance, EntityLivingBase entity) {
		ItemStack itemstack = entity.getHeldItem();

        if (itemstack != null)
        {
            GlStateManager.pushMatrix();
            Minecraft mc = Minecraft.getMinecraft();

            if (instance.livingEntityRenderer.getMainModel().isChild)
            {
                float f = 0.5F;
                GlStateManager.translate(0.0F, 0.625F, 0.0F);
                GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
                GlStateManager.scale(f, f, f);
            }
            
            if (XenonClient.instance.settings.oldBlockhit) {
            	EntityPlayer player = mc.theWorld.getPlayerEntityByUUID(entity.getUniqueID());
            	
            	if (player != null && player.isBlocking()) {
            		((ModelBiped)instance.livingEntityRenderer.getMainModel()).postRenderArm(0.0325f);
            		if (player.isSneaking())
            			GlStateManager.translate(-0.58f, 0.3f, -0.2f);
            		else
            			GlStateManager.translate(-0.48f, 0.2f, -0.2f);
            		
            		GlStateManager.rotate(-24390.0F, 137290.0F, -2009900.0F, -2054900.0F);
            	}else 
            		((ModelBiped)instance.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F);
            	
            }else
            	((ModelBiped)instance.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F);
            
            
            GlStateManager.translate(-0.0625F, 0.4375F, 0.0625F);

            if (entity instanceof EntityPlayer && ((EntityPlayer)entity).fishEntity != null)
            {
                itemstack = new ItemStack(Items.fishing_rod, 0);
            }

            Item item = itemstack.getItem();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item).getRenderType() == 2)
            {
                GlStateManager.translate(0.0F, 0.1875F, -0.3125F);
                GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                float f1 = 0.375F;
                GlStateManager.scale(-f1, -f1, f1);
            }

            if (entity.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.203125F, 0.0F);
            }

            mc.getItemRenderer().renderItem(entity, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON);
            GlStateManager.popMatrix();
        }
	}
	
	@Hook("net.minecraft.client.renderer.ItemRenderer#transformFirstPersonItem(float, float) -> line 311")
	public static void handleTransformFirstPersonItem(Minecraft mc, float equipProgress, float swingProgress) {
		if (XenonClient.instance.settings.oldBow && mc != null && mc.thePlayer != null && mc.thePlayer.getItemInUse() != null && mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem) != null && mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem).getItem() == Items.bow)
            GlStateManager.translate(0.0F, 0.0F, -0.08F);

		else if (XenonClient.instance.settings.oldFishingRod && mc != null && mc.thePlayer != null && mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem) != null && mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem).getItem() == Items.fishing_rod)
            GlStateManager.translate(0.1F, -0.02F, -0.335F);
		
		GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, equipProgress * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        GlStateManager.rotate(f * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f1 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
	}
	
	private static long sneak = 0L;
	private static boolean isSneaking = false;
	
	/**
	 * Only works in singleplayer as it changes the {@link net.minecraft.entity.player.EntityPlayer} class.
	 * @param player
	 * @return
	 */
	@Hook("net.minecraft.entity.player.EntityPlayer#getEyeHeight() -> line 2330")
	public static float handleEyeHeight(EntityPlayer player) {
		if (XenonClient.instance.settings.oldSneak) {
			if (isSneaking != player.isSneaking() || sneak <= 0L)
	            sneak = System.currentTimeMillis();

			isSneaking = player.isSneaking();
	        float f = 1.62F;

	        if (player.isSneaking())
	        {
	            int i = (int)(sneak + 8L - System.currentTimeMillis());

	            if (i > -50)
	            {
	                f += (float)((double)i * 0.0017D);

	                if (f < 0.0F || f > 10f)
	                {
	                    f = 1.54F;
	                }
	            }
	            else
	            {
	                f = (float)((double)f - 0.08D);
	            }
	        }
	        else
	        {
	            int j = (int)(sneak + 8L - System.currentTimeMillis());

	            if (j > -50)
	            {
	                f -= (float)((double)j * 0.0017D);
	                f = (float)((double)f - 0.08D);

	                if (f < 0.0f)
	                    f = 1.62F;
	            }
	        }

	        return f;
		}else {
			float f = 1.62F;

	        if (player.isPlayerSleeping())
	            f = 0.2F;

	        if (player.isSneaking())
	            f -= 0.08F;

	        return f;
		}
		
	}

}

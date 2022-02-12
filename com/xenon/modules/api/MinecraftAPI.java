package com.xenon.modules.api;

import com.xenon.XenonClient;
import com.xenon.util.readability.Hook;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
/**
 * 
 * @author VoidAlchemist
 *
 */
public class MinecraftAPI {
	
	/**
	 * 
	 * @param instance
	 * @param leftClick
	 */
	@Hook("net.minecraft.client.Minecraft#sendClickBlockToController(boolean leftClick) -> line 1531")
	public static void sendClickBlockToController(Minecraft instance, boolean leftClick) {
		if (!XenonClient.instance.settings.oldHitreg && !leftClick)
            instance.leftClickCounter = 0;

        if ((XenonClient.instance.settings.oldHitreg || instance.leftClickCounter <= 0) &&
        		(!instance.thePlayer.isUsingItem() || XenonClient.instance.settings.oldBlockhit))	// allow hitting blocks while using items
        {
            if (leftClick && instance.objectMouseOver != null && instance.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                BlockPos blockpos = instance.objectMouseOver.getBlockPos();

                if (instance.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air && instance.playerController.onPlayerDamageBlock(blockpos, instance.objectMouseOver.sideHit))
                {
                    instance.effectRenderer.addBlockHitEffects(blockpos, instance.objectMouseOver.sideHit);
                    instance.thePlayer.swingItem();
                }
            }
            else
                instance.playerController.resetBlockRemoving();
        }
	}
	
	/**
	 * 
	 * @param instance
	 */
	@Hook("net.minecraft.client.Minecraft#clickMouse() -> line 1537")
	public static void clickMouse(Minecraft instance) {
		instance.thePlayer.swingItem();

        if (instance.objectMouseOver == null)
        	instance.logger.error("Null returned as \'hitResult\', this shouldn\'t happen!");
        else
        {
            switch (instance.objectMouseOver.typeOfHit)
            {
                case ENTITY:
                	instance.playerController.attackEntity(instance.thePlayer, instance.objectMouseOver.entityHit);
                    break;

                case BLOCK:
                    BlockPos blockpos = instance.objectMouseOver.getBlockPos();

                    if (instance.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air)
                    {
                    	instance.playerController.clickBlock(blockpos, instance.objectMouseOver.sideHit);
                        break;
                    }

                case MISS:
            }
        }
	}
	
	

}

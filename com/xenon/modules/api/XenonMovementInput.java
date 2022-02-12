package com.xenon.modules.api;

import com.xenon.XenonClient;
import com.xenon.util.readability.Hook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;

/**
 * We use a external variable to store if print is toggled,<br>
 * otherwise it's too annoying to toggle it back each time we change world (particularly on servers).
 * @author VoidAlchemist
 * @see com.xenon.XenonClient#shouldToggleSprint
 * @see net.minecraft.client.entity.EntityPlayerSP#movementInput
 */
@Hook("net.minecraft.client.Minecraft#setDimensionAndSpawnPlayer(int) -> line 2461"
		+ "net.minecraft.client.Minecraft#loadWorld(WorldClient, String) -> line 2423")
public class XenonMovementInput extends MovementInput{
	
	private boolean sneaktoggled = false;
	private int sneakWasPressed, sprintWasPressed = 0;
	private float originalFlySpeed = -1.0F;
	private float boostedFlySpeed = 0.0F;
	private GameSettings gameSettings;
	private Minecraft mc;
	private EntityPlayerSP player;
	
	public XenonMovementInput(GameSettings s)
	{
		gameSettings = s;
		mc = Minecraft.getMinecraft();
	}
	
	@Override
	public void updatePlayerMoveState() 
	{
		player = mc.thePlayer;
		moveStrafe = 0.0f;
		moveForward = 0.0f;
		
		if (gameSettings.keyBindForward.isKeyDown())
			moveForward++;
		
		if (gameSettings.keyBindBack.isKeyDown())
			moveForward--;
		
		if (gameSettings.keyBindLeft.isKeyDown())
			moveStrafe++;
		
		if (gameSettings.keyBindRight.isKeyDown())
			moveStrafe--;
		
		jump = gameSettings.keyBindJump.isKeyDown();
		
		
		if (XenonClient.instance.settings.toggleSneak)
		{
			if (gameSettings.xenonBindSneakToggled.isKeyDown())
			{
				if (sneakWasPressed == 0)
				{
					if (sneaktoggled)	sneakWasPressed = -1;
					
					else if (player.isRiding() || player.capabilities.isFlying)
						sneakWasPressed = 8;
					
					else
						sneakWasPressed = 1;
					
					sneaktoggled = !sneaktoggled;
					
				}
				else if (sneakWasPressed > 0)
					sneakWasPressed++;
				
			}
			else {
				if (sneakWasPressed > 7)
					sneaktoggled = false;
				
				sneakWasPressed = 0;
			}
		}else
			sneaktoggled = false;
		
		sneak = sneaktoggled ? true : gameSettings.keyBindSneak.isKeyDown();
		
		
		
		if (sneak)
		{
			moveStrafe *= 0.3F;
			moveForward *= 0.3F;
		}
		
		
		if (XenonClient.instance.settings.toggleSprint){
			if (gameSettings.xenonBindSprintToggled.isKeyDown()){
				if (sprintWasPressed == 0){
					if (XenonClient.instance.shouldToggleSprint)	sprintWasPressed = -1;
					
					else if (player.capabilities.isFlying)
						sprintWasPressed = 8;
					
					else	sprintWasPressed = 1;
					
					XenonClient.instance.shouldToggleSprint = !XenonClient.instance.shouldToggleSprint;
				}
				else if (sprintWasPressed > 0)
					sprintWasPressed++;
				
				
			}
			else {
				if (sprintWasPressed > 7)
					XenonClient.instance.shouldToggleSprint = false;
				sprintWasPressed = 0;
				
			}
		}
		else
			XenonClient.instance.shouldToggleSprint = false;
		
		
		
		if (XenonClient.instance.shouldToggleSprint && moveForward == 1.0F && !player.isUsingItem() && !player.isPotionActive(Potion.blindness))
			player.setSprinting(true);
		
		
		if (XenonClient.instance.settings.flyBoost > 1 && player.capabilities.isCreativeMode && player.capabilities.isFlying && 
				mc.getRenderViewEntity() == player && (XenonClient.instance.shouldToggleSprint || gameSettings.keyBindSprint.isKeyDown()))
		{
			if (originalFlySpeed < 0.0F && player.capabilities.getFlySpeed() != boostedFlySpeed)
				originalFlySpeed = player.capabilities.getFlySpeed();
			
			boostedFlySpeed = originalFlySpeed * XenonClient.instance.settings.flyBoost;
			player.capabilities.setFlySpeed(boostedFlySpeed);
			
			if (sneak)
				player.motionY -= 0.15D * (double)(XenonClient.instance.settings.flyBoost - 1f);
			
			if (jump)
				player.motionY += 0.15D * (double)(XenonClient.instance.settings.flyBoost - 1f);
			
			
		}
		else {
			if (player.capabilities.getFlySpeed() == boostedFlySpeed)
				player.capabilities.setFlySpeed(originalFlySpeed);
			
			originalFlySpeed = -1.0F;
			
		}
		
		
	}

}

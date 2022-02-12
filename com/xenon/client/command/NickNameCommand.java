package com.xenon.client.command;

import java.util.Arrays;

import com.xenon.XenonClient;
import com.xenon.util.readability.Hook;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
/**
 * Custom Client-side command. Forge Hooks aren't present, so necessary intercept in our own way the chat messages to process them.<br>
 * Done in {@link net.minecraft.client.gui.GuiChat#keyTyped()}.
 * @author VoidAlchemist
 *
 */
public class NickNameCommand {

	private static final String error = "\u00A7cInvalid Syntax : /xenonnickname:xnm set:show:toggle <new nickname>";
	
	private static void process(String[] msg) {
		if (msg.length < 2 || !(msg[0].equals("/xenonnickname") || msg[0].equals("/xnm"))) {
			sendmsg(error);
			return;
		}
		String op = msg[1];
		if (op.equals("set")) {
			if (msg.length != 3) {	//goto, help me
				sendmsg(error);
				return;
			}
			
			String newnickname = XenonClient.instance.settings.customName = msg[2].replaceAll("&", "\u00A7");
			sendmsg("\u00A7aSet \u00A7r"+newnickname+" \u00A7aas a nickname.");
		}else if (op.equals("show")) {
			String nickname = XenonClient.instance.settings.customName;
			nickname.replaceAll("\u00A7", "&");
			sendmsg(nickname + " ('&' is used in place of the paragraph symbol)");
		}else if (op.equals("toggle")) {
			boolean newState = XenonClient.instance.settings.customnameEnabled = !XenonClient.instance.settings.customnameEnabled;
			sendmsg("\u00A7aNickname is now "+(newState ? "enabled" : "disabled")+".");
		}else
			sendmsg(error);
	}
	
	/**
	 * 
	 * @param msg
	 */
	@Hook("net.minecraft.client.gui.GuiChat#keyTyped() -> line 127")
	public static void process(String msg) {
		assert msg != null : "trying to process an empty string as a command.";
		process(msg.split("\\s+", 3));	//allows nicknames containing spaces!
	}
	
	private static void sendmsg(String s) {
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(s));
	}
}

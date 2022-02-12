# Xenon-Client-v2
An open-source PvP 1.8.9 client for people to study

Note that I didn't try to reinvent the wheel or anything, I just wanted my own PvP client (for security) plus a lot of uncommon features.

Uncommon features include:<br><br>
- **Scoreboard resizing**: Lunar does it but labymod does not, which is a hassle if you must play with forge.<br><br>
- **Title resizing and blending**: In 10 years of playing Minecraft, I've never found a mod which features title customization. It may sounds ludicrous, but having played mini-walls on Hypixel quite seriously, I can tell the title are just too much in the middle of a fight. "Your mini wither is low" taking 80% of your screen is kind and all, but I wanna play the game. So in the end, I came up with a resizing feature (necessary) but I also found changing opacity quite handy as well.<br><br>
- **Speed FOV**: In my opinion, the FOV manager of lunar isn't praticle at all. Most of the time, all you need is to be able to see something even with Speed II. Labymod does feature it, but again, if you find forge slow, you're stuck.<br><br>
- **Revamped chat**: the default 1.8 Minecraft chat's dark background takes a lot of space, even if the messages are small. So you need to resize the entire chat, but then, you can't read anything. This client simply draws, for each line, a rectangular background which width depends of the length of the message. It's not the best aesthetically speaking, but it's so good to PvP with it (or you can just disable the chat background for even better HUD experience). <br>
WARNING : While you can enable/disable the chat background, you cannot restore the vanilla big ugly rectangle (because I didn't find it necessary to create a setting for that).
- **Item switch animation**: When you switch items in hand, you'll find a little animation displaying the new held item going up. The animation is good as it is, although 1.7's is a bit better. However, it often makes my eyes bug for a second when the item isn't fully "up" and I want to know if I have the right item in hand. This client adds a feature that allows you to shorten the animation up to 0 with no transition at all. For it to be benefical, you need to have a extremelly good mouse wheel, and to perfectly control it, but for those like me who still don't use key shortcuts for refill/healing, it's such a blessing.


Known bug that keeps strangling me with every MCP project I create : Crash when the player has the audacity to click on Resource pack Gui, i.e you cannot edit your current resourcepack while using this version. Handy, isn't it. I'll make sure to fix this bug whenever I get some motivation somehow.

PS: Keep in mind that this client's code is far from being presentable and that I still edit way too much of Vanilla code (I didn't put the vanilla folders on git on purpose because Mojang EULA is a thing). In order to grasp roughly what my code is actually doing, I put some "Hook" annotations with a target as paramaterer, so that the reader may understand where this is executed in the vanilla code.

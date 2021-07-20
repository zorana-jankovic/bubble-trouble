# bubble-trouble

### Description

Bubble Trouble is a 2D game implemented in Java using JavaFX which is based and similar to existing [Bubble Trouble](https://www.miniclip.com/games/bubble-trouble/en/) game.

There is a main menu with submenus like options, commands, entering new name, starting game, continuing new game etc.
There are background music and sound effects.
Player can configure music volume and window mode.
Window dimensions are scalable based on the screen size and will remain the same on different screen sizes.

Player plays the game with moving player left and right trying to avoid balls, while also trying to shoot and destroy them with the harpoon, when player hits the ball it divides into 2 balls with smaller size until smallest size. When there is no ball left player goes to the next level. When a ball hit the player he loses 1 life, if player loses all lifes game is over and his score will be stored if it is in the top 10. 

There are also fake balls which can appear with some probability and will disappear when they hit the player or player hits them with the harpoon, their purpose is to make game harder, their color is semi transparent so player can differ them from real ones.

Every level is time limited and if player doesn't complete level in the given time game will be over.

When player hits the ball, there is a chance for drop of a bonus. Bonuses that exist are:
- Life (Adding life to the player)
- Watch (Increase remaining time)
- Bunker (Player can position it anywhere and for some time it will shoot balls)
- Shield (Player becomes semi transparent and resistant to the next hit of a ball for some short time)
- Time slow (Slowing speed of balls and time passage for some short time)

There are 3 main rooms, but infinite number of levels, each level is harder than previous one. Difficulty is increased based on number of balls, 
speed of balls and size of balls including division level of balls.

In the main menu there is option *Manual* which will open PDF file that describes everything that exist in the game.

### Pictures

Some of menus:

**Main menu:**
![main_menu](https://user-images.githubusercontent.com/48600224/126399898-ef4f0ed7-6667-4e4a-ac54-63599c0be7d5.png)

**Commands menu:**
![commands](https://user-images.githubusercontent.com/48600224/126399983-4bad03af-903a-43a3-8a82-149b5e73cbbb.png)

**Options menu:**
![settings](https://user-images.githubusercontent.com/48600224/126400005-e0715e31-7af5-46ce-8ec1-9c6cc410f088.png)

Gameplay images that show look of 3 main rooms:

**Room 1:**
![image](https://user-images.githubusercontent.com/48600224/109743867-ab76c100-7bd1-11eb-899b-e5f030d4e525.png)

**Room 2:**
![level_2](https://user-images.githubusercontent.com/48600224/126400170-6dbcadfd-0cf5-405a-85e2-084c34af3b81.png)

**Room 3:**
![level_3](https://user-images.githubusercontent.com/48600224/126400174-2fd88348-ca3f-4042-bccb-532d17393686.png)

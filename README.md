# ShowMeMyDPS
This is a mod to show total damage within a specific period of time

It automatically counts the damage after the start of the following battles and displays the result in the chat box after the battle is over
<br>
・During the battle with Raid bosses (Grootslang, Orphion, Canyon Colossus, Greg)
<br>
・Legendary Islands

Download Latest:<br><https://github.com/soramame0256/showmemydps/releases/download/1.4-SNAPSHOT/showmemydps-1.4-SNAPSHOT.jar>
# What are the outputs
DPS: Average DPS during the battle
<br>
(The total of the numbers in the names of armor stands beginning with "-" that appeared during the battle, divided by the total battle time)


Avg/hit: Average damage per hit during the battle
<br>
(The total of the numbers in the names of armor stands beginning with "-" that appeared during the battle, divided by the number of armor stands)

Total:Total Damage caused during the battle
<br>
(The total of the numbers in the names of armor stands beginning with "-" that appeared during the battle)
# Notes
The following types of damage will also be counted
<br>
・Damage from enemy attacks you receive
<br>
・Damage caused to minions (Greater One, etc.) that appear during the battle

The following time is also counted as DPS time
<br>
・When the Boss is invincible (during Watched, etc.) or when you are running away from the enemy
<br>
・Time spent in the Legendary Islands break room

Spells/abilities such as Guardian Angel will not be counted perfectly.
<details>
  <summary>Why?</summary>
   The server removes their damage indicator before final damage,<br>
   so this mod cannot detect damage dealt after that point.
</details>

Please note these will cause errors in the damage outputs
# How to use
・In Raid Boss/Legendary Island
<br>
The count will start automatically after the start of the battle
<br>
・Manual
<br>
The output is generated by /showdps, and reset and output by /showdps reset 

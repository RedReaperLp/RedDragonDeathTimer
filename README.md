This is a plugin mostly for Speedrunners, who don´t want to use a Stream timer. Here you have the Timer in the Actionbar above your Experiencebar. The Timer will automatically be stopped, if you beat the Ender_Dragon.

It is compatible with the RedPermsAPI to give you the ability to set permissions, for who can use the following commands:

Link to RedPermsAPI: https://www.github.com/RedReaperLp/RedPermsAPI

```yaml
/timer toggle :
- Starts/Stops the timer
- Says in chat, if Timer is en/disabled

/timer auto :
- en/disables timer start on player-movement
- says in chat, if this is en/disabled
- keeps settings after server restart

/timer reset :
- resets the server after 10 seconds
- new world generation on server-startup
- timer will be reseted
- keeps settings after server restart

/timer hint :
- en/disables Player-Damage Indicator
- says in chat, if this is en/disabled
- keeps settings after server restart


Player-Death :
- on Player-Death Timer will say "Failed"
- Bossbar with Countdown from 180 Seconds

Player-Damage :
- says who took damage and how many lives are left
```
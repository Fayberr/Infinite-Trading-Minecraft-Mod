# Infinite Trading

A NeoForge mod that lets operators grant infinite trading to specific players.
Villager trades never lock up for authorized players, and demand-based price
increases are reset after every trade.

## Commands

- `/inftrade enable <players>` grants infinite trading. Requires operator level 2.
- `/inftrade disable <players>` revokes it, even in singleplayer.

In singleplayer and LAN worlds infinite trading is on by default, no setup needed. On dedicated servers it is off until enabled per player.

The list of authorized players is stored in the world data, so it survives restarts.

## Details

- Runs on the server. The integrated server in singleplayer also works.
- Clients do not need the mod installed.
- Trade uses and demand are reset after each trade, and the trading screen updates
  immediately.

## Requirements

- NeoForge 21.1.65 or newer for Minecraft 1.21.1.

## Building

JDK 21 and a NeoForge 1.21.1 development environment.

```bash
./gradlew build
```

The jar is in `build/libs/`.

## License

GPL-3.0-or-later

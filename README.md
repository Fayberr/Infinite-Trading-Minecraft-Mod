# Infinite Trading

A Fabric mod that lets operators grant infinite trading to specific players.
Villager trades never lock up for authorized players, and demand-based price
increases are reset after every trade.

This is the Fabric port for Minecraft 26.1.2 (deobfuscated mappings, Java 25).
There is also a NeoForge 1.21.1 version in this repository.

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

- Fabric Loader 0.19.3 or newer for Minecraft 26.1.2.
- Fabric API for 26.1.2.
- Java 25.

## Building

JDK 25 and Gradle 9.7 or newer (Loom 1.17.19).

```bash
./gradlew build
```

The jar is in `build/libs/`.

## License

GPL-3.0-or-later

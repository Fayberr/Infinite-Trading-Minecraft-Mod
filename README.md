# Infinite Trading Minecraft Mod

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.1-blue.svg)
![Mod Loader](https://img.shields.io/badge/Loader-NeoForge-orange.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)
![Author](https://img.shields.io/badge/Author-Fayber-purple.svg)

A server-side utility for NeoForge 1.21.1 that allows server operators to grant infinite trading capabilities to specific players.

## 🚀 Features

- **Per-Player Control:** Enable or disable infinite trading for specific players without affecting others.
- **Trade Lock Prevention:** For authorized players, villager trades never lock up, and demand-based price increases are reset.
- **Instant Synchronization:** The trading UI updates in real-time when trades are reset.
- **Persistence:** Authorized players are saved in the world data and persist across server restarts.
- **Server-Side Only:** Can be installed on the server only. Clients do not need the mod to connect.

## 🛠 Specifications

- **Version:** `1.0.0`
- **Minecraft Version:** `1.21.1`
- **Mod Loader:** `NeoForge` (javafml)
- **Minimum NeoForge Version:** `21.1.65`
- **Side:** `Server-side (Client optional)`

## 🎮 Usage

### Commands
- **/inftrade enable {player}** - Grants the specified player(s) infinite trading. (Requires OP Level 2)
- **/inftrade disable {player}** - Removes infinite trading from the specified player(s). (Requires OP Level 2)

## 📥 Installation

1. Ensure you have the latest **NeoForge** installed for 1.21.1 on your server.
2. Download the `.jar` from the releases page (or build it yourself).
3. Drop the file into your server's `mods` folder.
4. (Optional) Clients can install it for a smoother UI experience, but it is not required.

## 🏗 Building

The project uses Gradle and OpenJDK 21.
```powershell
./gradlew build
```
The compiled jar will be in `build/libs/`.

---
*Created by Fayber*

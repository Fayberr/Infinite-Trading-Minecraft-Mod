package com.derfa.playerinftrading;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InfiniteTradingData extends SavedData {
    private final Set<UUID> infiniteTraders = new HashSet<>();
    private final Set<UUID> optedOut = new HashSet<>();
    // Not persisted: true on singleplayer/integrated servers, false on dedicated servers.
    private boolean defaultOn;

    public InfiniteTradingData() {}

    public boolean isInfinite(UUID playerUuid) {
        if (optedOut.contains(playerUuid)) return false;
        return defaultOn || infiniteTraders.contains(playerUuid);
    }

    public void addPlayer(UUID playerUuid) {
        infiniteTraders.add(playerUuid);
        optedOut.remove(playerUuid);
        setDirty();
    }

    public void removePlayer(UUID playerUuid) {
        infiniteTraders.remove(playerUuid);
        optedOut.add(playerUuid);
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (UUID uuid : infiniteTraders) {
            list.add(StringTag.valueOf(uuid.toString()));
        }
        tag.put("traders", list);
        ListTag off = new ListTag();
        for (UUID uuid : optedOut) {
            off.add(StringTag.valueOf(uuid.toString()));
        }
        tag.put("optedOut", off);
        return tag;
    }

    public static InfiniteTradingData load(CompoundTag tag, HolderLookup.Provider registries) {
        InfiniteTradingData data = new InfiniteTradingData();
        ListTag list = tag.getList("traders", Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++) {
            try {
                data.infiniteTraders.add(UUID.fromString(list.getString(i)));
            } catch (IllegalArgumentException ignored) {}
        }
        ListTag off = tag.getList("optedOut", Tag.TAG_STRING);
        for (int i = 0; i < off.size(); i++) {
            try {
                data.optedOut.add(UUID.fromString(off.getString(i)));
            } catch (IllegalArgumentException ignored) {}
        }
        return data;
    }

    public static InfiniteTradingData get(Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            throw new RuntimeException("Must be called on server side");
        }
        DimensionDataStorage storage = serverLevel.getServer().overworld().getDataStorage();
        InfiniteTradingData data = storage.computeIfAbsent(new SavedData.Factory<>(InfiniteTradingData::new, InfiniteTradingData::load), "playerinftrading");
        data.defaultOn = !serverLevel.getServer().isDedicatedServer();
        return data;
    }
}

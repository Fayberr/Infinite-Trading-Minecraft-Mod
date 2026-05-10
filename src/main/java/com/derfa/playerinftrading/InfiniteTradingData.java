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

    public InfiniteTradingData() {}

    public boolean isInfinite(UUID playerUuid) {
        return infiniteTraders.contains(playerUuid);
    }

    public void addPlayer(UUID playerUuid) {
        infiniteTraders.add(playerUuid);
        setDirty();
    }

    public void removePlayer(UUID playerUuid) {
        infiniteTraders.remove(playerUuid);
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (UUID uuid : infiniteTraders) {
            list.add(StringTag.valueOf(uuid.toString()));
        }
        tag.put("traders", list);
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
        return data;
    }

    public static InfiniteTradingData get(Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            throw new RuntimeException("Must be called on server side");
        }
        DimensionDataStorage storage = serverLevel.getServer().overworld().getDataStorage();
        return storage.computeIfAbsent(new SavedData.Factory<>(InfiniteTradingData::new, InfiniteTradingData::load), "playerinftrading");
    }
}

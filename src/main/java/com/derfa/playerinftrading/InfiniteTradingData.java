package com.derfa.playerinftrading;

import com.mojang.serialization.Codec;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Uuids;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InfiniteTradingData extends PersistentState {
    private final Set<UUID> infiniteTraders = new HashSet<>();

    public static final Codec<InfiniteTradingData> CODEC = Uuids.CODEC.listOf().xmap(list -> {
        InfiniteTradingData data = new InfiniteTradingData();
        data.infiniteTraders.addAll(list);
        return data;
    }, data -> new ArrayList<>(data.infiniteTraders));

    public InfiniteTradingData() {}

    public boolean isInfinite(UUID playerUuid) {
        return infiniteTraders.contains(playerUuid);
    }

    public void addPlayer(UUID playerUuid) {
        if (infiniteTraders.add(playerUuid)) {
            markDirty();
        }
    }

    public void removePlayer(UUID playerUuid) {
        if (infiniteTraders.remove(playerUuid)) {
            markDirty();
        }
    }

    public static InfiniteTradingData get(World world) {
        if (!(world instanceof ServerWorld serverWorld)) {
            throw new RuntimeException("Must be called on server side");
        }
        PersistentStateType<InfiniteTradingData> type = new PersistentStateType<>(
            "playerinftrading",
            InfiniteTradingData::new,
            CODEC,
            DataFixTypes.LEVEL
        );
        return serverWorld.getServer().getOverworld().getPersistentStateManager().getOrCreate(type);
    }
}

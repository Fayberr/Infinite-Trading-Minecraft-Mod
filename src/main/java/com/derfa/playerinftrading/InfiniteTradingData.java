package com.derfa.playerinftrading;

import com.mojang.serialization.Codec;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InfiniteTradingData extends PersistentState {
    private final Map<UUID, Boolean> overrides = new HashMap<>();
    private boolean isDedicated = false;

    public static final Codec<InfiniteTradingData> CODEC = Codec.unboundedMap(Codec.STRING, Codec.BOOL).xmap(map -> {
        InfiniteTradingData data = new InfiniteTradingData();
        map.forEach((uuidStr, enabled) -> {
            try {
                data.overrides.put(UUID.fromString(uuidStr), enabled);
            } catch (IllegalArgumentException ignored) {}
        });
        return data;
    }, data -> {
        Map<String, Boolean> map = new HashMap<>();
        data.overrides.forEach((uuid, enabled) -> map.put(uuid.toString(), enabled));
        return map;
    });

    public InfiniteTradingData() {}

    public boolean isInfinite(UUID playerUuid) {
        if (overrides.containsKey(playerUuid)) {
            return overrides.get(playerUuid);
        }
        return !isDedicated;
    }

    public void setInfinite(UUID playerUuid, boolean enabled) {
        if (overrides.containsKey(playerUuid) && overrides.get(playerUuid) == enabled) {
            return;
        }
        overrides.put(playerUuid, enabled);
        markDirty();
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
        InfiniteTradingData data = serverWorld.getServer().getOverworld().getPersistentStateManager().getOrCreate(type);
        data.isDedicated = serverWorld.getServer().isDedicated();
        return data;
    }
}

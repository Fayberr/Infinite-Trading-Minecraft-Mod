package com.derfa.playerinftrading;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.SavedDataStorage;

import java.util.HashSet;
import java.util.List;
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

    public static final Codec<InfiniteTradingData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.listOf().optionalFieldOf("traders", List.of()).forGetter(data ->
                    data.infiniteTraders.stream().map(UUID::toString).toList())
    ).apply(instance, traders -> {
        InfiniteTradingData data = new InfiniteTradingData();
        for (String uuid : traders) {
            try {
                data.infiniteTraders.add(UUID.fromString(uuid));
            } catch (IllegalArgumentException ignored) {}
        }
        return data;
    }));

    public static final SavedDataType<InfiniteTradingData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath("infinitetrading", "playerinftrading"),
            InfiniteTradingData::new,
            CODEC,
            DataFixTypes.SAVED_DATA_COMMAND_STORAGE
    );

    public static InfiniteTradingData get(Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            throw new RuntimeException("Must be called on server side");
        }
        SavedDataStorage storage = serverLevel.getDataStorage();
        return storage.computeIfAbsent(TYPE);
    }
}

package com.derfa.playerinftrading.mixin;

import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TradeOffer.class)
public interface MerchantOfferAccessor {
    @Accessor("demandBonus")
    void setDemand(int demand);
}

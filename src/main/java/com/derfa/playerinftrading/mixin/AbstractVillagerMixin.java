package com.derfa.playerinftrading.mixin;

import com.derfa.playerinftrading.InfiniteTradingData;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.jetbrains.annotations.Nullable;

@Mixin(MerchantEntity.class)
public abstract class AbstractVillagerMixin {

    @Shadow @Nullable protected PlayerEntity customer;

    @Inject(method = "getOffers", at = @At("RETURN"))
    private void onGetOffers(CallbackInfoReturnable<TradeOfferList> cir) {
        MerchantEntity villager = (MerchantEntity) (Object) this;
        if (this.customer != null && !villager.getEntityWorld().isClient()) {
            if (InfiniteTradingData.get(villager.getEntityWorld()).isInfinite(this.customer.getUuid())) {
                TradeOfferList offers = cir.getReturnValue();
                if (offers != null) {
                    for (TradeOffer offer : offers) {
                        offer.resetUses();
                        offer.setSpecialPrice(0);
                        ((MerchantOfferAccessor) offer).setDemand(0);
                    }
                }
            }
        }
    }

    @Inject(method = "trade", at = @At("TAIL"))
    private void onTrade(TradeOffer offer, CallbackInfo ci) {
        MerchantEntity villager = (MerchantEntity) (Object) this;
        if (this.customer != null && !villager.getEntityWorld().isClient()) {
            if (InfiniteTradingData.get(villager.getEntityWorld()).isInfinite(this.customer.getUuid())) {
                offer.resetUses();
                offer.setSpecialPrice(0);
                ((MerchantOfferAccessor) offer).setDemand(0);

                int level = 1;
                int xp = 0;
                if (villager instanceof VillagerEntity v) {
                    level = v.getVillagerData().level();
                    xp = v.getExperience();
                }

                this.customer.sendTradeOffers(
                    this.customer.currentScreenHandler.syncId,
                    villager.getOffers(),
                    level,
                    xp,
                    villager.isLeveledMerchant(),
                    villager.canRefreshTrades()
                );
            }
        }
    }
}

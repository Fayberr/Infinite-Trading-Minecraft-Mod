package com.derfa.playerinftrading.mixin;

import com.derfa.playerinftrading.InfiniteTradingData;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractVillager.class)
public abstract class AbstractVillagerMixin {

    @Shadow protected Player tradingPlayer;

    @Inject(method = "getOffers", at = @At("RETURN"))
    private void onGetOffers(CallbackInfoReturnable<MerchantOffers> cir) {
        AbstractVillager villager = (AbstractVillager) (Object) this;
        if (this.tradingPlayer != null && !villager.level().isClientSide()) {
            if (InfiniteTradingData.get(villager.level()).isInfinite(this.tradingPlayer.getUUID())) {
                MerchantOffers offers = cir.getReturnValue();
                if (offers != null) {
                    for (MerchantOffer offer : offers) {
                        offer.resetUses();
                        offer.setSpecialPriceDiff(0);
                        ((MerchantOfferAccessor) offer).setDemand(0);
                    }
                }
            }
        }
    }

    @Inject(method = "notifyTrade", at = @At("TAIL"))
    private void onNotifyTrade(MerchantOffer offer, CallbackInfo ci) {
        AbstractVillager villager = (AbstractVillager) (Object) this;
        if (this.tradingPlayer != null && !villager.level().isClientSide()) {
            if (InfiniteTradingData.get(villager.level()).isInfinite(this.tradingPlayer.getUUID())) {
                offer.resetUses();
                offer.setSpecialPriceDiff(0);
                ((MerchantOfferAccessor) offer).setDemand(0);

                // Synchronize to client so the UI updates immediately
                int level = 1;
                int xp = 0;
                if (villager instanceof Villager v) {
                    level = v.getVillagerData().level();
                    xp = v.getVillagerXp();
                }

                this.tradingPlayer.sendMerchantOffers(
                    this.tradingPlayer.containerMenu.containerId,
                    villager.getOffers(),
                    level,
                    xp,
                    villager.showProgressBar(),
                    villager.canRestock()
                );
            }
        }
    }
}

package com.example.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.InfoEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(targets = "net.minecraft.village.TradeOffers$EnchantBookFactory")
public class NoGarbageEnchantmentBookMixin {

    @Inject(at = @At("RETURN"), method = "create", cancellable = true)
    private void init(Entity entity, Random random, CallbackInfoReturnable<TradeOffer> cir) {
        Enchantment enchantment = Registry.ENCHANTMENT.getRandom(random);
        int i = enchantment.getMaximumLevel();
        ItemStack itemStack = EnchantedBookItem.forEnchantment(new InfoEnchantment(enchantment, i));
        int j = 2 + 3 * i;
        if (j > 64) j = 64;

        TradeOffer returnValue = cir.getReturnValue();
        TradeOffer tradeOffer = new TradeOffer(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), itemStack, 12, returnValue.getTraderExperience(), 0.2F);
        cir.setReturnValue(tradeOffer);
    }
}
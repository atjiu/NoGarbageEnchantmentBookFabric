package com.example.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(targets = "net/minecraft/village/TradeOffers$EnchantBookFactory")
public class NoGarbageEnchantmentBookMixin {

    @Inject(at = @At("RETURN"), method = "create", cancellable = true)
    private void init(Entity entity, Random random, CallbackInfoReturnable<TradeOffer> cir) {
        List<Enchantment> list = Registries.ENCHANTMENT.stream().filter(Enchantment::isAvailableForEnchantedBookOffer).toList();
        Enchantment enchantment = list.get(random.nextInt(list.size()));
        int i = enchantment.getMaxLevel();
        ItemStack itemStack = EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, i));
        int j = 2 + 3 * i;
        if (j > 64) j = 64;

        TradeOffer returnValue = cir.getReturnValue();
        TradeOffer tradeOffer = new TradeOffer(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), itemStack, 12, returnValue.getMerchantExperience(), 0.2F);
        cir.setReturnValue(tradeOffer);
    }
}
/*
 * MIT License
 *
 * Copyright © 2020 Bukkit Commons Studio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.maxgamer.quickshop.bukkit.util.matcher.item;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.NotNull;
import org.maxgamer.quickshop.bukkit.QuickShop;
import org.maxgamer.quickshop.bukkit.util.MiscUtils;

@RequiredArgsConstructor
public final class ItemMatcher {

    final boolean useBukkitMatcher;

    final QuickShop plugin;

    final ItemMatcherFilter[] globalFilter;

    public boolean matches(@NotNull final ItemStack original, @NotNull final ItemStack stack) {
        return this.matches(original, stack, this.globalFilter);
    }

    public boolean matches(@NotNull ItemStack original, @NotNull ItemStack stack, @NotNull final ItemMatcherFilter... filter) {

        //Object copy
        original = original.clone();
        original.setAmount(1);
        stack = stack.clone();
        stack.setAmount(1);
        if (this.useBukkitMatcher) {
            return original.isSimilar(stack);
        }
        if (!original.getType().equals(stack.getType())) { //Material check
            return false;
        }
        if (!MiscUtils.mapMatches(original.getEnchantments(), stack.getEnchantments())) {
            return false;
        }
        if (original.hasItemMeta() != stack.hasItemMeta()) { //ItemMeta check
            return false;
        }
        if (!original.hasItemMeta()) {
            return true;
        }
        final ItemMetaMatcher matcher = new ItemMetaMatcher(original.getItemMeta(), stack.getItemMeta(), Arrays.asList(filter));
        return true;
    }

}

@RequiredArgsConstructor
class ItemMetaMatcher {

    final ItemMeta original;

    final ItemMeta stack;

    final List<ItemMatcherFilter> filters;

    public boolean displayNameCheck() {
        if (!this.filters.contains(ItemMatcherFilter.DISPLAYNAME)) {
            return true;
        }
        if (this.original.hasDisplayName() != this.stack.hasDisplayName()) {
            return false;
        }
        if (this.original.hasDisplayName()) {
            return this.original.getDisplayName().equals(this.stack.getDisplayName());
        }
        return true;
    }

    public boolean loreCheck() {
        if (!this.filters.contains(ItemMatcherFilter.LORES)) {
            return true;
        }
        if (this.original.hasLore() != this.stack.hasLore()) {
            return false;
        }
        if (this.original.hasLore()) {
            //noinspection ConstantConditions # We already check at has Lore.
            return this.original.getLore().equals(this.stack.getLore());
        }
        return true;
    }

    public boolean enchantmentCheck() {
        if (!this.filters.contains(ItemMatcherFilter.ENCHANTMENTS)) {
            return true;
        }
        if (this.original.hasEnchants() != this.stack.hasEnchants()) {
            return false;
        }
        if (this.original.hasEnchants()) {
            return MiscUtils.mapMatches(this.original.getEnchants(), this.stack.getEnchants());
        }
        return true;
    }

    public boolean attributeModifiers() {
        if (!this.filters.contains(ItemMatcherFilter.ATTRIBUTEMODIFIERS)) {
            return true;
        }
        try {
            if (this.original.hasAttributeModifiers() != this.stack.hasAttributeModifiers()) {
                return false;
            }
            if (this.original.hasAttributeModifiers()) {
                //noinspection ConstantConditions
                return MiscUtils.mapMatches(this.original.getAttributeModifiers().asMap(), this.stack.getAttributeModifiers().asMap());
            }
            return true;
        } catch (final Throwable th) {
            return true; //minecraft version not support
        }
    }

    public boolean unbreakableCheck() {
        if (!this.filters.contains(ItemMatcherFilter.UNBREAKABLE)) {
            return true;
        }
        return this.original.isUnbreakable() == this.stack.isUnbreakable();
    }

    public boolean itemFlagCheck() {
        if (!this.filters.contains(ItemMatcherFilter.ITEMFLAGS)) {
            return true;
        }
        return this.original.getItemFlags().equals(this.stack.getItemFlags());
    }

    public boolean customModuleDataCheck() {
        if (!this.filters.contains(ItemMatcherFilter.CUSTOMMODELDATA)) {
            return true;
        }
        return this.original.getCustomModelData() == this.stack.getCustomModelData();
    }

}

@RequiredArgsConstructor
class subtypeMatcher {  //FIXME, NEED ADD TRY AND CATCH

    final List<ItemMatcherFilter> filters;

    final ItemMeta original;

    final ItemMeta stack;

    public boolean bannerMetaMatch() {
        if (!this.filters.contains(ItemMatcherFilter.META_BANNER)) {
            return true;
        }
        if (this.original instanceof BannerMeta != this.stack instanceof BannerMeta) {
            return false;
        }
        if (!(this.original instanceof BannerMeta)) {
            return true;
        }
        final BannerMeta originalBannerMeta = (BannerMeta) this.original;
        final BannerMeta stackBannerMeta = (BannerMeta) this.stack;
        if (originalBannerMeta.numberOfPatterns() != stackBannerMeta.numberOfPatterns()) {
            return false;
        }
        return originalBannerMeta.getPatterns().equals(stackBannerMeta.getPatterns());
    }

    public boolean blockDataMeta() {
        if (!this.filters.contains(ItemMatcherFilter.META_BLOCKDATA)) {
            return true;
        }
        if (this.original instanceof BlockDataMeta != this.stack instanceof BlockDataMeta) {
            return false;
        }
        if (!(this.original instanceof BlockDataMeta)) {
            return true;
        }
        final BlockDataMeta originalBlockDataMeta = (BlockDataMeta) this.original;
        final BlockDataMeta stackBlockDataMeta = (BlockDataMeta) this.stack;
        if (originalBlockDataMeta.hasBlockData() != stackBlockDataMeta.hasBlockData()) {
            return false;
        }
        return originalBlockDataMeta.equals(stackBlockDataMeta);
    }

    public boolean blockStateMeta() {
        if (!this.filters.contains(ItemMatcherFilter.META_BLOCKSTATE)) {
            return true;
        }
        if (this.original instanceof BlockStateMeta != this.stack instanceof BlockStateMeta) {
            return false;
        }
        if (!(this.original instanceof BlockStateMeta)) {
            return true;
        }
        final BlockStateMeta originalBlockDataMeta = (BlockStateMeta) this.original;
        final BlockStateMeta stackBlockDataMeta = (BlockStateMeta) this.stack;
        if (originalBlockDataMeta.hasBlockState() != stackBlockDataMeta.hasBlockState()) {
            return false;
        }
        return originalBlockDataMeta.equals(stackBlockDataMeta);
    }

    public boolean bookMeta() {
        if (!this.filters.contains(ItemMatcherFilter.META_BOOK)) {
            return true;
        }
        if (this.original instanceof BookMeta != this.stack instanceof BookMeta) {
            return false;
        }
        if (!(this.original instanceof BookMeta)) {
            return true;
        }
        final BookMeta originalBookDataMeta = (BookMeta) this.original;
        final BookMeta stackBookDataMeta = (BookMeta) this.stack;
        if (originalBookDataMeta.hasTitle() != stackBookDataMeta.hasTitle()) {
            return false;
        }
        if (originalBookDataMeta.hasTitle()) {
            //noinspection ConstantConditions
            if (!originalBookDataMeta.getTitle().equals(stackBookDataMeta.getTitle())) {
                return false;
            }
        }
        if (originalBookDataMeta.hasGeneration() != stackBookDataMeta.hasGeneration()) {
            return false;
        }
        if (originalBookDataMeta.hasGeneration()) {
            //noinspection ConstantConditions
            if (originalBookDataMeta.getGeneration().ordinal() != stackBookDataMeta.getGeneration().ordinal()) {
                return false;
            }
        }
        if (originalBookDataMeta.hasAuthor() != stackBookDataMeta.hasAuthor()) {
            return false;
        }
        if (originalBookDataMeta.hasAuthor()) {
            //noinspection ConstantConditions
            if (!originalBookDataMeta.getAuthor().equals(stackBookDataMeta.getAuthor())) {
                return false;
            }
        }
        if (originalBookDataMeta.getPageCount() != stackBookDataMeta.getPageCount()) {
            return false;
        }
        return originalBookDataMeta.getPages().equals(stackBookDataMeta.getPages());
    }

    public boolean crossbowCheck() {
        if (!this.filters.contains(ItemMatcherFilter.META_CROSSBOW)) {
            return true;
        }
        if (this.original instanceof CrossbowMeta != this.stack instanceof CrossbowMeta) {
            return false;
        }
        if (!(this.original instanceof CrossbowMeta)) {
            return true;
        }
        final CrossbowMeta originalCrossbowMeta = (CrossbowMeta) this.original;
        final CrossbowMeta stackCrossbowMeta = (CrossbowMeta) this.stack;
        return originalCrossbowMeta.getChargedProjectiles().equals(stackCrossbowMeta.getChargedProjectiles());
    }

    public boolean damageCheck() {
        if (!this.filters.contains(ItemMatcherFilter.META_DAMAGE)) {
            return true;
        }
        if (this.original instanceof Damageable != this.stack instanceof Damageable) {
            return false;
        }
        if (!(this.original instanceof Damageable)) {
            return true;
        }
        final Damageable originalDamageableMeta = (Damageable) this.original;
        final Damageable stackDamageableMeta = (Damageable) this.stack;
        return originalDamageableMeta.getDamage() == stackDamageableMeta.getDamage();
    }

    public boolean enchantmentStorageCheck() {
        if (!this.filters.contains(ItemMatcherFilter.META_ENCHANTMENTSTORAGE)) {
            return true;
        }
        if (this.original instanceof EnchantmentStorageMeta != this.stack instanceof EnchantmentStorageMeta) {
            return false;
        }
        if (!(this.original instanceof EnchantmentStorageMeta)) {
            return true;
        }
        final EnchantmentStorageMeta originalEnchantmentStorageMeta = (EnchantmentStorageMeta) this.original;
        final EnchantmentStorageMeta stackEnchantmentStorageMeta = (EnchantmentStorageMeta) this.stack;

        return MiscUtils.mapMatches(originalEnchantmentStorageMeta.getStoredEnchants(), stackEnchantmentStorageMeta.getStoredEnchants());
    }

    //    public boolean fireworkEffectCheck(){
    //        if((original instanceof FireworkEffectMeta) != (stack instanceof FireworkEffectMeta)){
    //            return false;
    //        }
    //        if(!(original instanceof FireworkEffectMeta)){
    //            return true;
    //        }
    //        FireworkEffectMeta originalFireworkEffectMeta = (FireworkEffectMeta) original;
    //        FireworkEffectMeta stackFireworkEffectMeta = (FireworkEffectMeta)stack;
    //        if(originalFireworkEffectMeta.hasEffect() != stackFireworkEffectMeta.hasEffect()){
    //            return false;
    //        }
    //        if(originalFireworkEffectMeta.hasEffect()){
    //            //noinspection ConstantConditions
    //            return originalFireworkEffectMeta.getEffect().equals(stackFireworkEffectMeta.getEffect());
    //        }
    //        return true;
    //    }
    public boolean fireworkCheck() {
        if (!this.filters.contains(ItemMatcherFilter.META_FIREWORK)) {
            return true;
        }
        if (this.original instanceof FireworkMeta != this.stack instanceof FireworkMeta) {
            return false;
        }
        if (!(this.original instanceof FireworkMeta)) {
            return true;
        }
        final FireworkMeta originalFireworkMeta = (FireworkMeta) this.original;
        final FireworkMeta stackFireworkMeta = (FireworkMeta) this.stack;
        if (originalFireworkMeta.getEffects().equals(stackFireworkMeta.getEffects())) {
            return false;
        }
        return originalFireworkMeta.getPower() == stackFireworkMeta.getPower();
    }

    public boolean knowledgeBookCheck() {
        if (!this.filters.contains(ItemMatcherFilter.META_KNOWLEDGEBOOK)) {
            return true;
        }
        if (this.original instanceof KnowledgeBookMeta != this.stack instanceof KnowledgeBookMeta) {
            return false;
        }
        if (!(this.original instanceof KnowledgeBookMeta)) {
            return true;
        }
        final KnowledgeBookMeta originalKnowledgeBookMeta = (KnowledgeBookMeta) this.original;
        final KnowledgeBookMeta stackKnowledgeBookMeta = (KnowledgeBookMeta) this.stack;
        return originalKnowledgeBookMeta.getRecipes().equals(stackKnowledgeBookMeta);
    }

    public boolean leatherArmorCheck() {
        if (!this.filters.contains(ItemMatcherFilter.META_LEATHERARMOR)) {
            return true;
        }
        if (this.original instanceof LeatherArmorMeta != this.stack instanceof LeatherArmorMeta) {
            return false;
        }
        if (!(this.original instanceof LeatherArmorMeta)) {
            return true;
        }
        final LeatherArmorMeta originalLeatherArmorMeta = (LeatherArmorMeta) this.original;
        final LeatherArmorMeta stackLeatherArmorMeta = (LeatherArmorMeta) this.stack;
        return originalLeatherArmorMeta.getColor().asRGB() == stackLeatherArmorMeta.getColor().asRGB();
    }

    public boolean mapCheck() {
        if (!this.filters.contains(ItemMatcherFilter.META_MAP)) {
            return true;
        }
        if (this.original instanceof MapMeta != this.stack instanceof MapMeta) {
            return false;
        }
        if (!(this.original instanceof MapMeta)) {
            return true;
        }
        final MapMeta originalMapMeta = (MapMeta) this.original;
        final MapMeta stackMapMeta = (MapMeta) this.stack;
        return originalMapMeta.equals(stackMapMeta);
    }

    public boolean potionCheck() {
        if (!this.filters.contains(ItemMatcherFilter.META_POTION)) {
            return true;
        }
        if (this.original instanceof PotionMeta != this.stack instanceof PotionMeta) {
            return false;
        }
        if (!(this.original instanceof PotionMeta)) {
            return true;
        }
        final PotionMeta originalPotionMeta = (PotionMeta) this.original;
        final PotionMeta stackPotionMeta = (PotionMeta) this.stack;
        return originalPotionMeta.equals(stackPotionMeta);
    }

    public boolean repairCheck() {
        if (!this.filters.contains(ItemMatcherFilter.META_REPAIR)) {
            return true;
        }
        if (this.original instanceof Repairable != this.stack instanceof Repairable) {
            return false;
        }
        if (!(this.original instanceof Repairable)) {
            return true;
        }
        return ((Repairable) this.original).getRepairCost() == ((Repairable) this.stack).getRepairCost();
    }

    public boolean skullCheck() {
        if (!this.filters.contains(ItemMatcherFilter.META_SKULL)) {
            return true;
        }
        if (this.original instanceof SkullMeta != this.stack instanceof SkullMeta) {
            return false;
        }
        if (!(this.original instanceof SkullMeta)) {
            return true;
        }
        final SkullMeta originalSkullMeta = (SkullMeta) this.original;
        final SkullMeta stackSkullMeta = (SkullMeta) this.stack;
        if (originalSkullMeta.getOwningPlayer() != stackSkullMeta.getOwningPlayer()) {
            return false;
        }
        if (originalSkullMeta.getOwningPlayer() == null) {
            return true;
        }
        //noinspection ConstantConditions
        return originalSkullMeta.getOwningPlayer().getUniqueId().equals(stackSkullMeta.getOwningPlayer().getUniqueId());
    }

    public boolean spawnEggCheck() {
        if (!this.filters.contains(ItemMatcherFilter.META_SPAWNEGG)) {
            return true;
        }
        if (this.original instanceof SpawnEggMeta != this.stack instanceof SpawnEggMeta) {
            return false;
        }
        if (!(this.original instanceof SpawnEggMeta)) {
            return true;
        }
        final SpawnEggMeta originalSpawnEggMeta = (SpawnEggMeta) this.original;
        final SpawnEggMeta stackSpawnEggMeta = (SpawnEggMeta) this.stack;

        return originalSpawnEggMeta.getSpawnedType().equals(stackSpawnEggMeta.getSpawnedType()); //For 1.12 and before.
    }

    public boolean suspiciousStewCheck() {
        if (!this.filters.contains(ItemMatcherFilter.META_SUSPICIOUSSTEW)) {
            return true;
        }
        if (this.original instanceof SuspiciousStewMeta != this.stack instanceof SuspiciousStewMeta) {
            return false;
        }
        if (!(this.original instanceof SuspiciousStewMeta)) {
            return true;
        }
        return ((SuspiciousStewMeta) this.original).getCustomEffects().equals(((SuspiciousStewMeta) this.stack).getCustomEffects());
    }

    public boolean tropicalFishBucketCheck() {
        if (!this.filters.contains(ItemMatcherFilter.META_REPAIR)) {
            return true;
        }
        if (this.original instanceof TropicalFishBucketMeta != this.stack instanceof TropicalFishBucketMeta) {
            return false;
        }
        if (!(this.original instanceof TropicalFishBucketMeta)) {
            return true;
        }
        return this.original.equals(this.stack);
    }

}

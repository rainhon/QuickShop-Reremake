package org.maxgamer.quickshop.shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
@AllArgsConstructor
@Data
public class ShopExtra {
    @NotNull String namespace;
    @NotNull Map<String, String> data;
}

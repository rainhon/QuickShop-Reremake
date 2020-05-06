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

package org.maxgamer.quickshop.bukkit.util;


import java.util.Map;
import org.jetbrains.annotations.NotNull;

public final class MiscUtils {

    private MiscUtils() {
    }

    /**
     * Convert strArray to String. E.g "Foo, Bar"
     *
     * @param strArray Target array
     * @return str
     */
    @NotNull
    public static String array2String(@NotNull final String[] strArray) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < strArray.length; i++) {
            builder.append(strArray[i]);
            if (i + 1 != strArray.length) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    /**
     * Match the map1 and map2
     *
     * @param map1 Map1
     * @param map2 Map2
     * @return Map1 match Map2
     */
    public static boolean mapMatches(@NotNull final Map<?, ?> map1, @NotNull final Map<?, ?> map2) {
        for (final Map.Entry<?, ?> entry : map1.entrySet()) {
            if (!map2.containsKey(entry.getKey())) {
                return false;
            }
            if (entry.getValue() != map2.get(entry.getKey())) {
                return false;
            }
        }
        return true;
    }

}

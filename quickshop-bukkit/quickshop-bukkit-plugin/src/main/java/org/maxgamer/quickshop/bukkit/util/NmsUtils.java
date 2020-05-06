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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public final class NmsUtils {

    private NmsUtils() {
    }

    /**
     * Get the net.minecraft.server version. E.g v1_15_R1
     *
     * @return The version of NMS, return "Unknown" when failed (usually is impossible)
     */
    @NotNull
    public static String getNMSVersion() {
        final String name = Bukkit.getServer().getClass().getPackage().getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }

    /**
     * Get MinecraftServer's TPS
     *
     * @return TPS (e.g 19.92)
     */
    public static double getTPS() {
        final Field tpsField;
        final Object serverInstance;
        try {
            final Optional<Class<?>> nmsClass = NmsUtils.getNMSClass("MinecraftServer");
            if (nmsClass.isPresent()) {
                serverInstance = nmsClass.get().getMethod("getServer").invoke(null);
                tpsField = serverInstance.getClass().getField("recentTps");
            } else {
                return 20.0;
            }
        } catch (final NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException |
            InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            return 20.0;
        }
        try {
            final double[] tps = (double[]) tpsField.get(serverInstance);
            return tps[0];
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
            return 20.0;
        }
    }

    /**
     * Get the MinecraftServer class
     *
     * @param className custom class name
     * @return The class
     * @throws ClassNotFoundException the exception if cannot found target class.
     */
    @NotNull
    public static Optional<Class<?>> getNMSClass(@NotNull final String className) throws ClassNotFoundException {
        final String name = Bukkit.getServer().getClass().getPackage().getName();
        final String version = name.substring(name.lastIndexOf('.') + 1);
        return Optional.of(Class.forName("net.minecraft.server." + version + '.' + className));
    }

    /**
     * Get Minecraft server version
     *
     * @return The version like 1.15.2 1.14.4
     */
    public static String getServerVersion() {
        try {
            final Field consoleField = Bukkit.getServer().getClass().getDeclaredField("console");
            consoleField.setAccessible(true); // protected
            final Object console = consoleField.get(Bukkit.getServer()); // dedicated server
            return String.valueOf(console.getClass().getSuperclass().getMethod("getVersion").invoke(console));
        } catch (final Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

}

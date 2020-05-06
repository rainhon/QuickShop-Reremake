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

package org.maxgamer.quickshop.bukkit.util.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;
import lombok.Cleanup;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

// FIXME: 14.02.2020 Change that class with more professional.
public final class PluginUpdaterSpigotMC implements PluginUpdater {

    @NotNull
    private final Plugin plugin;

    private final int project;

    @NotNull
    private final String checkURL;

    @NotNull
    @Getter
    private String latestVersion;

    public PluginUpdaterSpigotMC(@NotNull final Plugin plgn, final int prjctid) {
        this.plugin = plgn;
        this.latestVersion = plgn.getDescription().getVersion();
        this.project = prjctid;
        this.checkURL = "https://api.spigotmc.org/legacy/update.php?resource=" + prjctid;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    @NotNull
    public String getResourceURL() {
        return "https://www.spigotmc.org/resources/" + this.project;
    }

    @NotNull
    @Override
    public Optional<String> getChangeLog() {
        return Optional.empty();
    }

    @Override
    public boolean checkForUpdates() throws IOException {
        final URLConnection con = new URL(this.checkURL).openConnection();
        @Cleanup final BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        this.latestVersion = reader.readLine();
        if (this.latestVersion != null) {
            reader.close();
            return !this.latestVersion.isEmpty() &&
                !this.plugin.getDescription().getVersion().equals(this.latestVersion);
        } else {
            return false;
        }
    }

}
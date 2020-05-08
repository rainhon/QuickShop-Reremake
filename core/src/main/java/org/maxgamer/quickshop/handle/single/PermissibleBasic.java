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

package org.maxgamer.quickshop.api.handle.single;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.maxgamer.quickshop.api.single.Perm;
import org.maxgamer.quickshop.api.single.Permissible;

@RequiredArgsConstructor
public final class PermissibleBasic implements Permissible {

    @NotNull
    private final UUID uuid;

    @NotNull
    private final List<Perm> perms;

    @NotNull
    @Override
    public JsonObject serialize() {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.add("uuid", this.uuid.toString());

        final JsonArray permissions = new JsonArray();

        this.perms.forEach(perm -> permissions.add(perm.serialize()));
        jsonObject.add("permissions", permissions);

        return jsonObject;
    }

}

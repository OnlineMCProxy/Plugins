/*
 * MIT License
 *
 * Copyright (c) derrop and derklaro
 * Copyright (c) contributors
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
package com.github.derrop.proxy.plugins;

import com.github.derrop.proxy.api.command.CommandMap;
import com.github.derrop.proxy.api.plugin.PluginContainer;
import com.github.derrop.proxy.api.plugin.PluginState;
import com.github.derrop.proxy.api.plugin.annotation.Inject;
import com.github.derrop.proxy.api.plugin.annotation.Plugin;
import com.github.derrop.proxy.api.service.ServiceRegistry;
import com.github.derrop.proxy.plugins.replay.ReplaySystem;

import java.io.IOException;

@Plugin(
        id = "com.github.derrop.plugins.replay",
        displayName = "Replay",
        version = 1,
        website = "https://github.com/derrop",
        authors = "derrop"
)
public class ReplayPlugin {

    @Inject(state = PluginState.ENABLED)
    public void enable(PluginContainer container, ServiceRegistry registry) {
        try {
            registry.setProvider(container, ReplaySystem.class, new ReplaySystem(), false, true);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        registry.getProviderUnchecked(CommandMap.class).registerCommand(container, new CommandReplay(registry), "replay");
    }

}

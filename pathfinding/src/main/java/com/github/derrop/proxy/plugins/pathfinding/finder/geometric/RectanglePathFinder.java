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
package com.github.derrop.proxy.plugins.pathfinding.finder.geometric;

import com.github.derrop.proxy.api.block.BlockAccess;
import com.github.derrop.proxy.api.block.Material;
import com.github.derrop.proxy.api.location.Location;
import com.github.derrop.proxy.plugins.pathfinding.PathPoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;

public class RectanglePathFinder extends GeometricPathFinder {

    public Queue<PathPoint> findRectanglePath(@NotNull BlockAccess access, @Nullable Location start, @NotNull Location pos1, @NotNull Location pos2) {
        int lowerX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int upperX = Math.max(pos1.getBlockX(), pos2.getBlockX());

        int y = pos1.getBlockY();

        int lowerZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int upperZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        Collection<Location> result = new ArrayList<>();

        for (int x = lowerX; x < upperX; x++) {
            result.add(new Location(x, y, lowerZ));
            result.add(new Location(x, y, upperZ));
        }

        for (int z = lowerZ; z < upperZ; z++) {
            result.add(new Location(lowerX, y, z));
            result.add(new Location(upperX, y, z));
        }

        System.out.println(result);
        for (Location blockPos : result) {
            access.setMaterial(blockPos, Material.EMERALD_BLOCK);
            try {
                Thread.sleep(20);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }

        return super.sortByStartPoint(result, start);
    }

}

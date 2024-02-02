/*
 * This file is part of JMZEmu.
 *
 * JMZEmu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JMZEmu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.zdechov.sharpmz.jmzemu.module.device.z80.api;

import org.exbin.framework.Module;

/**
 * Z80 device interface.
 *
 * @author JMZEmu Project (https://sharpmz.zdechov.net/?jmzemu)
 */
public interface CpuDeviceModuleApi extends Module {

    /**
     * Register memory handler.
     *
     * @param module
     * @param memoryHandler
     */
    void registerMemoryHandler(Module module, MemoryHandler memoryHandler);

    /**
     * Register port handler.
     *
     * @param module
     * @param portHandler
     */
    void registerPortHandler(Module module, PortHandler portHandler);

    public interface MemoryHandler {

        /**
         * Read from memory.
         *
         * @param position position in memory
         * @param parentHandler parent handler
         * @return red byte
         */
        byte read(int position, MemoryHandler parentHandler);

        /**
         * Write to memory.
         *
         * @param position position in memory
         * @param value written byte
         * @param parentHandler parent handler
         */
        void write(int position, byte value, MemoryHandler parentHandler);
    }

    public interface PortHandler {

        int inpPort(int portNumber, PortHandler parentHandler);

        void outPort(int portNumber, int value, PortHandler parentHandler);
    }
}

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

import org.exbin.framework.api.XBApplicationModule;

/**
 * Z80 device interface.
 *
 * @version 0.2.0 2016/08/03
 * @author JMZEmu Project (http://sharpmz.zdechov.net/?jmzemu)
 */
public interface CpuDeviceModuleApi extends XBApplicationModule {

    /**
     * Register memory handler.
     *
     * @param module
     * @param memoryHandler
     */
    public void registerMemoryHandler(XBApplicationModule module, MemoryHandler memoryHandler);

    /**
     * Register port handler.
     *
     * @param module
     * @param portHandler
     */
    public void registerPortHandler(XBApplicationModule module, PortHandler portHandler);

    /**
     * Unregister handlers registred for other modules.
     *
     * @param module
     */
    public void unregisterModule(XBApplicationModule module);

    public interface MemoryHandler {

        /**
         * Read from memory.
         *
         * @param position position in memory
         * @param parentHandler parent handler
         * @return red byte
         */
        public byte read(int position, MemoryHandler parentHandler);

        /**
         * Write to memory.
         *
         * @param position position in memory
         * @param value written byte
         * @param parentHandler parent handler
         */
        public void write(int position, byte value, MemoryHandler parentHandler);
    }

    public interface PortHandler {

        public int inpPort(int portNumber, PortHandler parentHandler);

        public void outPort(int portNumber, int value, PortHandler parentHandler);
    }
}

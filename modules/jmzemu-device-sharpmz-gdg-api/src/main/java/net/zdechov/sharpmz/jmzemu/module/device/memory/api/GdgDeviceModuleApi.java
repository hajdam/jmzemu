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
package net.zdechov.sharpmz.jmzemu.module.device.memory.api;

/**
 * Z80 device interface.
 *
 * @version 0.1.1 2014/08/21
 * @author JMZEmu Project (http://sharpmz.zdechov.net/?jmzemu)
 */
public interface GdgDeviceModuleApi {

    /**
     * Read byte of memory from ROM.
     *
     * @param position position in memory
     * @return red byte
     */
    byte readRom(int position);
}

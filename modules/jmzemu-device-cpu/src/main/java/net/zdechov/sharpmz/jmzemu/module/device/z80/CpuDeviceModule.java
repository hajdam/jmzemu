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
package net.zdechov.sharpmz.jmzemu.module.device.z80;

import net.zdechov.sharpmz.jmzemu.module.device.z80.api.CpuDeviceModuleApi;
import org.exbin.framework.Module;

/**
 * Z80 CPU device module.
 *
 * @author JMZEmu Project (https://sharpmz.zdechov.net/?jmzemu)
 */
public class CpuDeviceModule implements CpuDeviceModuleApi, Module {

    private Z80 cpu;

    public CpuDeviceModule() {
    }

    public void init() {
        cpu = new Z80();
    }

    @Override
    public void registerMemoryHandler(Module module, MemoryHandler memoryHandler) {
        cpu.registerMemoryHandler(memoryHandler);
    }

    @Override
    public void registerPortHandler(Module module, PortHandler portHandler) {
        cpu.registerPortHandler(portHandler);
    }

    public void unregisterModule(Module module) {
        cpu.unregisterModule(module);
    }
}

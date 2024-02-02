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

package net.zdechov.sharpmz.jmzemu.module.device.memory;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import org.exbin.framework.Module;
import org.exbin.xbup.plugin.XBModuleHandler;

/**
 * Memory device module.
 *
 * @author JMZEmu Project (https://sharpmz.zdechov.net/?jmzemu)
 */
public class MemoryDeviceModule implements Module {

    private Memory memory;

//    private static ResourceMap resourceMap;

    private ActionListener helpActionLisneter;
    JMenuItem helpContextMenuItem;

    /** Constructor */
    public MemoryDeviceModule() {
    }

    public void init(XBModuleHandler moduleHandler) {
        helpContextMenuItem = new JMenuItem("Help Contents...");
        String path = ".";
        try {
            path = (new File(".")).getCanonicalPath();
        } catch (IOException ex) {
            Logger.getLogger(MemoryDeviceModule.class.getName()).log(Level.SEVERE, null, ex);
        }
//        moduleHandler.getMenuManagement().addMenuItem(helpContextMenuItem, BasicMenuType.HELP, MenuPositionMode.BEFORE_PANEL);

        memory = new Memory();
    }
}

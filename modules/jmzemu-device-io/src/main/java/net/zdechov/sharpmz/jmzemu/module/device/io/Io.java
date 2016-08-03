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

package net.zdechov.sharpmz.jmzemu.module.device.io;

import java.net.*;

/**
 * Sharp MZ IO.
 * 
 * This file was originally kindly provided by Zdeněk Adler, ZA Software, 2011
 *
 * @version 0.1.0 2012/06/21
 * @author JMZEmu Project (http://sharpmz.zdechov.net/?jmzemu)
 */

public class Io {

//  private JMZEmuApplet m;

//    public Io(JMZEmuApplet main) {
//        m = main;
//    }
//
//    public URL mzfURL;
//
//    public void out(int port, int b) {
//        switch (port & 0xFF) {
//            case 0x01:
//                try {
//                    m.memory.loadMZF(mzfURL.toString(), mzfURL.openStream(), m.cpu.HL(), 0x80);
//                } catch (Exception e) {
//                }
//                break;
//            case 0x02:
//                try {
//                    m.memory.loadMZFbody(mzfURL.toString(), mzfURL.openStream(), m.cpu.HL(), 0);
//                } catch (Exception e) {
//                }
//                break;
//            case 0xCC:
//                m.gdg.outCC(b);
//                break;
//            case 0xCD:
//                m.gdg.outCD(b);
//                break;
//            case 0xCE:
//                m.gdg.outCE(b);
//                break;
//            case 0xCF:
//                m.gdg.outCF(port >> 8, b);
//                break;
//            case 0xD0:
//                m.i8255.outD0(b);
//                break;
//            case 0xD2:
//                m.i8255.outD2(b);
//                break;
//            case 0xD3:
//                m.i8255.outD3(b);
//                break;
//            case 0xD4:
//                m.i8253.outD4(b);
//                break;
//            case 0xD5:
//                m.i8253.outD5(b);
//                break;
//            case 0xD6:
//                m.i8253.outD6(b);
//                break;
//            case 0xD7:
//                m.i8253.outD7(b);
//                break;
//            case 0xE0:
//                m.memory.aRom = false;
//                m.memory.cgRom = false;
//                break;
//            case 0xE1:
//                m.memory.eRom = false;
//                break;
//            case 0xE2:
//                m.memory.aRom = true;
//                break;
//            case 0xE3:
//                m.memory.eRom = true;
//                break;
//            case 0xE4:
//                m.memory.aRom = true;
//                m.memory.eRom = true;
//                m.memory.cgRom = (m.mz800mode) ? true : false;
//                m.memory.vram = (m.mz800mode) ? true : false;
//                break;
//            case 0xF0:
//                m.gdg.outF0(b);
//                break;
//            case 0xFC:
//                m.z80pio.outFC(b);
//        }
//    }
//
//    public int in(int port) {
//        switch (port & 0xFF) {
//            case 0xCE:
//                return m.gdg.inCE();
//            case 0xE0:
//                m.memory.cgRom = true;
//                m.memory.vram = true;
//                return 0xFF;
//            case 0xE1:
//                m.memory.cgRom = false;
//                m.memory.vram = false;
//                return 0xFF;
//            case 0xD0:
//                return m.i8255.inD0();
//            case 0xD1:
//                return m.i8255.inD1();
//            case 0xD2:
//                return (m.gdg.vBlnk << 7)/*((m.gdg.vblnk>35469) ? 0x80 : 0x00)*/ | ((m.cpu.clk > 1773447) ? 0x40 : 0x00) | 0x10; //0x10 = Motor ON - FIX IT!
//            case 0xD4:
//                return m.i8253.inD4();
//            case 0xD5:
//                return m.i8253.inD5();
//            case 0xD6:
//                return m.i8253.inD6();
//            case 0xFE:
//                return (0xF3 & (m.gdg.vBlnk << 5));
//            default:
//                return 0xFF;
//        }
//    }
}
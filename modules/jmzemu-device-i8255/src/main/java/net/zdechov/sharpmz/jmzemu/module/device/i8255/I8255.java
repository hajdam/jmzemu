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
package net.zdechov.sharpmz.jmzemu.module.device.i8255;

import java.awt.event.*;

/**
 * Sharp MZ I8255.
 *
 * This file was originally kindly provided by Zdeněk Adler, ZA Software, 2011
 *
 * @version 0.1.0 2012/06/21
 * @author JMZEmu Project (http://sharpmz.zdechov.net/?jmzemu)
 */
public class I8255 {
//
//    private JMZEmuApplet m;
//
//    public I8255(JMZEmuApplet main) {
//        m = main;
//    }
//
//    private int[] row = new int[10];
//
//    public int portC;
//
//    private int strobe;
//
//    public void reset() {
//        for (int i = 0; i < 10; ++i) {
//            row[i] = 0xFF;
//        }
//        portC = 0;
//    }
//
//    public int inD0() {
//        return (strobe > 9) ? 0xFF : strobe;
//    }
//
//    public int inD1() {
//        return ((strobe < 10 & strobe >= 0) ? row[strobe] : 0xff);
//    }
//
//    public void outD0(int value) {
//        strobe = value & 0x0f;
//    }
//
//    public void outD2(int b) {
//        portC = b;
//    }
//
//    public void outD3(int b) {
//        if ((b & 0x80) == 0) {
//            portC = ((b & 1) == 1) ? portC | (1 << ((b >> 1) & 7)) : portC & (~(1 << ((b >> 1) & 7)));
//        }
//    }
//
//    public void keyDetected(KeyEvent e) {
//        boolean hit;
//        int mx;	//matrix 0x.. row/column in hex
//        int c = e.getKeyCode();
//
//        if (e.getID() == KeyEvent.KEY_PRESSED) {
//            hit = true;
//        } else if (e.getID() == KeyEvent.KEY_RELEASED) {
//            hit = false;
//        } else {
//            return;
//        }
//
//        mx = 0xFF;
//        //System.out.println(c);
//        switch (c) {
//            // alphabet
//            case KeyEvent.VK_A:
//                mx = 0x47;
//                break;
//            case KeyEvent.VK_B:
//                mx = 0x46;
//                break;
//            case KeyEvent.VK_C:
//                mx = 0x45;
//                break;
//            case KeyEvent.VK_D:
//                mx = 0x44;
//                break;
//            case KeyEvent.VK_E:
//                mx = 0x43;
//                break;
//            case KeyEvent.VK_F:
//                mx = 0x42;
//                break;
//            case KeyEvent.VK_G:
//                mx = 0x41;
//                break;
//            case KeyEvent.VK_H:
//                mx = 0x40;
//                break;
//
//            case KeyEvent.VK_I:
//                mx = 0x37;
//                break;
//            case KeyEvent.VK_J:
//                mx = 0x36;
//                break;
//            case KeyEvent.VK_K:
//                mx = 0x35;
//                break;
//            case KeyEvent.VK_L:
//                mx = 0x34;
//                break;
//            case KeyEvent.VK_M:
//                mx = 0x33;
//                break;
//            case KeyEvent.VK_N:
//                mx = 0x32;
//                break;
//            case KeyEvent.VK_O:
//                mx = 0x31;
//                break;
//            case KeyEvent.VK_P:
//                mx = 0x30;
//                break;
//
//            case KeyEvent.VK_Q:
//                mx = 0x27;
//                break;
//            case KeyEvent.VK_R:
//                mx = 0x26;
//                break;
//            case KeyEvent.VK_S:
//                mx = 0x25;
//                break;
//            case KeyEvent.VK_T:
//                mx = 0x24;
//                break;
//            case KeyEvent.VK_U:
//                mx = 0x23;
//                break;
//            case KeyEvent.VK_V:
//                mx = 0x22;
//                break;
//            case KeyEvent.VK_W:
//                mx = 0x21;
//                break;
//            case KeyEvent.VK_X:
//                mx = 0x20;
//                break;
//
//            case KeyEvent.VK_Y:
//                mx = 0x17;
//                break;
//            case KeyEvent.VK_Z:
//                mx = 0x16;
//                break;
//            case KeyEvent.VK_F6:
//                mx = 0x15;
//                break; //@
//
//            //numbers
//            case KeyEvent.VK_1:
//                mx = 0x57;
//                break;
//            case KeyEvent.VK_2:
//                mx = 0x56;
//                break;
//            case KeyEvent.VK_3:
//                mx = 0x55;
//                break;
//            case KeyEvent.VK_4:
//                mx = 0x54;
//                break;
//            case KeyEvent.VK_5:
//                mx = 0x53;
//                break;
//            case KeyEvent.VK_6:
//                mx = 0x52;
//                break;
//            case KeyEvent.VK_7:
//                mx = 0x51;
//                break;
//            case KeyEvent.VK_8:
//                mx = 0x50;
//                break;
//
//            case KeyEvent.VK_0:
//                mx = 0x63;
//                break;
//            case KeyEvent.VK_9:
//                mx = 0x62;
//                break;
//
//            //specials
//            case KeyEvent.VK_INSERT:
//                mx = 0x77;
//                break; //INST
//            case KeyEvent.VK_BACK_SPACE:
//            case KeyEvent.VK_DELETE:
//                mx = 0x76;
//                break; //DEL
//
//            case 93:
//                mx = 0x13;
//                break; // ]
//            case 91:
//                mx = 0x14;
//                break; // [
//            case 222:
//                mx = 0x01;
//                break; // :
//
//            case KeyEvent.VK_UP:
//                mx = 0x75;
//                break; //UP
//            case KeyEvent.VK_DOWN:
//                mx = 0x74;
//                break; //DOWN
//            case KeyEvent.VK_LEFT:
//                mx = 0x72;
//                break; //LEFT
//            case KeyEvent.VK_RIGHT:
//                mx = 0x73;
//                break; //RIGHT
//            case KeyEvent.VK_SLASH:
//                mx = 0x70;
//                break; //normal SLASH
//
//            case KeyEvent.VK_F1:
//                mx = 0x97;
//                break; //F1
//            case KeyEvent.VK_F2:
//                mx = 0x96;
//                break; //F2
//            case KeyEvent.VK_F3:
//                mx = 0x95;
//                break;
//            case KeyEvent.VK_F4:
//                mx = 0x94;
//                break;
//            case KeyEvent.VK_F5:
//                mx = 0x93;
//                break;
//            case KeyEvent.VK_F12:
//                m.reset();
//                break; //machine reset
//
//            case KeyEvent.VK_F11:
//                System.out.println(Integer.toHexString(m.cpu.PC()));
//                break; //Dispatch
//
//            case KeyEvent.VK_SHIFT:
//                mx = 0x80;
//                break;
//            case KeyEvent.VK_CONTROL:
//                mx = 0x86;
//                break;
//            case KeyEvent.VK_ESCAPE:
//            case KeyEvent.VK_END:
//            case KeyEvent.VK_PAUSE:
//                mx = 0x87;
//                break;
//
//            //other
//            case KeyEvent.VK_F7:
//                mx = 0x67;
//                break; //BACKSLASH
//            case 61:
//                mx = 0x66;
//                break; //vlnovka
//            case KeyEvent.VK_MINUS:
//                mx = 0x65;
//                break;
//            case KeyEvent.VK_SPACE:
//                mx = 0x64;
//                break;
//            case KeyEvent.VK_COMMA:
//                mx = 0x61;
//                break;
//            case KeyEvent.VK_PERIOD:
//                mx = 0x60;
//                break;
//
//            case 192:
//                mx = 0x07;
//                break;	//BLANK
//            case KeyEvent.VK_CAPS_LOCK:
//                mx = 0x06;
//                break;	//GRAPH
//            case KeyEvent.VK_F9:
//                mx = 0x05;
//                break;	//LIBRA (POUND)
//            case KeyEvent.VK_BACK_SLASH:
//                mx = 0x04;
//                break;	//ALPHA
//            case KeyEvent.VK_TAB:
//                mx = 0x03;
//                break;
//            case KeyEvent.VK_SEMICOLON:
//                mx = 0x02;
//                break;
//            //case KeyEvent.VK_COLON: mx=0x01; break;
//            case KeyEvent.VK_ENTER:
//                mx = 0x00;
//                break;
//            case KeyEvent.VK_F8:
//                mx = 0x71;
//                break; //OTAZNIK
//
//        }
//
//        if (mx == 0xFF) {
//            return;
//        }
//
//        int mask = 0;
//        switch (mx & 7) {
//            case 0:
//                mask = 0x01;
//                break;
//            case 1:
//                mask = 0x02;
//                break;
//            case 2:
//                mask = 0x04;
//                break;
//            case 3:
//                mask = 0x08;
//                break;
//            case 4:
//                mask = 0x10;
//                break;
//            case 5:
//                mask = 0x20;
//                break;
//            case 6:
//                mask = 0x40;
//                break;
//            case 7:
//                mask = 0x80;
//                break;
//        }
//
//        if (hit) {
//            row[mx >>> 4] &= (~mask) & 0xFF;
//        } else {
//            row[mx >>> 4] |= mask;
//        }
//    }
}

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

package net.zdechov.sharpmz.jmzemu.module.device.i8253;

/**
 * Sharp MZ I8253.
 * 
 * This file was originally kindly provided by Zdeněk Adler, ZA Software, 2011
 *
 * @version 0.1.0 2012/06/21
 * @author JMZEmu Project (http://sharpmz.zdechov.net/?jmzemu)
 */

public class I8253 {

//  private JMZEmuApplet m;
//
//  public I8253( JMZEmuApplet main ) {
//    m = main;
//  }
//
//  public Counter ctc0 = new Counter();
//  public Counter ctc1 = new Counter();
//  public Counter ctc2 = new Counter();
//
//  public boolean ctc0gate;
//
//  private int tempC0; //Temp state for next counter decrement
//
//  public void runC0(int tStates) {
//    tStates *= 10;
//    tStates += tempC0;
//    int decval = tStates/32;
//    tempC0 = tStates % 32;
//    ctc0.cnt -= decval;
//    if (ctc0.cnt<0) {
//      ctc0.cnt += (ctc0.mode==0) ? 0xFFFF : ctc0.value;
//      if ((m.z80pio.mask & 0x10)==0) {
//        if (m.z80pio.intenbl==0) {
//          m.cpu.interrupt(m.z80pio.intAddr);
//          //System.out.println("INT from CTC0");
//        }
//      }
//    }
//  }
//
//  public void runC1() {
//    if (--ctc1.cnt < 0) {
//      ctc1.cnt = ctc1.value;
//      if (--ctc2.cnt < 0) {
//        ctc2.cnt = ctc2.value;
//        if ((m.i8255.portC&4)==4) m.cpu.interrupt(m.z80pio.intAddr);
//      }
//    }
//  }
//
//  public void reset() {
//    ctc0.reset();
//    ctc1.reset();
//    ctc2.reset();
//    tempC0 = 0;
//  }
//
//  public void outD4(int b) {
//    ctc0.outcnt(b);
//  }
//
//  public void outD5(int b) {
//    ctc1.outcnt(b);
//  }
//
//  public void outD6(int b) {
//    ctc2.outcnt(b);
//    //m.gdg.hblnk = 0;  // FIX it!
//  }
//
//  public void outD7(int b) {
//    if (((b>>4) & 3) == 0) {
//      switch (b>>6) {
//        case 0: if (ctc0.latched == 0) {
//          ctc0.latch = ctc0.cnt;
//          ctc0.latched = 2;
//          break;
//        }
//        case 1: if (ctc1.latched == 0) {
//          ctc1.latch = ctc1.cnt;
//          ctc1.latched = 2;
//          break;
//        }
//        case 2: if (ctc2.latched == 0) {
//          ctc2.latch = ctc2.cnt;
//          ctc2.latched = 2;
//          break;
//        }
//      }
//    }
//    else {
//      switch (b>>6) {
//        case 0: {
//          ctc0.fl = (b >> 4) & 3;
//          ctc0.mode = (b >> 1) & 3;
//          ctc0.bcd = b & 1;
//          ctc0.latched = 0;
//          ctc0.hilo = false;
//        }
//        case 1: {
//          ctc1.fl = (b >> 4) & 3;
//          ctc1.mode = (b >> 1) & 3;
//          ctc1.bcd = b & 1;
//          ctc1.latched = 0;
//          ctc1.hilo = false;
//        }
//        case 2: {
//          ctc2.fl = (b >> 4) & 3;
//          ctc2.mode = (b >> 1) & 3;
//          ctc2.bcd = b & 1;
//          ctc2.latched = 0;
//          ctc2.hilo = false;
//        }
//      }
//    }
//  }
//
//  public int inD4() {
//    return ctc0.incnt();
//  }
//
//  public int inD5() {
//    return ctc1.incnt();
//  }
//
//  public int inD6() {
//    return ctc2.incnt();
//  }
}
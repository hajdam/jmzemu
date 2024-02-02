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

package net.zdechov.sharpmz.jmzemu.module.device.z80pio;

/**
 * Z-80 PIO.
 *
 * This file was originally kindly provided by Zdeněk Adler, ZA Software, 2011
 *
 * @author JMZEmu Project (https://sharpmz.zdechov.net/?jmzemu)
 */

public class Z80pio {

  public Z80pio() {
  }

  public int intAddr;
  public int next;
  public int mask;
  public int intenbl;

  public void reset() {
    intenbl = 0xFF;
    next = 0;
    intAddr = 0;
    mask = 0xFF;
  }

  public void outFC(int b) {
    switch (next) {
      case 0: {
        if ((b & 0xF)==7) {
          if (((b >> 4) & 1)==1) next = 1;
          if ((b & 0x80)==0x80) intenbl = 0; else intenbl = 0xFF;
        }
        if ((b & 1)==0) intAddr = b;
        if (b==0x03) intenbl=0xFF;
        if (b==0x83) intenbl=0;
      }
        break;
      case 1: {
        mask = b;
        next = 0;
      }
        break;
    }
  }
}

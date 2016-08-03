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

import java.io.BufferedInputStream;
import java.io.InputStream;
import net.zdechov.sharpmz.jmzemu.module.device.memory.api.RomMemory;

/**
 * Sharp MZ Memory.
 * 
 * This file was originally kindly provided by Zdeněk Adler, ZA Software, 2011
 *
 * @version 0.1.0 2014/08/21
 * @author JMZEmu Project (http://sharpmz.zdechov.net/?jmzemu)
 */

public class Memory implements RomMemory {

  public Memory() {
  }

  /** RAM & ROM. Note: array rom is also used in 700 mode as VRAM ! */
  public byte ram[] = new byte[65536];
  public byte rom[] = new byte[65536];

  /** Memory mapping switches */
  public boolean aRom = true;
  public boolean eRom = true;
  public boolean cgRom = false;
  public boolean vram = false;

  private int bl=0;

  public void reset() {
    aRom = true;
    eRom = true;
    cgRom = false;
    vram = false;
  }

  /* TODO
  public int readMem700 (int addr) {
    switch (addr >> 12) {
      case 0: return aRom ? rom[addr] : ram[addr];
      case 1: return cgRom ? rom[addr] : ram[addr];
      case 0xC: return vram ? rom[addr] : ram[addr];
      case 0xE: if (eRom) switch (addr) {
                  case 0xE000:
                  case 0xE001:
                  case 0xE002:
                  case 0xE003:
                  case 0xE004:
                  case 0xE005:
                  case 0xE006:
                  case 0xE007: return m.io.in(addr-0xDF30);
                  case 0xE008: return (m.gdg.msync>0xCBC0) ? 0x7F : 0x7E;
                  default: return rom[addr];
                } else return ram[addr];
      case 0xD:
      case 0xF: return eRom ? rom[addr] : ram[addr];
      default: return ram[addr];
    }
  }

  public void writeMem700(int addr, byte b) {
    switch (addr >> 12) {
      case 0: if (!aRom) ram[addr]=b;
        break;
      case 1: if (!cgRom) ram[addr]=b;
        break;
      case 0xC: if (vram) {rom[addr]=b; m.gdg.cgUpdate=true;} else ram[addr]=b;
        break;
      case 0xD: if (eRom) {
                  rom[addr]=b;
                  if (addr<0xD3E8) m.gdg.draw700(addr, b);
                  if ((addr>0xD7FF)&(addr<0xDBE8)) m.gdg.draw700(addr-0x800,rom[addr-0x800]);
                }
                else ram[addr]=b;
      case 0xE: if (eRom) switch (addr) {
                  case 0xE000:
                  case 0xE001:
                  case 0xE002:
                  case 0xE003:
                  case 0xE004:
                  case 0xE005:
                  case 0xE006:
                  case 0xE007: m.io.out(addr-0xDF30,b);
                    break;
                  case 0xE008:
                    break;
                } else ram[addr]=b;
      case 0xF: if (!eRom) ram[addr]=b;
        break;
      default: ram[addr]=b;
        break;
    }
  }

  public int readMem800(int addr) {
    switch (addr>>12) {
      case 0: return aRom ? rom[addr] : ram[addr];
      case 1: return cgRom ? rom[addr] : ram[addr];
      case 8:
      case 9: return vram ? m.gdg.read800(addr) : ram[addr];
      case 0xA:
      case 0xB: return (vram&(m.gdg.dmd>3)) ? m.gdg.read800(addr) : ram[addr];
      case 0xE:
      case 0xF: return eRom ? rom[addr] : ram[addr];
      default: return ram[addr];
    }
  }

  public void writeMem800(int addr, byte b) {
    switch (addr>>12) {
      case 0: if (!aRom) ram[addr]=b;
        break;
      case 1: if (!cgRom) ram[addr]=b;
        break;
      case 8:
      case 9: if (vram) m.gdg.draw800(addr,b); else ram[addr]=b;
        break;
      case 0xA:
      case 0xB: if (vram&(m.gdg.dmd>3)) m.gdg.draw800(addr,b); else ram[addr]=b;
        break;
      case 0xE:
      case 0xF: if (!eRom) ram[addr]=b;
        break;
      default: ram[addr]=b;
    }
  } */

  public void loadROM( String name, InputStream is, int addr, int length ) throws Exception {
    System.out.println("Loading system ROM "+name);
    readBytes( is, rom, addr, length );
  }

  public void loadMZF( String name, InputStream is, int addr, int length ) throws Exception {
    System.out.println("Loading MZF header "+name);
    readBytes( is, ram, addr, length );
  }

  public void loadMZFbody( String name, InputStream is, int addr, int length ) throws Exception {
    byte buf[] = new byte[128];
    readBytes( is, buf, 0, 0x80 );
    int leng = (buf[0x13]<<8)+buf[0x12];

    System.out.println("Loading body, length:"+Integer.toHexString(leng)+" addr:"+Integer.toHexString(addr));
    if(readBytes( is, ram, addr, leng )==leng) System.out.println("Finished OK");
    else System.out.println("Error with loading body!!!");
  }

  private int readBytes( InputStream is, byte a[], int off, int n ) throws Exception {
    try {
      BufferedInputStream bis = new BufferedInputStream( is, n );
      for (int i = 0; i < n; i++) {
      a[i+off] = (byte) bis.read();
    }
      return n;
    }
    catch ( Exception e ) {
      System.err.println( e );
      e.printStackTrace();
      throw e;
    }
  }

    @Override
    public byte readRom(int position) {
        return rom[position];
    }
}
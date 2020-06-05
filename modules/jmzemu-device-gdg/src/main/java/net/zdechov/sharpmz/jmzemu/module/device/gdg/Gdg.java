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
package net.zdechov.sharpmz.jmzemu.module.device.gdg;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import net.zdechov.sharpmz.jmzemu.module.device.memory.api.RomMemory;

/**
 * Sharp MZ-800 GDG.
 *
 * This file was originally kindly provided by Zdeněk Adler, ZA Software, 2011
 *
 * @version 0.2.0 2016/08/03
 * @author JMZEmu Project (http://sharpmz.zdechov.net/?jmzemu)
 */
public class Gdg {
    
    private static final int DISPLAY_PAL_RESOLUTION_X = 460;
    private static final int DISPLAY_PAL_RESOLUTION_Y = 287;

    private RomMemory romMemory;

    public BufferedImage image = new BufferedImage(DISPLAY_PAL_RESOLUTION_X, DISPLAY_PAL_RESOLUTION_Y, BufferedImage.TYPE_INT_RGB);
    public Graphics graphics = image.getGraphics();
    
    private final int[] mz700cols = {
        0x000000,
        0x0000FF,
        0xFF0000,
        0xFF00FF,
        0x00FF00,
        0x00FFFF,
        0xFFFF00,
        0xFFFFFF};

    private final int[] mz800cols = {
        0x000000,
        0x4040AC,
        0xD03400,
        0xB40C8C,
        0x406C00,
        0x24CCFF,
        0xE8D430,
        0xD0D0D0,
        0x848484,
        0x008CE8,
        0xFF0000,
        0xF054CC,
        0x54FF54,
        0x80FFFF,
        0xFFFF28,
        0xFFFFFF};

    private final int[] validRovA = {3, 3, 15, 0, 1, 1, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private final int[] validRovB = {12, 12, 15, 0, 4, 4, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private boolean mz800mode;
    public int msync;
    public boolean cgUpdate;
    public int dmd;
    public int rfReg;
    public int wfReg;
    public int writeRov;
    public int readRov;
    private int palBlk;
    public int paleta[] = new int[16];
    public int plt[] = new int[16];

    /**
     * Border color
     */
    public int border;
    /**
     * Do scroll after next SSA/SEA or not?
     */
    public boolean scrollPos;
    /**
     * Scroll start register
     */
    public int ssa;
    public int ossa;
    /**
     * Scroll end register
     */
    public int sea;
    public int osea;
    /**
     * Scroll offset register
     */
    public int sof;
    public int osof;
    public int osofL;
    public int osofH;
    /**
     * Scroll width register
     */
    public int sw;

    /**
     * array for screen, max 640x200
     */
    public int screen[] = new int[128000];
    public int finalScreen[] = new int[460 * 287];

    public int vram[][] = new int[4][0x4000];
    public int scrollBuffer[][] = new int[4][0x4000];

    /**
     * Set GDG defaults
     */
    public void reset() {
        sof = 0;
        sw = 0x7D;
        sea = 0x7D;
        ssa = 0;
        osof = 0;
        osea = 0;
        ossa = 0;
        osofH = 0;
        osofL = 0;
        scrollPos = false;
        border = 0;
        resetGdgCycle();
        for (int i = 0; i < 128000; i++) {
            screen[i] = 0;
        }
    }

    public int inCE() {
        curLnStat = gdgCycle % 227;
        hSync = (curLnStat < 43) ? 0 : 1;
        hBlnk = ((curLnStat > 56) & (curLnStat < 171)) ? 1 : 0;
        return (((msync > 0xCBC0) ? 1 : 0) | (vSync << 4) | (hSync << 5) | (vBlnk << 6) | (hBlnk << 7));
    }

    /**
     * Set Write format register
     */
    public void outCC(int b) {
        wfReg = b;
        writeRov = ((wfReg & 0x10) == 0) ? validRovA[dmd] : validRovB[dmd];
    }

    /**
     * Set Read format register
     */
    public void outCD(int b) {
        rfReg = b;
        readRov = ((rfReg & 0x10) == 0) ? validRovA[dmd] : validRovB[dmd];
    }

    /**
     * Set display mode (DMD) register
     */
    public void outCE(int b) {
        if (dmd != (b & 0xF)) {
            resetPal();
        }
        dmd = b & 0xF;
        switch (b & 0xC) {
            case 0:
                if (!mz800mode) {
                    mz800mode = true;
                }
                redraw800(0, 0x1FFF);
                break;
            case 4:
                System.out.println("Hi-res 640x200 mode still not supported");
                break;
            case 8:
                if (mz800mode) {
                    mz800mode = false;
                    redraw700();
                }
                break;
            default:
                System.out.println("Invalid DMD selection");
                break;
        }
        outCC(wfReg);
        outCD(rfReg);
    }

    /**
     * Execute scroll
     */
    public void doScroll() {
        scrollPos = false;
        osof = (osofH << 8) | osofL;
        if ((sof != osof) & (sof < 0x3E9)) {
            int sStart;
            int sEnd;
            int _320 = (dmd < 4) ? 40 : 80;

            if (sea > ssa) {
                sStart = ((ssa * _320) << 3) / 5;
                sEnd = ((sea * _320) << 3) / 5;
            } else {
                sStart = ((sea * _320) << 3) / 5;
                sEnd = ((ssa * _320) << 3) / 5;
            }

            if (sof > osof) {
                for (int rov = 0; rov < 4; rov++) {
                    movevb(sStart, sEnd - (((sof - osof) * _320) / 5), ((sof - osof) * _320) / 5, rov);
                    movevb(sStart + (((sof - osof) * _320) / 5), sStart, (sEnd - sStart) - (((sof - osof) * _320) / 5), rov);
                }
            } else {
                for (int rov = 0; rov < 4; rov++) {
                    movevb(sEnd - (((osof - sof) * _320) / 5), sStart, ((osof - sof) * _320) / 5, rov);
                    movevb(sStart, sStart + (((osof - sof) * _320) / 5), (sEnd - sStart) - (((osof - sof) * 320) / 5), rov);
                }
            }
            for (int rov = 0; rov < 4; rov++) {
                movebv(sStart, sStart, sEnd - sStart, rov);
            }
            redraw800(sStart, sEnd);
        }
    }

    /**
     * Set scroll registers and border color
     */
    public void outCF(int port, int b) {
        switch (port) {
            case 1: {
                osofL = sof & 0xFF;
                sof = (sof & 0xFF00) | (b & 0xFF);
                if (scrollPos) {
                    doScroll();
                } else {
                    scrollPos = true;
                }
            }
            break;
            case 2: {
                osofH = sof >> 8;
                sof = (sof & 0xFF) | ((b & 0xFF) << 8);
                if (scrollPos) {
                    doScroll();
                } else {
                    scrollPos = true;
                }
            }
            break;
            case 3:
                sw = b & 0x7F;
                break;
            case 4: {
                ossa = ssa;
                ssa = b & 0x7F;
                if (ossa != ssa) {
                    osof = 0;
                    sof = 0;
                    scrollPos = false;
                }
            }
            break;
            case 5: {
                osea = sea;
                sea = b & 0x7F;
                if (osea != sea) {
                    osof = 0;
                    sof = 0;
                    scrollPos = false;
                }
            }
            break;
            case 6:
                border = b & 0xF;
                break;
        }
    }

    /**
     * Set palette register
     */
    public void outF0(int b) {
        if (dmd == 2) {
            if ((b & 0x40) == 0x40) {
                palBlk = b & 3;
            } else {
                paleta[(b >> 4) & 3] = b & 15;
            }
            for (int i = 0; i < 16; i++) {
                plt[i] = ((i >> 2) == palBlk) ? paleta[i & 3] : i;
            }
        } else {
            plt[b >> 4] = b & 0xF;
        } //redraw800(0,0x1FFF); //DELETE IT after scanline emulation !!!!!!!
    }

    private void resetPal() {
        palBlk = 0;
        for (int i = 0; i < 16; i++) {
            plt[i] = i;
            paleta[i] = i;
        }
    }

    /**
     * Draws byte to screen in MZ-700 mode
     */
    public void draw700(int addr, int b) {
        int adr = addr - 0xD000;
        addr += 0x800;
        int y = adr / 40;
        int x = (adr - (y << 3) - (y << 5)) << 3;
        y <<= 3;
        int fgCol = romMemory.readRom(addr) & 7;
        int bkCol = (romMemory.readRom(addr) >> 4) & 7;

        b <<= 3;
        b += ((romMemory.readRom(addr) & 0x80) << 4) + 0xC000;

        for (int yy = 0; yy < 8; yy++) {
            int chr = romMemory.readRom(b + yy);
            for (int xx = 0; xx < 8; xx++) {
                screen[x + xx + (y * 320)] = ((chr >> xx) & 1) == 1 ? bkCol : fgCol;
            }
            y++;
        }
    }

    /**
     * Refresh screen
     */
    public void redraw700() {
        for (int addr = 0xD000; addr <= 0xD3E7; addr++) {
            draw700(addr, romMemory.readRom(addr));
        }
        cgUpdate = false;
    }

    public void draw800(int addr, int b) {
        addr -= 0x8000;
        switch (wfReg >> 5) {
            case 0:
                for (int rov = 0; rov < 4; rov++) {
                    if (((wfReg >> rov) & 1) == 1) {
                        vram[rov][addr] = b;
                    }
                }
                break;
            case 1:
                for (int rov = 0; rov < 4; rov++) {
                    if (((wfReg >> rov) & 1) == 1) {
                        vram[rov][addr] = vram[rov][addr] ^ b;
                    }
                }
                break;
            case 2:
                for (int rov = 0; rov < 4; rov++) {
                    if (((wfReg >> rov) & 1) == 1) {
                        vram[rov][addr] = vram[rov][addr] | b;
                    }
                }
                break;
            case 3:
                for (int rov = 0; rov < 4; rov++) {
                    if (((wfReg >> rov) & 1) == 1) {
                        vram[rov][addr] = vram[rov][addr] & (~b & 0xFF);
                    }
                }
                break;
            case 4:
            case 5:
                for (int rov = 0; rov < 4; rov++) {
                    if (((writeRov >> rov) & 1) == 1) {
                        if (((wfReg >> rov) & 1) == 1) {
                            vram[rov][addr] = b;
                        } else {
                            vram[rov][addr] = 0;
                        }
                    }
                }
                break;
            case 6:
            case 7:
                for (int rov = 0; rov < 4; rov++) {
                    if (((writeRov >> rov) & 1) == 1) {
                        if (((wfReg >> rov) & 1) == 1) {
                            vram[rov][addr] = vram[rov][addr] | b;
                        } else {
                            vram[rov][addr] = vram[rov][addr] & (~b & 0xFF);
                        }
                    }
                }
                break;
        }
        redraw800(addr, addr);
    }

    public int read800(int addr) {
        addr -= 0x8000;
        switch (rfReg & 0x80) {
            case 0: {
                int val = 0xFF;
                for (int rov = 0; rov < 4; rov++) {
                    if (((rfReg >> rov) & 1) == 1) {
                        val &= vram[rov][addr];
                    }
                }
                return val;
            }
            case 0x80: {
                int val = 0;
                for (int i = 0; i < 8; i++) {
                    if (((((vram[0][addr] >> i) & 1)
                            | (((vram[1][addr] >> i) & 1) << 1)
                            | (((vram[2][addr] >> i) & 1) << 2)
                            | (((vram[3][addr] >> i) & 1) << 3)) & readRov) == (rfReg & readRov)) {
                        val |= 1 << i;
                    }
                }
                return val;
            }
            default:
                return 0;
        }
    }

    public void redraw800(int fromAddr, int toAddr) {
        for (int addr = fromAddr; addr <= toAddr; addr++) {
            int y = (addr << 3) / 320;
            int x = (addr << 3) % 320;
            if (addr < 8000) {
                switch (dmd) {
                    case 0:
                        for (int i = 0; i < 8; i++) {
                            screen[x + i + (y * 320)] = ((vram[0][addr] >> i) & 1) | (((vram[1][addr] >> i) & 1) << 1);
                        }
                        break;
                    case 1:
                        for (int i = 0; i < 8; i++) {
                            screen[x + i + (y * 320)] = ((vram[2][addr] >> i) & 1) | (((vram[3][addr] >> i) & 1) << 1);
                        }
                        break;
                    case 2:
                        for (int i = 0; i < 8; i++) {
                            screen[x + i + (y * 320)] = (((vram[0][addr] >> i) & 1) | (((vram[1][addr] >> i) & 1) << 1) | (((vram[2][addr] >> i) & 1) << 2) | (((vram[3][addr] >> i) & 1) << 3));
                        }
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                }
            }
        }
    }

    private void movevb(int fromIndex, int toIndex, int count, int rov) {
        for (int i = 0; i < count; i++) {
            scrollBuffer[rov][toIndex + i] = vram[rov][fromIndex + i];
        }
    }

    private void movebv(int fromIndex, int toIndex, int count, int rov) {
        for (int i = 0; i < count; i++) {
            vram[rov][toIndex + i] = scrollBuffer[rov][fromIndex + i];
        }
    }

    public int gdgCycle;
    private int t;
    public int vSync;
    public int vBlnk;
    public int hBlnk;
    public int hSync;
    public int lineCounter;
    public int curLnStat;
    public boolean pixelStat;
    public int pixelCounter;
    public boolean drawFinished;
    public boolean currentLineNeeded;
    public int fsPos;
    public int asPos;
    public boolean screenRedrawNeeded;

    static final int MZ_SCR_WIDTH = 460;
    static final int MZ_UP_BORDER_HEIGHT = 45;

    public void resetGdgCycle() {
        gdgCycle = 0;
        vSync = 1;
        hSync = 0;
        vBlnk = 0;
        hBlnk = 0;
        lineCounter = 0;
        pixelStat = false;
        pixelCounter = 0;
        if (screenRedrawNeeded) {
            drawFinished = true;
            screenRedrawNeeded = false;
        }
        curLnStat = 0;
        currentLineNeeded = false;
        fsPos = 0;
        asPos = 0;
    }

    public void setBorderPixel() {
        int n = (pixelStat) ? 2 : 3; //Draw 2 or 3 pixels? (2,5 pix on Tstate)
        pixelStat = !pixelStat;
        for (int pixcnt = 0; pixcnt < n; n--) {
            int col = mz800cols[border];
            if (!currentLineNeeded) {
                if (finalScreen[fsPos] != col) {
                    currentLineNeeded = true;
                }
            }
            finalScreen[fsPos++] = col;
            pixelCounter++;

            if (pixelCounter == MZ_SCR_WIDTH) {
                if (currentLineNeeded) {
                    image.setRGB(0, lineCounter, MZ_SCR_WIDTH, 1, finalScreen, lineCounter * MZ_SCR_WIDTH, MZ_SCR_WIDTH);
                    currentLineNeeded = false;
                    screenRedrawNeeded = true;
                }
                lineCounter++;
            }

        }
    }

    public void setScreenPixel() {
        int n = (pixelStat) ? 2 : 3; //Draw 2 or 3 pixels? (2,5 pix on Tstate)
        pixelStat = !pixelStat;
        for (int pixcnt = 0; pixcnt < n; n--) {
            int col = (mz800mode) ? mz800cols[plt[screen[asPos++]]] : mz700cols[screen[asPos++]];
            if (!currentLineNeeded) {
                if (finalScreen[fsPos] != col) {
                    currentLineNeeded = true;
                }
            }
            finalScreen[fsPos++] = col;
            pixelCounter++;
        }
    }

    public void processGdg(int tStates) {
        for (t = tStates; t > 0; t--) {
            switch (mzScenarios[gdgCycle]) {
                case MZ_HORIZONTAL_RETRACE:
                    pixelCounter = 0;
                    break;
                case MZ_BORDER_DRAW:
                    setBorderPixel();
                    break;
                case MZ_SCREEN_BEGIN:
                    vBlnk = 1;
                    break;
                case MZ_SCREEN_END:
                    vBlnk = 0;
                    break;
                case MZ_SCREEN_DRAW:
                    setScreenPixel();
                    break;
                case MZ_VERTICAL_RETRACE:
                    resetGdgCycle();
                    break;
            }
            gdgCycle++;

            /*      curLnStat = gdgCycle % 227;

             if (curLnStat<43) {
             pixelCounter=0;
             if (curLnStat==0) m.i8253.runC1();
             }

             if (gdgCycle<10215) { //Process upper border
             if (curLnStat>42) {
             setBorderPixel();
             }
             } else

             if (gdgCycle<55615) { //Process main screen
             vBlnk = 1;
             if ((curLnStat>42)&(curLnStat<73)) setBorderPixel();else
             if ((curLnStat<201)&(curLnStat>72)) setScreenPixel();else
             if (curLnStat>200) setBorderPixel();
             if (gdgCycle==55614) {
             vBlnk = 0;
             if ((m.z80pio.mask & 0x20)==0) {
             if (m.z80pio.intenbl==0) {
             m.cpu.interrupt(m.z80pio.intAddr);
             }
             }
             }
             } else

             if (gdgCycle<65149) { //Process lower border
             if (curLnStat>42) {
             setBorderPixel();
             }
             } else

             if (gdgCycle<70824) { //Process vertical retrace
             //Do nothing, uff :-)
             vSync = 0;
             if (gdgCycle==70823) resetGdgCycle();
             }*/
        }
    }

    private int mzScenarios[] = new int[71000];
    private final int MZ_HORIZONTAL_RETRACE = 10;
    private final int MZ_BORDER_DRAW = 11;
    private final int MZ_SCREEN_DRAW = 12;
    private final int MZ_VERTICAL_RETRACE = 13;
    private final int MZ_DO_NOTHING = 14;
    private final int MZ_SCREEN_BEGIN = 15;
    private final int MZ_SCREEN_END = 16;

    /**
     * This constructor create table with screen scenarios
     */
    private void Gdg() {
        System.out.println("Creating MZ screen scenario table");
        int scenarioPos = 0;
        for (int y = 0; y < 45; y++) {
            for (int t = 0; t < 227; t++) {
                if (t < 43) {
                    mzScenarios[scenarioPos++] = MZ_HORIZONTAL_RETRACE;
                } else {
                    mzScenarios[scenarioPos++] = MZ_BORDER_DRAW;
                }
            }
        }
        for (int y = 0; y < 200; y++) {
            for (int t = 0; t < 227; t++) {
                if ((t == 0) & (y == 0)) {
                    mzScenarios[scenarioPos++] = MZ_SCREEN_BEGIN;
                } else if ((t == 226) & (y == 199)) {
                    mzScenarios[scenarioPos++] = MZ_SCREEN_END;
                } else if (t < 43) {
                    mzScenarios[scenarioPos++] = MZ_HORIZONTAL_RETRACE;
                } else if ((t > 42) & (t < 73)) {
                    mzScenarios[scenarioPos++] = MZ_BORDER_DRAW;
                } else if ((t < 201) & (t > 72)) {
                    mzScenarios[scenarioPos++] = MZ_SCREEN_DRAW;
                } else if (t > 200) {
                    mzScenarios[scenarioPos++] = MZ_BORDER_DRAW;
                }
            }
        }
        for (int y = 0; y < 42; y++) {
            for (int t = 0; t < 227; t++) {
                if (t < 43) {
                    mzScenarios[scenarioPos++] = MZ_HORIZONTAL_RETRACE;
                } else {
                    mzScenarios[scenarioPos++] = MZ_BORDER_DRAW;
                }
            }
        }
        for (int y = 0; y < 25; y++) {
            for (int t = 0; t < 227; t++) {
                if (t < 43) {
                    mzScenarios[scenarioPos++] = MZ_HORIZONTAL_RETRACE;
                } else {
                    mzScenarios[scenarioPos++] = 999;
                }
            }
        }
        mzScenarios[scenarioPos - 1] = MZ_VERTICAL_RETRACE;

        System.out.println("Current T-states per VBLNK: " + scenarioPos);
    }

    public void setRomMemory(RomMemory romMemory) {
        this.romMemory = romMemory;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}

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
package net.zdechov.sharpmz.jmzemu.module.device.counter;

/**
 * Sharp MZ Counter.
 *
 * This file was originally kindly provided by Zdeněk Adler, ZA Software, 2011
 *
 * @author JMZEmu Project (https://sharpmz.zdechov.net/?jmzemu)
 */
public class Counter {

    public int cnt;
    public int latch;
    public int value;
    public int hi;
    public int fl;
    public int mode;
    public int bcd;
    public int latched;
    public boolean hilo;

    public void reset() {
        value = 0xFFFF;
        cnt = 0xFFFF;
        hilo = false;
        latched = 0;
        mode = 0;
        bcd = 0;
        fl = 0;
    }

    public void outcnt(int b) {
        switch (fl) {
            case 1:
                cnt = (cnt & 0xFF00) | (b & 0xFF);
                break;
            case 2:
                cnt = (cnt & 0xFF) | (b << 8);
                break;
            case 3:
                cnt = (hilo) ? (cnt & 0Xff) | (b << 8) : (cnt & 0xFF00) | (b & 0xFF);
                hilo = !hilo;
                break;
        }
        value = cnt;
    }

    public int incnt() {
        if (latched > 0) {
            switch (fl) {
                case 1:
                    latched = 0;
                    return (latch & 0xFF);
                case 2:
                    latched = 0;
                    return (latch >> 8);
                case 3:
                    int val = (hilo) ? (latch >> 8) : (latch & 0xFF);
                    hilo = !hilo;
                    latched--;
                    return (val);
                default:
                    return 0xFF;
            }
        } else {
            switch (fl) {
                case 1:
                    return (cnt & 0xFF);
                case 2:
                    return (cnt >> 8);
                case 3:
                    int val = (hilo) ? (cnt >> 8) : (cnt & 0xFF);
                    hilo = !hilo;
                    return (val);
                default:
                    return 0xFF;
            }
        }
    }
}

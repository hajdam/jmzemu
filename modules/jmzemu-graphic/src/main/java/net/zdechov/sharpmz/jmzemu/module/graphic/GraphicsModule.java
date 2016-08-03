package net.zdechov.sharpmz.jmzemu.module.graphic;

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
import java.awt.image.BufferedImage;
import org.exbin.framework.api.XBApplicationModule;
import org.exbin.xbup.plugin.XBModuleHandler;

/**
 * Graphics module.
 *
 * @version 0.2.0 2016/08/03
 * @author JMZEmu Project (http://sharpmz.zdechov.net/?jmzemu)
 */
public class GraphicsModule implements XBApplicationModule {

    private final GraphicsComponent graphics = new GraphicsComponent();

    @Override
    public void init(XBModuleHandler xbmh) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void unregisterModule(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public GraphicsComponent getGraphicsComponent() {
        return graphics;
    }
    
    public void setGraphicsImage(BufferedImage image) {
        graphics.setGraphicsImage(image);
    }
}

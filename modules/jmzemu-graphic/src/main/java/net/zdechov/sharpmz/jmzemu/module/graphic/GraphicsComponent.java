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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.JComponent;

/**
 * Graphics component.
 *
 * @version 0.2.0 2016/08/03
 * @author JMZEmu Project (http://sharpmz.zdechov.net/?jmzemu)
 */
public class GraphicsComponent extends JComponent {

    private RenderingMode renderingMode = RenderingMode.KEEP_ASPECT_RATIO;

    private static final int DISPLAY_PAL_RESOLUTION_X = 460;
    private static final int DISPLAY_PAL_RESOLUTION_Y = 287;
    private Image screenImage = new BufferedImage(DISPLAY_PAL_RESOLUTION_X, DISPLAY_PAL_RESOLUTION_Y, BufferedImage.TYPE_INT_RGB);

    private final ImageObserver nullImageObserver = new ImageObserver() {
        @Override
        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
            return false;
        }
    };

    public GraphicsComponent() {
        // TODO: Remove - generates random lines
        Graphics g = screenImage.getGraphics();
        for (int i = 0; i < 1000; i++) {
            g.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
            g.drawLine((int) (Math.random() * DISPLAY_PAL_RESOLUTION_X), (int) (Math.random() * DISPLAY_PAL_RESOLUTION_Y), (int) (Math.random() * DISPLAY_PAL_RESOLUTION_X), (int) (Math.random() * DISPLAY_PAL_RESOLUTION_Y));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension size = getSize();
        RectangleStructure rect = new RectangleStructure();
        switch (renderingMode) {
            case STRETCH: {
                rect.x = 0;
                rect.y = 0;
                rect.width = size.width;
                rect.height = size.height;
                break;
            }
            case KEEP_ASPECT_RATIO: {
                int screenWidth = screenImage.getWidth(nullImageObserver);
                int screenHeight = screenImage.getHeight(nullImageObserver);
                if ((screenWidth * size.height) / screenHeight < size.width) {
                    rect.width = (screenWidth * size.height) / screenHeight;
                    rect.height = size.height;
                } else {
                    rect.width = size.width;
                    rect.height = (screenHeight * size.width) / screenWidth;
                }
                rect.x = (size.width - rect.width) / 2;
                rect.y = (size.height - rect.height) / 2;
                break;
            }
            case FIXED_NORMAL_ZOOM: {
                rect.width = screenImage.getWidth(nullImageObserver);
                rect.height = screenImage.getHeight(nullImageObserver);
                rect.x = (size.width - rect.width) / 2;
                rect.y = (size.height - rect.height) / 2;
                break;
            }
            case FIXED_DOUBLE_ZOOM: {
                rect.width = screenImage.getWidth(nullImageObserver) * 2;
                rect.height = screenImage.getHeight(nullImageObserver) * 2;
                rect.x = (size.width - rect.width) / 2;
                rect.y = (size.height - rect.height) / 2;
                break;
            }
        }
        g.drawImage(screenImage, rect.x, rect.y, rect.width, rect.height, nullImageObserver);
    }

    public void setGraphicsImage(BufferedImage image) {
        this.screenImage = image;
        repaint();
    }

    private static class RectangleStructure {

        int x;
        int y;
        int width;
        int height;
    }

    public static enum RenderingMode {
        FIXED_NORMAL_ZOOM,
        FIXED_DOUBLE_ZOOM,
        KEEP_ASPECT_RATIO,
        STRETCH
    }
}

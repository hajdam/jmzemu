/*
 * This file is part of JMzEmu.
 *
 * JMzEmu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JMzEmu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.zdechov.sharpmz.jmzemu;

import java.awt.Dimension;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.exbin.framework.XBBaseApplication;
import org.exbin.xbup.core.parser.basic.XBHead;
import org.exbin.framework.gui.about.api.GuiAboutModuleApi;
import org.exbin.framework.gui.editor.api.GuiEditorModuleApi;
import org.exbin.framework.gui.file.api.GuiFileModuleApi;
import org.exbin.framework.gui.frame.api.ApplicationFrameHandler;
import org.exbin.framework.gui.frame.api.GuiFrameModuleApi;
import org.exbin.framework.gui.menu.api.GuiMenuModuleApi;
import org.exbin.framework.gui.options.api.GuiOptionsModuleApi;
import org.exbin.framework.gui.undo.api.GuiUndoModuleApi;
import org.exbin.framework.gui.utils.ActionUtils;
import org.exbin.framework.api.XBApplicationModuleRepository;
import org.exbin.framework.deltahex.DeltaHexModule;
import org.exbin.framework.deltahex.panel.HexPanel;
import org.exbin.framework.gui.update.api.GuiUpdateModuleApi;

/**
 * Emulator main application.
 *
 * @version 0.2.0 2016/08/02
 * @author ExBin Project (http://exbin.org)
 */
public class JMzEmu {

    private static Preferences preferences;
    private static boolean verboseMode = false;
    private static boolean devMode = false;
    private static ResourceBundle bundle;

    /**
     * Main method launching the application.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        try {
            preferences = Preferences.userNodeForPackage(JMzEmu.class);
        } catch (SecurityException ex) {
            preferences = null;
        }
        try {
            bundle = ActionUtils.getResourceBundleByClass(JMzEmu.class);
            // Parameters processing
            Options opt = new Options();
            opt.addOption("h", "help", false, bundle.getString("cl_option_help"));
            opt.addOption("v", false, bundle.getString("cl_option_verbose"));
            opt.addOption("dev", false, bundle.getString("cl_option_dev"));
            BasicParser parser = new BasicParser();
            CommandLine cl = parser.parse(opt, args);
            if (cl.hasOption('h')) {
                HelpFormatter f = new HelpFormatter();
                f.printHelp(bundle.getString("cl_syntax"), opt);
            } else {
                verboseMode = cl.hasOption("v");
                devMode = cl.hasOption("dev");
                Logger logger = Logger.getLogger("");
                try {
                    logger.setLevel(Level.ALL);
                    logger.addHandler(new XBHead.XBLogHandler(verboseMode));
                } catch (java.security.AccessControlException ex) {
                    // Ignore it in java webstart
                }

                XBBaseApplication app = new XBBaseApplication();
                app.setAppPreferences(preferences);
                app.setAppBundle(bundle, ActionUtils.getResourceBaseNameBundleByClass(JMzEmu.class));
                app.init();

                XBApplicationModuleRepository moduleRepository = app.getModuleRepository();
                moduleRepository.addClassPathModules();
                moduleRepository.addModulesFromManifest(JMzEmu.class);
                moduleRepository.initModules();

                GuiFrameModuleApi frameModule = moduleRepository.getModuleByInterface(GuiFrameModuleApi.class);
                GuiEditorModuleApi editorModule = moduleRepository.getModuleByInterface(GuiEditorModuleApi.class);
                GuiMenuModuleApi menuModule = moduleRepository.getModuleByInterface(GuiMenuModuleApi.class);
                GuiAboutModuleApi aboutModule = moduleRepository.getModuleByInterface(GuiAboutModuleApi.class);
                GuiUndoModuleApi undoModule = moduleRepository.getModuleByInterface(GuiUndoModuleApi.class);
                GuiFileModuleApi fileModule = moduleRepository.getModuleByInterface(GuiFileModuleApi.class);
                GuiOptionsModuleApi optionsModule = moduleRepository.getModuleByInterface(GuiOptionsModuleApi.class);
                GuiUpdateModuleApi updateModule = moduleRepository.getModuleByInterface(GuiUpdateModuleApi.class);

                DeltaHexModule deltaHexModule = moduleRepository.getModuleByInterface(DeltaHexModule.class);

//                try {
//                    updateModule.setUpdateUrl(new URL(bundle.getString("update_url")));
//                    updateModule.setUpdateDownloadUrl(new URL(bundle.getString("update_download_url")));
//                } catch (MalformedURLException ex) {
//                    Logger.getLogger(JMzEmu.class.getName()).log(Level.SEVERE, null, ex);
//                }
                updateModule.registerDefaultMenuItem();
                aboutModule.registerDefaultMenuItem();

                frameModule.registerExitAction();
                frameModule.registerBarsVisibilityActions();

                // Register clipboard editing actions
                fileModule.registerMenuFileHandlingActions();
                fileModule.registerToolBarFileHandlingActions();
                fileModule.registerLastOpenedMenuActions();
                fileModule.registerCloseListener();

//                undoModule.registerMainMenu();
//                undoModule.registerMainToolBar();
//                undoModule.registerUndoManagerInMainMenu();

                // Register clipboard editing actions
                menuModule.registerMenuClipboardActions();
                menuModule.registerToolBarClipboardActions();

                optionsModule.registerMenuAction();

//                deltaHexModule.registerEditFindMenuActions();
//                deltaHexModule.registerEditFindToolBarActions();
//                deltaHexModule.registerViewNonprintablesMenuActions();
//                deltaHexModule.registerToolsOptionsMenuActions();
//                deltaHexModule.registerClipboardCodeActions();
//                deltaHexModule.registerOptionsMenuPanels();
//                deltaHexModule.registerGoToLine();
//                deltaHexModule.registerPropertiesMenu();
//                deltaHexModule.registerPrintMenu();
//                deltaHexModule.registerViewModeMenu();
//                deltaHexModule.registerCodeTypeMenu();
//                deltaHexModule.registerPositionCodeTypeMenu();
//                deltaHexModule.registerHexCharactersCaseHandlerMenu();
//                deltaHexModule.registerWordWrapping();

                ApplicationFrameHandler frameHandler = frameModule.getFrameHandler();
                HexPanel hexPanel = (HexPanel) deltaHexModule.getEditorProvider();
                editorModule.registerEditor("hex", hexPanel);
                editorModule.registerUndoHandler();
                undoModule.setUndoHandler(hexPanel.getHexUndoHandler());

//                deltaHexModule.registerStatusBar();
//                deltaHexModule.registerOptionsPanels();
//                deltaHexModule.getTextStatusPanel();
                updateModule.registerOptionsPanels();

//                deltaHexModule.loadFromPreferences(preferences);

                frameHandler.setMainPanel(editorModule.getEditorPanel());
                frameHandler.setDefaultSize(new Dimension(600, 400));
                frameHandler.show();
                updateModule.checkOnStart(frameHandler.getFrame());

                List fileArgs = cl.getArgList();
                if (fileArgs.size() > 0) {
                    fileModule.loadFromFile((String) fileArgs.get(0));
                }
            }
        } catch (ParseException | RuntimeException ex) {
            Logger.getLogger(JMzEmu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

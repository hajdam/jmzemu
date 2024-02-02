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
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.zdechov.sharpmz.jmzemu.module.graphic.GraphicsModule;
import net.zdechov.sharpmz.jmzemu.module.gui.panel.AboutDialogSidePanel;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.exbin.framework.App;
import org.exbin.framework.about.api.AboutModuleApi;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.addon.update.api.AddonUpdateModuleApi;
import org.exbin.framework.basic.BasicApplication;
import org.exbin.framework.editor.api.EditorModuleApi;
import org.exbin.framework.file.api.FileModuleApi;
import org.exbin.framework.frame.api.ApplicationFrameHandler;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.options.api.OptionsModuleApi;
import org.exbin.framework.preferences.api.Preferences;
import org.exbin.framework.preferences.api.PreferencesModuleApi;
import org.exbin.framework.ui.api.UiModuleApi;
import org.exbin.framework.window.api.WindowModuleApi;

/**
 * Emulator main application.
 *
 * @author JMZEmu Project (https://sharpmz.zdechov.net/?jmzemu)
 */
public class JMzEmu {

    /**
     * Main method launching the application.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        BasicApplication app = new BasicApplication();
        app.init();

        app.setAppDirectory(JMzEmu.class);
        app.addClassPathModules();
        app.addModulesFromManifest(JMzEmu.class);
        app.initModules();

        App.launch(() -> {
            PreferencesModuleApi preferencesModule = App.getModule(PreferencesModuleApi.class);
            preferencesModule.setupAppPreferences(JMzEmu.class);
            Preferences preferences = preferencesModule.getAppPreferences();
            ResourceBundle bundle = App.getModule(LanguageModuleApi.class).getBundle(JMzEmu.class);

            try {
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
                    return;
                }

                boolean verboseMode = cl.hasOption("v");
                boolean devMode = cl.hasOption("dev");

                final UiModuleApi uiModule = App.getModule(UiModuleApi.class);
                final WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
                FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
                EditorModuleApi editorModule = App.getModule(EditorModuleApi.class);
                ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
                AboutModuleApi aboutModule = App.getModule(AboutModuleApi.class);
                LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
                FileModuleApi fileModule = App.getModule(FileModuleApi.class);
                OptionsModuleApi optionsModule = App.getModule(OptionsModuleApi.class);
                AddonUpdateModuleApi updateModule = App.getModule(AddonUpdateModuleApi.class);

                frameModule.createMainMenu();
                updateModule.registerDefaultMenuItem();
                aboutModule.registerDefaultMenuItem();
                AboutDialogSidePanel sidePanel = new AboutDialogSidePanel();
                aboutModule.setAboutDialogSideComponent(sidePanel);
                frameModule.registerExitAction();
                frameModule.registerBarsVisibilityActions();

                // Register clipboard editing actions
                fileModule.registerMenuFileHandlingActions();
                fileModule.registerToolBarFileHandlingActions();
                fileModule.registerRecenFilesMenuActions();
                fileModule.registerCloseListener();

                // actionModule.registerMenuClipboardActions();
                optionsModule.registerMenuAction();

                languageModule.setAppBundle(bundle);
                uiModule.initSwingUi();

                final ApplicationFrameHandler frameHandler = frameModule.getFrameHandler();

                GraphicsModule graphicsModule = new GraphicsModule();
                frameHandler.setMainPanel(graphicsModule.getGraphicsComponent());
                frameHandler.setDefaultSize(new Dimension(600, 400));
                frameHandler.showFrame();

                List fileArgs = cl.getArgList();
                if (fileArgs.size() > 0) {
                    fileModule.loadFromFile((String) fileArgs.get(0));
                }
            } catch (ParseException | RuntimeException ex) {
                Logger.getLogger(JMzEmu.class.getName()).log(Level.SEVERE, null, ex);
//                System.exit(1);
            }
        });
    }
}

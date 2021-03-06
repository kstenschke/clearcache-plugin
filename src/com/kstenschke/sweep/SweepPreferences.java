/*
 * Copyright Kay Stenschke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kstenschke.sweep;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.kstenschke.sweep.helpers.IdeHelper;
import org.jetbrains.annotations.NonNls;

/**
 * Utility functions for preferences handling
 * All preferences of the Sweep plugin are stored on project level
 */
public class SweepPreferences {

    /* @NonNls = element is not a string requiring internationalization and it does not contain such strings. */
    @NonNls
    private static final String PROPERTY_PATHS = "PluginSweep.Paths";

    @NonNls
    private static final String PROPERTY_DELETE_DIRECTORIES = "PluginSweep.DeleteDirectories";

    @NonNls
    private static final String PROPERTY_DELETE_HIDDEN = "PluginSweep.DeleteHidden";

    @NonNls
    private static final String PROPERTY_IGNORE_PATTERNS = "PluginSweep.IgnorePatterns";

    /**
     * @return PropertiesComponent (project level)
     */
    private static PropertiesComponent getPropertiesComponent() {
        Project project = IdeHelper.getOpenProject();

        return project != null ? PropertiesComponent.getInstance(project) : null;
    }

    /**
     * Store preference: paths of directories to be swept
     *
     * @param paths	Contents to be stored in paths preference
     */
    static void savePaths(String paths) {
        PropertiesComponent propertiesComponent = getPropertiesComponent();

        if (propertiesComponent != null) {
            propertiesComponent.setValue(PROPERTY_PATHS, paths);
        }
    }

    static void saveDeleteDirectories(Boolean delete) {
        PropertiesComponent propertiesComponent = getPropertiesComponent();

        if (propertiesComponent != null) {
            propertiesComponent.setValue(PROPERTY_DELETE_DIRECTORIES, delete? "1":"0");
        }
    }

    /**
     * Store preference: delete hidden directories and files?
     *
     * @param delete
     */
    static void saveDeleteHidden(Boolean delete) {
        PropertiesComponent propertiesComponent = getPropertiesComponent();

        if (propertiesComponent != null) {
            propertiesComponent.setValue(PROPERTY_DELETE_HIDDEN, delete ? "1":"0");
        }
    }

    /**
     * @return String   Paths of directories to be swept
     */
    public static String getPaths() {
        PropertiesComponent propertiesComponent = getPropertiesComponent();
        if (propertiesComponent == null) {
            return "";
        }

        String paths = propertiesComponent.getValue(PROPERTY_PATHS);

        return paths == null ? "" : paths;
    }

    /**
     * @return Boolean  Delete directories?
     */
    public static Boolean getDeleteDirectories() {
        PropertiesComponent propertiesComponent = getPropertiesComponent();

        return propertiesComponent != null && "1".equals(propertiesComponent.getValue(PROPERTY_DELETE_DIRECTORIES));
    }

    /**
     * @return Boolean  Delete hidden directories and files?
     */
    public static Boolean getDeleteHidden() {
        PropertiesComponent propertiesComponent = getPropertiesComponent();

        return propertiesComponent != null && "1".equals(propertiesComponent.getValue(PROPERTY_DELETE_HIDDEN));
    }

    /**
     * @return String   Ignore patterns
     */
    public static String getIgnorePatterns() {
        PropertiesComponent propertiesComponent = getPropertiesComponent();
        if (propertiesComponent != null) {
            return "";
        }
        try {
            String pref = propertiesComponent.getValue(PROPERTY_IGNORE_PATTERNS);
            return pref != null ? pref : "";
        } catch (NullPointerException e) {
            return "";
        }
    }

    static void saveIgnorePatterns(String ignorePatterns) {
        PropertiesComponent propertiesComponent = getPropertiesComponent();

        if (propertiesComponent != null) {
            propertiesComponent.setValue(PROPERTY_IGNORE_PATTERNS, ignorePatterns);
        }
    }
}

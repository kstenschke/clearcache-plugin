/*
 * Copyright 2013-2017 Kay Stenschke
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

package com.kstenschke.sweep.resources.forms;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileSystemTree;
import com.intellij.openapi.fileChooser.FileSystemTreeFactory;
import com.intellij.openapi.fileChooser.ex.FileSystemTreeFactoryImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBScrollPane;
import com.kstenschke.sweep.SweepPreferences;
import com.kstenschke.sweep.helpers.SelectedDirectoriesCollector;

import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

public class SweepConfiguration {

    private JPanel rootPanel;

    private JPanel TopPanel;

    private JCheckBox filesCheckBox;

    private JCheckBox directoriesCheckBox;

    private JCheckBox hiddenFilesAndDirectoriesCheckBox;

    private JTextField textFieldIgnorePatterns;

    private JTree projectTree;

    /**
     * Constructor
     */
    public SweepConfiguration() {
        InitForm();
    }

    /**
     * Initialize the form: render project tree, select nodes from project preference
     */
    private void InitForm() {
        // Init checkbox options' states
        directoriesCheckBox.setSelected(SweepPreferences.getDeleteDirectories());
        hiddenFilesAndDirectoriesCheckBox.setSelected(SweepPreferences.getDeleteHidden());

        textFieldIgnorePatterns.setText(SweepPreferences.getIgnorePatterns());

        // Add project files tree, select directories from user's project preference
        FileSystemTreeFactory treeFactory = new FileSystemTreeFactoryImpl();
        FileChooserDescriptor descriptor =  new FileChooserDescriptor(false, true, false, false,false,false);

        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        if (projects.length > 0) {
            Project project = projects[0];

            if (project != null) {
                VirtualFile baseDir = project.getBaseDir();
                descriptor.setRoots(baseDir);
//            descriptor.setIsTreeRootVisible(true);

                FileSystemTree tree = treeFactory.createFileSystemTree(project, descriptor);

                // Enable multi-selection
                projectTree = tree.getTree();
                projectTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

                // Find+select directories in project file-tree, that are contained in preference also
                SelectedDirectoriesCollector selFilesCollector = new SelectedDirectoriesCollector(baseDir);
                VirtualFile[] selectedDirectories = selFilesCollector.getSelectedVFDirectories();

                if (selectedDirectories != null) {
                    tree.select(selectedDirectories, null);
                }

                tree.updateTree();

                // Add project folders tree to settings component
                JBScrollPane jbScrollPane = new JBScrollPane(projectTree);
                rootPanel.add(jbScrollPane);
            }

            // Setup changeListener on checkboxes- checking delete hidden checks also delete directories
            hiddenFilesAndDirectoriesCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        directoriesCheckBox.setSelected(true);
                    }
                }
            });
        }
    }

    /**
     * Reset the form to factory default
     */
    private void onClickReset(ActionEvent e) {

    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    /**
     * Config modified?
     *
     * @return Boolean
    */
    public boolean isModified() {
        return ! (
                getData().equals(SweepPreferences.getPaths())
            &&    isSelectedDeleteDirectories()  == SweepPreferences.getDeleteDirectories()
            &&    isSelectedDeleteHidden()       == SweepPreferences.getDeleteHidden()
           && getIgnorePatterns().equals(SweepPreferences.getIgnorePatterns())
        );
    }

    public Boolean isSelectedDeleteDirectories() {
        return directoriesCheckBox.isSelected();
    }

    public Boolean isSelectedDeleteHidden() {
        return hiddenFilesAndDirectoriesCheckBox.isSelected();
    }

    /**
     * @return  Ignore patterns
     */
    public String getIgnorePatterns() {
        return textFieldIgnorePatterns.getText();
    }

    /**
     * @param str  Ignore patterns value
     */
    public void setIgnorePatterns(String str) {
        textFieldIgnorePatterns.setText(str);
    }

    public void setData() {

    }

    /**
     * Get selection paths
     *
     * @return  String
     */
    public String getData() {
        TreePath[] selectionPaths  = projectTree.getSelectionModel().getSelectionPaths();

        return Arrays.toString(selectionPaths);
    }

   private void createUIComponents() {
        // @todo    place custom component creation code here
    }

}

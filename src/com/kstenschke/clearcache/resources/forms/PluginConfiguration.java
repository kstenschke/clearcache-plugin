/*
 * Copyright 2013 Kay Stenschke
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

package com.kstenschke.clearcache.resources.forms;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileSystemTree;
import com.intellij.openapi.fileChooser.FileSystemTreeFactory;
import com.intellij.openapi.fileChooser.ex.FileSystemTreeFactoryImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.kstenschke.clearcache.ClearCachePreferences;
import com.kstenschke.clearcache.helpers.SelectedDirectoriesCollector;

import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.event.*;
import java.util.Arrays;


public class PluginConfiguration {

    public JPanel rootPanel;
    private JTree projectTree;


    /**
     * Constructor
     */
    public PluginConfiguration() {
        InitForm();
    }



    /**
     * Initialize the form: render project tree, select nodes from project preference
     */
    private void InitForm() {
        FileSystemTreeFactory treeFactory = new FileSystemTreeFactoryImpl();
        FileChooserDescriptor descriptor =  new FileChooserDescriptor(false, true, false, false,false,false);

        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        Project project = projects[0];

		if( project != null ) {
			descriptor.setIsTreeRootVisible(true);
			VirtualFile baseDir  = project.getBaseDir();
			descriptor.setRoots(baseDir);

			FileSystemTree tree = treeFactory.createFileSystemTree(project, descriptor);

				// Enable multi-selection
			projectTree   = tree.getTree();
			projectTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

				// Find+select directories in project file-tree, that are contained in preference also
			SelectedDirectoriesCollector selFilesCollector = new SelectedDirectoriesCollector(baseDir);
			VirtualFile[] selectedDirectories = selFilesCollector.getSelectedVFDirectories();

			if( selectedDirectories != null ) {
				tree.select(selectedDirectories, null);
			}

			tree.updateTree();

				// Add project folders tree to settings component
			JScrollPane jscrollPane  = new JScrollPane(projectTree);
			rootPanel.add(jscrollPane);
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
        return ! ( getData().equals( ClearCachePreferences.getPaths() ) );
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

        return Arrays.toString( selectionPaths );
    }




    private void createUIComponents() {
// TODO: place custom component creation code here
    }

}
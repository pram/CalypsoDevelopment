package com.naughtyzombie.calypso.maven.model;

import com.naughtyzombie.calypso.maven.process.CalypsoProcess;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: c935533
 * Date: 21/12/12
 * Time: 09:27
 */
public class CalypsoProcessModel implements TreeModel {

    CalypsoProcess root;

    public CalypsoProcessModel(CalypsoProcess root) {
        this.root = root;
    }

    @Override
    public Object getRoot() {
        return this.root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        CalypsoProcess parentProcess = (CalypsoProcess) parent;
        List<CalypsoProcess> children = parentProcess.getChildren();
        return children != null ? children.get(index) : null;
    }

    @Override
    public int getChildCount(Object parent) {
        CalypsoProcess parentProcess = (CalypsoProcess) parent;
        return parentProcess.getChildren() != null ? parentProcess.getChildren().size() : 0;
    }

    @Override
    public boolean isLeaf(Object node) {
        CalypsoProcess parentProcess = (CalypsoProcess) node;
        List<CalypsoProcess> children = parentProcess.getChildren();

        if (children != null) {
            return !(children.size() > 0);
        }

        return true;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return 0;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
    }
}

package com.yeahmobi.yscheduler.web.controller.topo;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

public class TopoTreeManager {

    private int col;

    private int row;

    public void init() {
        this.col = 0;
    }

    private TopoNode getRoot(List<TopoNode> nodes) {
        Set<TopoNode> notRootNodes = new HashSet<TopoNode>();
        for (TopoNode node : nodes) {
            notRootNodes.addAll(node.getNodes());
        }
        Collection subtract = CollectionUtils.subtract(nodes, notRootNodes);
        if (!subtract.isEmpty()) {
            return (TopoNode) subtract.iterator().next();
        } else {
            return null;
        }
    }

    public void tag(List<TopoNode> tree, boolean revert) {
        TopoNode root = getRoot(tree);
        tag(root, 0);
        if (revert) {
            revert(tree);
        }
    }

    private void revert(List<TopoNode> tree) {
        for (TopoNode node : tree) {
            node.setRow(this.row - node.getRow());
        }
    }

    private void tag(TopoNode node, int row) {
        tagRow(node, row);
        tagCol(node);
    }

    private void tagRow(TopoNode node, int row) {
        if (node.getRow() < row) {
            node.setRow(row);
            if (this.row < row) {
                this.row = row;
            }
        }
        row++;

        for (TopoNode child : node.getNodes()) {
            tagRow(child, row);
        }
    }

    private int tagCol(TopoNode node) {
        if (node.getHasArrived()) {
            return this.col++;
        }

        List<TopoNode> children = node.getNodes();
        int size = children == null ? 0 : children.size();
        if (size == 0) {
            node.setColumn(this.col);
            this.col++;
        } else {
            for (int i = 0; i < (size / 2); i++) {
                tagCol(children.get(i));
            }
            if ((size % 2) != 0) {
                node.setColumn(tagCol(children.get((size - 1) / 2)));
            } else {
                node.setColumn(this.col);
                this.col++;
            }
            for (int i = (size + 1) / 2; i < size; i++) {
                tagCol(children.get(i));
            }
        }
        return node.getColumn();
    }
}

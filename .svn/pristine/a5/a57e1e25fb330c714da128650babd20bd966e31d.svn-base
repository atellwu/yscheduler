package com.yeahmobi.yscheduler.web.controller.topo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yeahmobi.yscheduler.model.type.TaskInstanceStatus;

@Service
public class PageBuilder {

    private static final String NODE_FORMAT    = "<span class='window %s' id='%s' rel='tooltip' title='%s' >%s</span>\n";

    private static final String CSS_FORMAT     = "#%s {left: %sem;top: %sem;}\n";

    private static final String CONNECT_FORMAT = "instance.connect({uuids : [ '%s', '%s' ],overlays : overlays});\n";

    private static final String BOTTOM         = "-bottom";

    private static final String TOP            = "-top";

    private static final int    BASE_LEFT      = 5;

    private static final int    BASE_TOP       = 5;

    private static final int    ITEM_LEFT      = 10;

    private static final int    ITEM_TOP       = 10;

    public String nodeHtml(List<TopoNode> nodes) {
        String result = "";
        for (TopoNode node : nodes) {
            TaskInstanceStatus status = node.getStatus();
            String name = "default";
            String desc = "";
            if (status != null) {
                name = status.name().toLowerCase();
                desc = status.getDesc();
            }
            result += String.format(NODE_FORMAT, name, node.getName(), desc, node.getName());
        }
        return result;
    }

    public String css(List<TopoNode> nodes) {
        String result = "<style>";
        for (TopoNode node : nodes) {
            result += String.format(CSS_FORMAT, node.getName(), (node.getColumn() * ITEM_LEFT) + BASE_LEFT,
                                    (node.getRow() * ITEM_TOP) + BASE_TOP);
        }
        return result + "</style>";
    }

    public String connectHtml(List<TopoNode> nodes, boolean revert) {
        String result = "";
        for (TopoNode node : nodes) {

            List<TopoNode> connectNodes = node.getNodes();
            if (connectNodes != null) {
                for (TopoNode anotherNode : connectNodes) {
                    if (revert) {
                        result += String.format(CONNECT_FORMAT, anotherNode.getName() + BOTTOM, node.getName() + TOP);
                    } else {
                        result += String.format(CONNECT_FORMAT, node.getName() + BOTTOM, anotherNode.getName() + TOP);
                    }
                }
            }
        }
        return result;
    }
}

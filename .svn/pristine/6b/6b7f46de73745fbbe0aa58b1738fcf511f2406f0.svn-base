package com.yeahmobi.yscheduler.web.controller.topo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yeahmobi.yscheduler.model.common.UserContextHolder;
import com.yeahmobi.yscheduler.web.controller.AbstractController;
import com.yeahmobi.yscheduler.web.controller.workflow.WorkflowHelper;
import com.yeahmobi.yscheduler.web.controller.workflow.common.CommonWorkflowHelper;
import com.yeahmobi.yscheduler.web.topo.service.WorkflowTopoService;
import com.yeahmobi.yscheduler.web.vo.WorkflowDetailVO;

/**
 * @author Ryan Sun
 */
@Controller
@RequestMapping(value = { TopoController.SCREEN_NAME })
public class TopoController extends AbstractController {

    public static final String   SCREEN_NAME      = "topo";

    private static final Logger  LOGGER           = LoggerFactory.getLogger(TopoController.class);

    private static final String  PRIVATE_WORKFLOW = "workflow";

    private static final String  PRIVATE_INSTANCE = "instance";

    private static final String  COMMON_WORKFLOW  = "common_workflow";

    private static final String  COMMON_INSTANCE  = "common_instance";

    @Autowired
    private PageBuilder          pageBuilder;

    @Autowired
    @Qualifier("privateTopo")
    private WorkflowTopoService  privateWorkflowTopoService;

    @Autowired
    @Qualifier("commonTopo")
    private WorkflowTopoService  commonWorkflowTopoService;

    @Autowired
    private WorkflowHelper       workflowCreateHelper;

    @Autowired
    private CommonWorkflowHelper commonWorkflowCreateHelper;

    @RequestMapping(value = { "" }, method = RequestMethod.GET)
    public ModelAndView workflow(long id, String type) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<TopoNode> nodes;
        TopoTreeManager topoTreeManager = new TopoTreeManager();
        boolean isAdmin = UserContextHolder.getUserContext().isAdmin();
        long userId = UserContextHolder.getUserContext().getId();
        if (PRIVATE_WORKFLOW.equals(type)) {
            nodes = treeToList(this.privateWorkflowTopoService.buildWorkflowTopoTree(id, isAdmin, userId));
        } else if (PRIVATE_INSTANCE.equals(type)) {
            nodes = treeToList(this.privateWorkflowTopoService.buildInstanceTopoTree(id, isAdmin, userId));
            map.put("iframe", true);
        } else if (COMMON_WORKFLOW.equals(type)) {
            nodes = treeToList(this.commonWorkflowTopoService.buildWorkflowTopoTree(id, isAdmin, userId));
        } else if (COMMON_INSTANCE.equals(type)) {
            nodes = treeToList(this.commonWorkflowTopoService.buildInstanceTopoTree(id, isAdmin, userId));
            map.put("iframe", true);
        } else {
            LOGGER.warn("Wrong topo type: " + type);
            return null;
        }

        if (COMMON_WORKFLOW.equals(type) || COMMON_INSTANCE.equals(type)) {
            if (!nodes.isEmpty()) {
                topoTreeManager.tag(nodes, false);
            }
            map.put("connects", this.pageBuilder.connectHtml(nodes, false));
        } else {
            if (!nodes.isEmpty()) {
                topoTreeManager.tag(nodes, true);
            }
            map.put("connects", this.pageBuilder.connectHtml(nodes, true));
        }

        map.put("nodes", this.pageBuilder.nodeHtml(nodes));
        map.put("css", this.pageBuilder.css(nodes));
        return screen(map, SCREEN_NAME);
    }

    @RequestMapping(value = { "" }, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object workflow(HttpServletRequest request, Long id, String type) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {

            List<TopoNode> nodes;
            TopoTreeManager topoTreeManager = new TopoTreeManager();
            boolean isAdmin = UserContextHolder.getUserContext().isAdmin();
            long userId = UserContextHolder.getUserContext().getId();
            if (COMMON_WORKFLOW.equals(type)) {
                List<WorkflowDetailVO> detailVos = this.commonWorkflowCreateHelper.parse(id, request);

                nodes = treeToList(this.commonWorkflowTopoService.buildWorkflowTopoTree(id, detailVos, isAdmin, userId));
                if (!nodes.isEmpty()) {
                    topoTreeManager.tag(nodes, false);
                }
                map.put("connects", this.pageBuilder.connectHtml(nodes, false));

            } else {
                List<WorkflowDetailVO> detailVos = this.workflowCreateHelper.parse(request);
                nodes = treeToList(this.privateWorkflowTopoService.buildWorkflowTopoTree(null, detailVos, isAdmin,
                                                                                         userId));
                if (!nodes.isEmpty()) {
                    topoTreeManager.tag(nodes, true);
                }
                map.put("connects", this.pageBuilder.connectHtml(nodes, true));
            }

            map.put("nodes", this.pageBuilder.nodeHtml(nodes));
            map.put("css", this.pageBuilder.css(nodes));
            map.put("success", true);
        } catch (IllegalArgumentException e) {
            map.put("success", false);
            map.put("errorMsg", e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            map.put("success", false);
            map.put("errorMsg", e.getMessage());
        }
        return screen(map, SCREEN_NAME);

    }

    // private List<TopoNode> build() {
    // TopoNode node1 = new TopoNode();
    // TopoNode node2 = new TopoNode();
    // TopoNode node3 = new TopoNode();
    // TopoNode node4 = new TopoNode();
    // TopoNode node5 = new TopoNode();
    // TopoNode node6 = new TopoNode();
    // TopoNode node7 = new TopoNode();
    // TopoNode node8 = new TopoNode();
    // TopoNode node9 = new TopoNode();
    // node1.setName("node1");
    // node2.setName("node2");
    // node3.setName("node3");
    // node4.setName("node4");
    // node5.setName("node5");
    // node6.setName("node6");
    // node7.setName("node7");
    // node8.setName("node8");
    // node9.setName("node9");
    // node1.setStatus(TaskInstanceStatus.DEPENDENCY_WAIT);
    // node2.setStatus(TaskInstanceStatus.READY);
    // node3.setStatus(TaskInstanceStatus.RUNNING);
    // node4.setStatus(TaskInstanceStatus.SUCCESS);
    // node5.setStatus(TaskInstanceStatus.FAILED);
    // node6.setStatus(TaskInstanceStatus.WORKFLOW_FAILED);
    // node7.setStatus(TaskInstanceStatus.CANCELLED);
    // node8.setStatus(TaskInstanceStatus.SKIPPED);
    // node9.setStatus(TaskInstanceStatus.COMPLETE_WITH_UNKNOWN_STATUS);
    //
    // node1.setNodes(Arrays.asList(node2, node3));
    // node2.setNodes(Arrays.asList(node4));
    // node4.setNodes(Arrays.asList(node5, node6, node7));
    // node3.setNodes(Arrays.asList(node8, node9));
    //
    // List<TopoNode> nodes = new ArrayList<TopoNode>();
    // nodes.add(node1);
    // nodes.add(node2);
    // nodes.add(node3);
    // nodes.add(node4);
    // nodes.add(node5);
    // nodes.add(node6);
    // nodes.add(node7);
    // nodes.add(node8);
    // nodes.add(node9);
    // return nodes;
    //
    // }

    private ArrayList<TopoNode> treeToList(TopoNode root) {
        Set<TopoNode> nodes = new HashSet<TopoNode>();
        treeToSet(root, nodes);
        return new ArrayList<TopoNode>(nodes);
    }

    private void treeToSet(TopoNode root, Set<TopoNode> nodes) {
        nodes.add(root);
        List<TopoNode> children = root.getNodes();
        if (children != null) {
            for (TopoNode child : children) {
                treeToSet(child, nodes);
            }
        }
    }

}

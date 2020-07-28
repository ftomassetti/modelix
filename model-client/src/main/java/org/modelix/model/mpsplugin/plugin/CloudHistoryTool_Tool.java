package org.modelix.model.mpsplugin.plugin;

/*Generated by MPS */

import jetbrains.mps.plugins.tool.GeneratedTool;
import javax.swing.Icon;
import org.modelix.model.mpsplugin.history.HistoryView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowAnchor;
import org.modelix.model.mpsplugin.CloudRepository;
import org.modelix.model.lazy.TreeId;
import org.modelix.model.lazy.CLVersion;
import javax.swing.JComponent;

public class CloudHistoryTool_Tool extends GeneratedTool {
  private static final Icon ICON = IconContainer.ICON_a0_0;
  private HistoryView component;
  public CloudHistoryTool_Tool(Project project) {
    super(project, "Cloud History", null, ICON, ToolWindowAnchor.BOTTOM, false);
  }
  public void init(Project project) {
    super.init(project);
  }
  public void load(CloudRepository cloudRepository, TreeId treeId, CLVersion headVersion) {
    ((HistoryView) CloudHistoryTool_Tool.this.getComponent()).loadHistory(cloudRepository, treeId, headVersion);
    CloudHistoryTool_Tool.this.openTool(true);
  }
  public JComponent getComponent() {
    if (CloudHistoryTool_Tool.this.component == null) {
      CloudHistoryTool_Tool.this.component = new HistoryView();
    }
    return CloudHistoryTool_Tool.this.component;
  }
}

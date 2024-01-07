package cc.catman.workbench.api.server.websocket.run.value.breakpoint;

import java.util.List;

public class BreakPointManager {
    private List<BreakPoint> breakPoints;
    void addBreakPoint(BreakPoint breakPoint) {
        breakPoints.add(breakPoint);
    }
    void removeBreakPoint(BreakPoint breakPoint) {
        breakPoints.remove(breakPoint);
    }
    void updateBreakPoint(BreakPoint breakPoint) {
        breakPoints.remove(breakPoint);
        breakPoints.add(breakPoint);
    }

    /**
     * 等待断点的执行
     */
    void waitBreakPoint() {
    }
}

package cc.catman.workbench.api.server.websocket.run.debug.command;

public class StepIntoCommand extends Command {

    private StepIntoCommandData data;

    public static class StepIntoCommandData {
        private String threadId;

        public String getThreadId() {
            return threadId;
        }

        public void setThreadId(String threadId) {
            this.threadId = threadId;
        }
    }
}

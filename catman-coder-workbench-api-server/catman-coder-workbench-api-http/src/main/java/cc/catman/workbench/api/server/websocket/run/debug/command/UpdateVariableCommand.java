package cc.catman.workbench.api.server.websocket.run.debug.command;

public class UpdateVariableCommand extends Command {

    private UpdateVariableCommandData data;

    public static class UpdateVariableCommandData {
        private String threadId;
        private String variableId;
        private String value;

        public String getThreadId() {
            return threadId;
        }

        public void setThreadId(String threadId) {
            this.threadId = threadId;
        }

        public String getVariableId() {
            return variableId;
        }

        public void setVariableId(String variableId) {
            this.variableId = variableId;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}

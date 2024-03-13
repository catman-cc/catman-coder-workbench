package cc.catman.coder.workbench.core.message;

public interface Constants {
    interface  Message{
        interface  Topic{
            interface Channel{
                String  CREATE = "system/channel/create";
            }
            interface System{
                String PING = "system/ping";
                String PONG = "system/pong";
            }
        }
        interface Channel{
            String SYSTEM_ID = "system";
            String SYSTEM_KIND="default";
        }
    }

}

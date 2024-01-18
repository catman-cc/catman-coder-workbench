package cc.catman.workbench.api.server.configuration.netty;

import cc.catman.coder.workbench.core.Base;

import java.util.List;

/**
 * http触发器的描述信息
 */
public class HttpTrigger extends Base {
    private String id;

    private String name;
    /**
     * 触发器地址,默认以/开头,如果未显式提供,则为其追加一个/
     * 该uri支持通配符,例如: /api/v1/{id}
     * 同时支持多级通配符,例如: /api/v1/{id}/{name}
     * 除此之外,还支持regex表达式,例如: /api/v1/{id:\\d+}
     */
    private String uri;

    /**
     * 验证uri的方式,默认为ant path
     */
    private String match;

    /**
     * 支持的http方法,可以有多个方法匹配
     */
    private List<String> methods;

    /**
     * 触发器的处理器,交由后方的任务处理器处理
     * 处理器是对原始job的封装,需要额外提供下面几个能力:
     * - 重写uri,header,body等数据
     * - 支持断言和重放,比如: 断言某个字段的值是否符合预期,如果不符合,则重放,在重放时应该排除掉当前处理器
     * - 支持向上下文中写入更多额外数据,比如: 从header中获取token,然后写入到上下文中,供后续的处理器使用
     */
    private String handlerId;
}

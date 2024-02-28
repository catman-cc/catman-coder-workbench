package cc.catman.workbench.api.server.configuration.exception.ecodes;


import cc.catman.workbench.api.server.configuration.exception.annotations.Prefix;

@Prefix("SQL")
public enum SQLECode implements ECode<String> {
    INSERT_FAIL("001", "插入失败")
   ,SELECT_NONE("002", "查询失败")
           ;

    private String code;
    private String message;

    SQLECode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}

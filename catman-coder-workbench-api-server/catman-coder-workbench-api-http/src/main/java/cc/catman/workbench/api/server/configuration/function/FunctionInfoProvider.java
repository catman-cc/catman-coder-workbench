package cc.catman.workbench.api.server.configuration.function;

import cc.catman.coder.workbench.core.function.FunctionInfo;
import cc.catman.coder.workbench.core.type.SimpleTypeAnalyzer;
import cc.catman.coder.workbench.core.type.TypeDefinition;

import java.util.ArrayList;
import java.util.List;

public class FunctionInfoProvider {

    public List<FunctionInfo> provider() {
        List<FunctionInfo> functionInfos = new ArrayList<>();

        functionInfos.add(ifFunctionInfo());

        return functionInfos;
    }

    public FunctionInfo ifFunctionInfo() {
        FunctionInfo ifFunctionInfo = FunctionInfo.builder()
                .id("if")
                .name("if")
                .kind("if")
                .describe("if函数")
                .build();



        ifFunctionInfo.getContext().add(ifFunctionInfo);

        // if函数的定义,接受三个参数
        // 1. 条件,必传,函数返回值要求为bool
        // 2. 条件为真时执行的函数,非必填,函数返回值任意类型
        // 3. 条件为假时执行的函数,非必填,函数返回值任意类型
        TypeDefinition args = SimpleTypeAnalyzer.of(SimpleTypeAnalyzer.TypeDesc.create()
                .add("if", "boolean")
                .add("true", "function")
                .add("false", "function")
        ).analyzer();

        ifFunctionInfo.addArgs(args.getAllItems());
        return ifFunctionInfo;
    }
}

package cc.catman.workbench.api.server.configuration.function;

import cc.catman.coder.workbench.core.function.FunctionInfo;
import cc.catman.coder.workbench.core.type.DefaultType;
import cc.catman.coder.workbench.core.type.SimpleTypeAnalyzer;
import cc.catman.coder.workbench.core.type.TypeAnalyzer;
import cc.catman.coder.workbench.core.type.TypeDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FunctionInfoProvider {

    public List<FunctionInfo> provider(){
        List<FunctionInfo> functionInfos = new ArrayList<>();


        FunctionInfo ifFunctionInfo= FunctionInfo.builder()
                .id("if")
                .name("if")
                .kind("if")
                .describe("if函数")
                .build();

        Map<String, TypeDefinition> ifArgs = ifFunctionInfo.getArgs();
        // if函数的定义,接受三个参数
        // 1. 条件,必传,函数返回值要求为bool
        // 2. 条件为真时执行的函数,非必填,函数返回值任意类型
        // 3. 条件为假时执行的函数,非必填,函数返回值任意类型
        ifArgs.put("condition",TypeDefinition.builder().name("condition").type(
                DefaultType.builder().typeName("boolean").build()
        ).build());
        ifArgs.put("true",TypeDefinition.builder().name("true").type(
                DefaultType.builder().typeName("any").build()
        ).build());

        ifArgs.put("false",TypeDefinition.builder().name("false").type(
                DefaultType.builder().typeName("any").build()
        ).build());
        functionInfos.add(ifFunctionInfo);
        return functionInfos;
    }

    public static void main(String[] args) {
        FunctionInfoProvider functionInfoProvider = new FunctionInfoProvider();
        List<FunctionInfo> functionInfos = functionInfoProvider.provider();
        SimpleTypeAnalyzer simpleTypeAnalyzer = SimpleTypeAnalyzer.of(SimpleTypeAnalyzer.TypeDesc.create()
                .add("if", "boolean")
                .add("true", "any")
                .add("false", "any")
        );
        TypeDefinition analyzer = simpleTypeAnalyzer.analyzer();

        System.out.println(functionInfos);
    }
}

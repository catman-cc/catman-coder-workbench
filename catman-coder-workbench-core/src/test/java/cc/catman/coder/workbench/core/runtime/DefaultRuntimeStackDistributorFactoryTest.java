package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.ILoopReferenceContext;
import cc.catman.coder.workbench.core.executor.DefaultExecutorManager;
import cc.catman.coder.workbench.core.executor.WorkerExecutor;
import cc.catman.coder.workbench.core.function.FunctionCallInfo;
import cc.catman.coder.workbench.core.function.FunctionInfo;
import cc.catman.coder.workbench.core.node.DefaultWorker;
import cc.catman.coder.workbench.core.node.DefaultWorkerManager;
import cc.catman.coder.workbench.core.node.LocalExecutorService;
import cc.catman.coder.workbench.core.node.WorkInfoHelper;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.runtime.executor.DefaultFunctionExecutorManager;
import cc.catman.coder.workbench.core.runtime.parameter.DefaultParameterParserManager;
import cc.catman.coder.workbench.core.schedule.DefaultSchedule;
import cc.catman.coder.workbench.core.schedule.ISchedule;
import cc.catman.coder.workbench.core.type.DefaultType;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

class DefaultRuntimeStackDistributorFactoryTest {

    @Test
    void create() {
        FunctionCallInfo functionCallInfo = getFunctionCallInfo();

        DefaultFunctionVariablesTable variablesTable = DefaultFunctionVariablesTable
                .of(EIFunctionVariableScope.GLOBAL, Map.of("name", "hello world!"));

        DefaultFunctionRuntimeProvider provider = DefaultFunctionRuntimeProvider
                .create(functionCallInfo,variablesTable);

        DefaultRuntimeStackDistributorFactory factory = createDistributorFactory();
        IRuntimeStackDistributor stackDistributor = factory.create(provider);

        IRuntimeStack stack = stackDistributor.createRuntimeStack(provider);

        IFunctionCallResultInfo res = stack.call(functionCallInfo);
        assert res.hasResult();
        assert "hello world!".equals(res.getResult());
    }

    @NotNull
    private static FunctionCallInfo getFunctionCallInfo() {
        ILoopReferenceContext context = ILoopReferenceContext.create();

        TypeDefinition td = TypeDefinition.builder()
                .id("tdID")
                .name("expression")
                .type(DefaultType.builder()
                        .typeName(Constants.Type.TYPE_NAME_STRING)
                        .build())
                .build();
        context.add(td);

        FunctionInfo vfi = FunctionInfo.builder()
                .id("2f")
                .kind("simple")
                .build();
        context.add(vfi);

        FunctionCallInfo vf = FunctionCallInfo.builder().id("2")
                .functionId("2f")
                .config("name")
                .build();
        context.add(vf);


        Parameter parameter = Parameter.builder()
                .id("pid")
                .name("expression")
                .valueFunction(vf)
                .type(td)
                .typeId(td.getId())
                .build();
        context.add(parameter);

        FunctionInfo functionInfo = FunctionInfo.builder()
                .id("functionInfo")
                .kind("expression")
                .build();
        functionInfo.addArg("expression", td);
        context.add(functionInfo);

        FunctionCallInfo functionCallInfo = FunctionCallInfo.builder()
                .id("1")
                .functionId("functionInfo")
                .build();
        functionCallInfo.addArg("expression", parameter);
        context.add(functionCallInfo);
        return functionCallInfo;
    }

    private static DefaultRuntimeStackDistributorFactory createDistributorFactory() {
        ConversionService conversionService = DefaultConversionService.getSharedInstance();
        DefaultParameterParserManager parameterParserManager = new DefaultParameterParserManager();

        DefaultExecutorManager executorManager = new DefaultExecutorManager();
        executorManager.addExecutor(WorkerExecutor.builder()
                .id("default-local")
                .supportedFunctions(Arrays.asList(
                        FunctionInfo.builder().kind("expression").build()
                        , FunctionInfo.builder().kind("if").build()
                        , FunctionInfo.builder().kind("simple").build()
                ))
                .worker(DefaultWorker.builder()
                        .id("local" + UUID.randomUUID())
                        .local(true)
                        .executorService(new LocalExecutorService())
                        .messageBus(null)
                        .workerManager(new DefaultWorkerManager())
                        .systemInfo(WorkInfoHelper.getWorkInfo())
                        .build())
                .build());
        DefaultFunctionExecutorManager functionExecutorManager = new DefaultFunctionExecutorManager();
        ISchedule schedule = new DefaultSchedule(executorManager);

        return DefaultRuntimeStackDistributorFactory
                .builder()
                .executorManager(functionExecutorManager)
                .schedule(schedule)
                .parameterParserManager(parameterParserManager)
                .conversionService(conversionService)
                .build();
    }
}
## Usage example of basic function stack

You can use the code below to create a stack dispatcher that supports if/express/simple-value:

```java
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
```
Then, you can create a FunctionCallInfo message according to your needs, or just use the following code example.

> Note: The following code example is a simple example of how to use the stack dispatcher. You can modify the code according to your needs.
> You can simplify the process of creating FunctionCallInfo with the help of tools such as TypeAnalyzer.

```java
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
```

Then, you can use the following code to create a stack dispatcher and execute the function call.

> The following code provides a value containing only "hello world!" Examples of global variables.

```java
public void test(){
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
```


package cc.catman.coder.workbench.core.runtime;

import lombok.experimental.SuperBuilder;


@SuperBuilder
public class DefaultRuntimeStack extends AbstractRuntimeStack{

    @Override
    public IRuntimeStack createChildStack(String prefix, IFunctionRuntimeProvider provider, IRuntimeStackDistributor distributor) {

       return DefaultRuntimeStack.builder()
                .id(createStackId())
                .name(createStackName(prefix))
                .parentStack(parentStack)
                .variablesTable(provider.getVariablesTable())
                .distributor(this.getDistributor())
               .functionRuntimeProvider(provider)
                .runtimeDebuggerContext(this.runtimeDebuggerContext)
                .runtimeReportContext(this.runtimeReportContext)
                .parameterParserManager(parameterParserManager)
                .conversionService(conversionService)
               .schedule(schedule)
               .executorManager(executorManager)
                .isAsync(false)
                .indexOfStack(0)
                .debugMod(false)
                .build();
    }

}

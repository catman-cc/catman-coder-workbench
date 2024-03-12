package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.runtime.debug.Breakpoint;
import cc.catman.coder.workbench.core.runtime.executor.IFunctionExecutorManager;
import cc.catman.coder.workbench.core.schedule.ISchedule;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.core.convert.ConversionService;

import java.util.List;


@Data
@Builder
public class DefaultRuntimeStackDistributorFactory {

    private IParameterParserManager parameterParserManager;

    @Getter
    private IFunctionExecutorManager executorManager;

    private ConversionService conversionService;

    @Getter
    private ISchedule schedule;

    public IRuntimeStackDistributor create(IFunctionRuntimeProvider functionRuntimeProvider){
        return DefaultRuntimeStackDistributor.builder()
                .reportContext(createReportContext())
                .debuggerContext(createDebuggerContext())
                .executorManager(this.executorManager)
                .conversionService(this.conversionService)
                .parameterParserManager(parameterParserManager)
                .schedule(this.schedule)
                .build();
    }

    protected IRuntimeReportContext createReportContext(){
        return new IRuntimeReportContext() {
        };
    }

    private IRuntimeDebuggerContext createDebuggerContext(){
        return new IRuntimeDebuggerContext() {
            @Override
            public IRuntimeStackDistributor getDistributor() {
                return null;
            }

            @Override
            public List<Breakpoint> getBreakpoints() {
                return null;
            }

            @Override
            public IRuntimeStack getRuntimeStack() {
                return null;
            }

            @Override
            public void onStartBreakpoint() {

            }

            @Override
            public void onEndBreakpoint() {

            }

            @Override
            public void doTriggerBreakpoint(String breakpointName, Object... args) {

            }
        };
    }
}

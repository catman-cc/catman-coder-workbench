package cc.catman.coder.workbench.core.runtime.parameter.parser;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.function.FunctionCallInfo;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.runtime.IRuntimeStack;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;


/**
 * 文件类型参数解析策略
 * 支持将特定格式的对象转换为文件类型
 * - 特点结构的文件描述对象
 * - String类型的base64编码文件
 */
public class FileParameterParseStrategy extends AbstractParameterParseStrategy{
    @Override
    public Object doParse(Parameter parameter, Object preParseValue, IRuntimeStack stack) {
        FunctionCallInfo functionCallInfo = getFunctionCallInfo(parameter);
        if (Optional.ofNullable(functionCallInfo).isEmpty()){
            return null;
        }
        Object possibleFile = functionCallInfo.call(stack);
        if (Optional.ofNullable(possibleFile).isEmpty()){
            return null;
        }

        if (possibleFile instanceof byte[]){
           return possibleFile;
        }

        if (possibleFile instanceof File){
            return extractFile((File) possibleFile);
        }

        if (possibleFile instanceof String){
            return this.parseFileFromBase64((String) possibleFile);
        }
        throw new RuntimeException("不支持的文件类型");
    }

    private byte[] extractFile(File possibleFile) {
        try {
            return Files.toByteArray(possibleFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] parseFileFromBase64(String base64) {
        return Base64.getDecoder().decode(base64);
    }
    @Override
    public String getSupportTypeName() {
        return Constants.Type.TYPE_NAME_FILE;
    }
}

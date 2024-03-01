package cc.catman.coder.workbench.core.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypeDefinitionAnalyzerTest {

    @Test
    void analyze() {
        TypeDefinitionAnalyzer analyzer = TypeDefinitionAnalyzer.builder().object(TestType.class).build();
        TypeDefinition typeDefinition = analyzer.analyze();
        assertNotNull(typeDefinition);
        assertEquals("test",typeDefinition.getName());
        assertEquals("this is a test type",typeDefinition.getDescribe());
        assertTrue(typeDefinition.getRequired());
    }

    @TD(name = "test",required = true,desc = "this is a test type")
    class TestType{

        private String name;
        private boolean required;
        private int age;
        @TD(name = "inner",required = true,desc = "this is a re-write type")
        private InnerType innerType;
    }

    @TD(name = "otherInner",required = true,desc = "this is a inner type")
    class InnerType{
        private String name;
        private boolean required;
        private int age;
    }
}

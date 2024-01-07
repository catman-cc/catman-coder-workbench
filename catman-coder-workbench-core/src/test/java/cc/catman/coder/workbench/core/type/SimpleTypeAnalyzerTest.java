package cc.catman.coder.workbench.core.type;

import org.junit.jupiter.api.Test;

class SimpleTypeAnalyzerTest {

    @Test
    void analyzer() {
        TypeDefinition args = SimpleTypeAnalyzer.of(SimpleTypeAnalyzer.TypeDesc.create()
                .type("anonymous")
                .name("args")
                .add("language", "string")
                .add("expression", "string")
        ).analyzer();
        assert  "anonymous".equals(args.getType().getTypeName());
        assert  "args".equals(args.getName());
        assert args.getType().getPrivateItems().size() == 2;
        assert  "language".equals(args.getType().getPrivateItems().get(0).getName());
        assert  "string".equals(args.getType().getPrivateItems().get(0).getType().getTypeName());
        assert  "expression".equals(args.getType().getPrivateItems().get(1).getName());
        assert  "string".equals(args.getType().getPrivateItems().get(1).getType().getTypeName());
    }
}
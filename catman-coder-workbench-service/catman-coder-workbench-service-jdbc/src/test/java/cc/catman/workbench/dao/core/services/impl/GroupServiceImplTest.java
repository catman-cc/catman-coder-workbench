package cc.catman.workbench.dao.core.services.impl;

import cc.catman.workbench.CatmanCoderWorkbenchApplication;
import cc.catman.workbench.service.core.services.IGroupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.annotation.Resource;

@TestPropertySource(locations = "classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CatmanCoderWorkbenchApplication.class)
class GroupServiceImplTest {

    @Resource
    private IGroupService groupService;

    @Test
    void findById() {

    }

    @Test
    void save() {

    }
}
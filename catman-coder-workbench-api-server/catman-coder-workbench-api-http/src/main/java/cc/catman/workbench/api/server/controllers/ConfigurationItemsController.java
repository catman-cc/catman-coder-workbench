//package cc.catman.workbench.api.server.controllers;
//
//
//import cc.catman.coder.workbench.core.apis.repositories.ConfigurationItem;
//import cc.catman.coder.workbench.core.apis.service.ConfigurationItemsService;
//import org.springframework.web.bind.annotation.*;
//
//import jakarta.annotation.Resource;
//
//@RequestMapping("configuration-items")
//@RestController
//public class ConfigurationItemsController {
//    @Resource
//    private ConfigurationItemsService configurationItemsService;
//
//    @GetMapping("/name/{name}")
//    public ConfigurationItem findByName(@PathVariable  String name){
//        return configurationItemsService.findByName(name).orElse(null);
//    }
//
//    @PostMapping()
//    public ConfigurationItem save(@RequestBody ConfigurationItem item){
//        return configurationItemsService.save(item);
//    }
//}

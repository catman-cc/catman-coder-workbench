package cc.catman.workbench.api.server.controllers;

import cc.catman.coder.workbench.core.serialization.FstMinBinSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/fst")
public class FstMinBinController {

    @GetMapping(value = "/circle")
    public String getCircle() {
        FstMinBinSerializer serializer = new FstMinBinSerializer();
        Map<String, Object> a = new HashMap<>();
        Map<String, Object> b = new HashMap<>();
        a.put("b", b);
        b.put("a", a);
        return new String(Base64.getEncoder().encode(serializer.serialize(a)));
    }
}

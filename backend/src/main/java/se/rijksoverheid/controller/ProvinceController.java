package se.rijksoverheid.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.rijksoverheid.business.ProvinceService;
import se.rijksoverheid.model.Province;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/provinces")
public class ProvinceController {
    private ProvinceService provinceService;

    @GetMapping("")
    public ResponseEntity<List<Province>> getProvinces() {
        return ResponseEntity.ok(provinceService.getProvinces());
    }
}

package se.rijksoverheid.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import se.rijksoverheid.model.Province;
import se.rijksoverheid.model.ProvinceRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class ProvinceService {
    private ProvinceRepository provinceRepository;

    public List<Province> getProvinces() {
        return provinceRepository.findAll();
    }
}

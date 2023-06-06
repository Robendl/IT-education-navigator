package se.rijksoverheid.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.rijksoverheid.model.Province;
import se.rijksoverheid.model.ProvinceRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProvinceServiceTest {

    @Mock
    ProvinceRepository mockProvinceRepository;

    @InjectMocks
    ProvinceService provinceService;

    @Test
    void testGetProvinces() {
        List<Province> provinceList = new ArrayList<>();
        Province mockProvince = mock(Province.class);
        provinceList.add(mockProvince);

        when(mockProvinceRepository.findAll()).thenReturn(provinceList);

        assertEquals(provinceList, provinceService.getProvinces());
    }
}

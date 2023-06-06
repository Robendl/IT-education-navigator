package se.rijksoverheid.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import se.rijksoverheid.business.ProvinceService;
import se.rijksoverheid.model.Province;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProvinceControllerTest {

    @Mock
    ProvinceService mockProvinceService;

    @InjectMocks
    ProvinceController provinceController;

    @Test
    void testGetProvinces() {
        List<Province> provinceList = new ArrayList<>();
        Province mockProvince = mock(Province.class);
        provinceList.add(mockProvince);

        when(mockProvinceService.getProvinces()).thenReturn(provinceList);

        ResponseEntity<List<Province>> responseEntity = provinceController.getProvinces();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(provinceList, responseEntity.getBody());
    }
}

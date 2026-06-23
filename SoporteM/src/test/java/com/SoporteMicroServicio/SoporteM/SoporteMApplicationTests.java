package com.SoporteMicroServicio.SoporteM;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;

import com.SoporteMicroServicio.SoporteM.feign.UsuarioFeignClient;

@SpringBootTest
@ActiveProfiles("test")
class SoporteMApplicationTests {

	@MockitoBean
	private UsuarioFeignClient usuarioFeignClient;

	@Test
	void contextLoads() {
	}

}

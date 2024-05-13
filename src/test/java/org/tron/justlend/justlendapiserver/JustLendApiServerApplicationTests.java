package org.tron.justlend.justlendapiserver;


import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JustLendApiServerApplication.class)
@ActiveProfiles("prod")
public abstract class  JustLendApiServerApplicationTests {
}

package br.com.java.integrationtests.testcontainers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;
import java.util.Map;
import java.util.stream.Stream;


// Initializer é a classe responsável por criar o container do MySql dinamicamente através do testecontainer
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

    //classe estatica para ter apenas uma instancia do testcontainer em execução e não várias instancias de testes.
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:9.1.0");

        // Inicializa uma instância (única) do MySqlContainer
        private static void startContainers() {
            Startables.deepStart(Stream.of(mysql)).join();
        }

        private static Map<String, String> createConnectionConfiguration(){
            return Map.of(
                    "spring.datasource.url", mysql.getJdbcUrl(),
                    "spring.datasource.username", mysql.getUsername(),
                    "spring.datasource.password", mysql.getPassword()
            );
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();

            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource testcontainers = new MapPropertySource("testcontainers",
                    (Map) createConnectionConfiguration());
            environment.getPropertySources().addFirst(testcontainers);
        }

    }
}

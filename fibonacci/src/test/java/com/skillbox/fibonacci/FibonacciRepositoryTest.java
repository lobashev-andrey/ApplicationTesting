package com.skillbox.fibonacci;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FibonacciRepositoryTest extends PostgresTestContainerInitializer {

    @Autowired
    FibonacciRepository repository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    EntityManager entityManager;

    @BeforeAll
    public static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    public static void afterAll() {
        postgres.stop();
    }

    @AfterEach
    public void afterEach() {
        repository.deleteAll();
    }


    @Test
    @DisplayName("when save new number then return new number")
    public void whenSaveNewNumber_thenReturnNewNumber() {
        FibonacciNumber number = new FibonacciNumber(10, 55);
        repository.save(number);
        entityManager.flush();
        entityManager.detach(number);
        List<FibonacciNumber> numbers = jdbcTemplate.query(
                "SELECT * FROM fibonacci_number WHERE index = 10",
                (rs, rowNum) -> new FibonacciNumber(rs.getInt("index"), rs.getInt("value")));
        assertEquals(1, numbers.size());
        assertEquals(number.getIndex(), numbers.get(0).getIndex());
        assertEquals(number.getValue(), numbers.get(0).getValue());
    }


    @Test
    @DisplayName("when find by index then get proper number")
    public void whenFindByIndex_thenReturnProperNumber() {
        jdbcTemplate.execute("INSERT INTO fibonacci_number(index, value) VALUES (10, 55)");

        FibonacciNumber number = repository.findByIndex(10).get();

        assertEquals(10, number.getIndex());
        assertEquals(55, number.getValue());
    }

    @Test
    @DisplayName("when save existing number then no exception thrown and no double data appeared")
    public void whenSaveExistingNumber_thenNoExceptionAndNoDoubleData() {
        jdbcTemplate.execute("INSERT INTO fibonacci_number(index, value) VALUES (10, 55)");
        FibonacciNumber number = new FibonacciNumber(10, 55);

        assertDoesNotThrow(() -> repository.save(number));
        entityManager.flush();
        entityManager.detach(number);

        List<FibonacciNumber> numbers = jdbcTemplate.query(
                "SELECT * FROM fibonacci_number WHERE index = 10",
                (rs, rowNum) -> new FibonacciNumber(rs.getInt("index"), rs.getInt("value")));

        assertEquals(1, numbers.size());
        assertEquals(number.getIndex(), numbers.get(0).getIndex());
        assertEquals(number.getValue(), numbers.get(0).getValue());
    }
}

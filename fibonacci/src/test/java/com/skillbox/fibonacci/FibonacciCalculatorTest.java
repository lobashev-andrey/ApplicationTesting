package com.skillbox.fibonacci;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class FibonacciCalculatorTest {
    FibonacciCalculator calculator = new FibonacciCalculator();
    @Test
    @DisplayName("test get when index less than 1")
    public void givenIndexLessThanOne_testGetFibonacciNumber() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> calculator.getFibonacciNumber(0));
        assertEquals("Index should be greater or equal to 1", ex.getMessage());
    }

    @ParameterizedTest
    @DisplayName("test get when index 1 or 2")
    @ValueSource(ints = {1, 2})
    public void givenIndex1or2_testGetFibonacciNumber(int givenIndex) {
        assertEquals(1, calculator.getFibonacciNumber(givenIndex));
    }

    @ParameterizedTest
    @CsvSource({
            "5, 5",
            "10, 55",
            "30, 832040"})
    @DisplayName("test get when index > 2")
   public void givenIndexGraterThen2_testGetFibonacciNumber(int givenIndex, int expected) {
        assertEquals(expected, calculator.getFibonacciNumber(givenIndex));
    }

    // when data is too big
    @ParameterizedTest
    @CsvFileSource(resources = "/data_for_FibonacciCalculatorTest.csv")
    @DisplayName("test get when index > 2")
    public void givenIndexGraterThen2_testGetFibonacciNumber_withCSVFile(int givenIndex, int expected) {
        assertEquals(expected, calculator.getFibonacciNumber(givenIndex));
    }
}

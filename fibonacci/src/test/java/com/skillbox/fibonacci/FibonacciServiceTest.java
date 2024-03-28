package com.skillbox.fibonacci;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FibonacciServiceTest {

    FibonacciService service;

    @Mock
    private FibonacciRepository repository;

    @Mock
    private FibonacciCalculator calculator;


    @BeforeEach
    public void setUp() {
        service = new FibonacciService(repository, calculator);
    }

    @Test
    public void whenIndexIsInDatabase_thenReturnFromDatabase() {
        int index = 15;
        when(repository.findByIndex(index)).thenReturn(Optional.of(new FibonacciNumber(index, 610)));

        assertEquals(610, service.fibonacciNumber(index).getValue());
        verify(repository, times(1)).findByIndex(index);
        verify(calculator, times(0)).getFibonacciNumber(index);
    }

    @Test
    public void whenIndexNotInDatabase_thenCalculate() {
        int index = 10;
        when(calculator.getFibonacciNumber(index)).thenReturn(55);

        assertEquals(55, service.fibonacciNumber(index).getValue());

        verify(repository, times(1)).findByIndex(index);
        verify(calculator, times(1)).getFibonacciNumber(index);
    }

    @Test
    public void whenIndexLessThan_1_thenThrow() {
        int index = -5;

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.fibonacciNumber(index));
        assertEquals("Index should be greater or equal to 1", ex.getMessage());

        verify(repository, times(0)).findByIndex(index);
        verify(calculator, times(0)).getFibonacciNumber(index);
    }
}

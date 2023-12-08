package com.team1.zeeslag.enumerate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TypeUnitTest {

    @Test
    public void testGetNumericValue() {
        assertEquals(5, Type.CARRIER.getNumericValue());
        assertEquals(4, Type.BATTLESHIP.getNumericValue());
        assertEquals(3, Type.CRUISER.getNumericValue());
        assertEquals(3, Type.SUBMARINE.getNumericValue());
        assertEquals(2, Type.DESTROYER.getNumericValue());
    }
}

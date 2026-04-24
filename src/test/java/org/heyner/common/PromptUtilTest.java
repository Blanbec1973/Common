package org.heyner.common;

import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PromptUtilTest {

    @Test
    void testGetYesOrNoResponseOui() {
        // Arrange
        String input = "y\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        boolean result = PromptUtil.getYesOrNoResponse("Voulez-vous continuer ?");

        // Assert
        assertTrue(result);
    }

    @Test
    void testGetYesOrNoResponseNon() {
        // Arrange
        String input = "n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        boolean result = PromptUtil.getYesOrNoResponse("Voulez-vous continuer ?");

        // Assert
        assertFalse(result);
    }

    @Test
    void testGetYesOrNoResponseInvalidInput() {
        // Arrange
        String input = "peut-être\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        boolean result = PromptUtil.getYesOrNoResponse("Voulez-vous continuer ?");

        // Assert
        assertFalse(result);
    }

    @Test
    void testGetYesOrNoResponseWithNullMessage() {
        assertThrows(IllegalArgumentException.class, () -> PromptUtil.getYesOrNoResponse(null));
    }

    @Test
    void testGetYesOrNoResponseWithEmptyMessage() {
        assertThrows(IllegalArgumentException.class, () -> PromptUtil.getYesOrNoResponse(""));
    }

    @Test
    void testGetYesOrNoResponseWithBlankMessage() {
        assertThrows(IllegalArgumentException.class, () -> PromptUtil.getYesOrNoResponse("   "));
    }
}

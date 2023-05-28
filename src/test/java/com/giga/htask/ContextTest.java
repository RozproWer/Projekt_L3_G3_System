package com.giga.htask;


import com.giga.htask.model.Context;
import com.giga.htask.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContextTest {

    private Context context;

    @BeforeEach
    public void setUp() {
        context = Context.getInstance();
    }

    @Test
    public void testLoginWithValidCredentials() {
        // Arrange
        String email = "example@example.com";
        String password = "password";

        // Act
        User user = context.login(email, password);

        // Assert
        assertNotNull(user);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        // Arrange
        String email = "example@example.com";
        String password = "incorrect_password";

        // Act
        User user = context.login(email, password);

        // Assert
        assertNull(user);
    }

    @Test
    public void testGetLoggedUser() {
        // Arrange
        User user = new User();
        context.setLoggedUser(user);

        // Act
        User loggedUser = context.getLoggedUser();

        // Assert
        assertEquals(user, loggedUser);
    }

    @Test
    public void testSetLoggedUser() {
        // Arrange
        User user = new User();

        // Act
        context.setLoggedUser(user);
        User loggedUser = context.getLoggedUser();

        // Assert
        assertEquals(user, loggedUser);
    }
}

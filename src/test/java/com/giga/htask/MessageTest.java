package com.giga.htask;

import com.giga.htask.model.Message;
import org.junit.Assert;
import org.junit.Test;

public class MessageTest {

    @Test(expected = IllegalArgumentException.class)
    public void testMessageMaxLength() {
        // Tworzenie instancji Message
        Message message = new Message();

        // Przypisanie wiadomości o długości większej niż maksymalna
        String longMessage = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris consectetur metus ut ligula laoreet, in commodo orci aliquam. Morbi vehicula augue et nulla convallis, a fringilla sapien blandit. Aliquam vitae vulputate velit, eu porttitor mauris. Sed vestibulum euismod sem id bibendum. Pellentesque auctor bibendum dui ut tristique.";
        message.setMessage(longMessage);
    }
}
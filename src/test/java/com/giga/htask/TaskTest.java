package com.giga.htask;

import com.giga.htask.model.Task;
import org.junit.Assert;
import org.junit.Test;

public class TaskTest {

    @Test
    public void testDescription() {
        // Tworzenie instancji Task
        Task task = new Task();

        // Przypisanie opisu
        String description = "Lorem ipsum dolor sit amet";
        task.setDescription(description);

        // Pobieranie opisu i porównywanie z przypisanym opisem
        String retrievedDescription = task.getDescription();
        Assert.assertEquals(description, retrievedDescription);
    }
}
package com.derrick.todoapp;

/**
 * Created by derrick.njeru on 8/22/2017.
 */

public class TodoApp {
    private static final TodoApp ourInstance = new TodoApp();

    public static TodoApp getInstance() {
        return ourInstance;
    }

    private TodoApp() {
    }
}

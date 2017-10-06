package com.derrick.util;

/**
 * Created by derrick.njeru on 8/21/2017.
 * Used with the filter spinner in the tasks list.
 */
public enum TasksFilterType {
    /**
     * Do not filter tasks.
     */
    ALL_TASKS,

    /**
     * Filters only the active (not completed yet) tasks.
     */
    ACTIVE_TASKS,

    /**
     * Filters only the completed tasks.
     */
    COMPLETED_TASKS
}

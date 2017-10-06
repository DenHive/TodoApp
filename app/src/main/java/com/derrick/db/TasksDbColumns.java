package com.derrick.db;

import android.provider.BaseColumns;

/**
 * Created by derrick.njeru on 8/21/2017.
 */

public class TasksDbColumns {

    /* Inner class that defines the table columns */
    public static abstract class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_COMPLETED = "completed";
    }
}

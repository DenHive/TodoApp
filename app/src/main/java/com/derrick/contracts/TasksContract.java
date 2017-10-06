package com.derrick.contracts;

import android.support.annotation.NonNull;

import com.derrick.adapters.TasksAdapter;
import com.derrick.model.Task;
import com.derrick.util.BasePresenter;
import com.derrick.util.BaseView;
import com.derrick.util.TasksFilterType;

import java.util.List;

/**
 * Created by derrick.njeru on 8/20/2017.
 */

public interface TasksContract {
    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showTasks(List<Task> tasks);

        void showAddTask();

        void showTaskDetailsUi(String taskId, TasksAdapter.MyViewHolder myViewHolder);

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        void showCompletedTasksCleared();

        void showLoadingTasksError();

        void showNoTasks();

        void showActiveFilterLabel();

        void showCompletedFilterLabel();

        void showAllFilterLabel();

        void showNoActiveTasks();

        void showNoCompletedTasks();

        void showSuccessfullySavedMessage();

        boolean isActive();

        void showFilteringPopUpMenu();

        void showActionMode(String taskId, int position);
    }

    interface Presenter extends BasePresenter {
        void result(int requestCode, int resultCode);

        void loadTasks();

        void addNewTask();

        void openTaskDetails(@NonNull Task requestedTask, TasksAdapter.MyViewHolder myViewHolder);

        void completeTask(@NonNull Task completedTask);

        void activateTask(@NonNull Task activeTask);

        void clearCompletedTasks();

        void setFiltering(TasksFilterType requestType);

        TasksFilterType getFiltering();

        void openActionMode(@NonNull Task longPressedTask, int position);

    }
}

package com.derrick.presenters;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.derrick.activities.AddEditTaskActivity;
import com.derrick.adapters.TasksAdapter;
import com.derrick.contracts.TasksContract;
import com.derrick.db.TasksDbImplementation;
import com.derrick.model.Task;
import com.derrick.util.TasksFilterType;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by derrick.njeru on 8/20/2017.
 */

public class TasksPresenter implements TasksContract.Presenter {
    private TasksContract.View mTasksView;
    private TasksDbImplementation mTasksDbImplementation;
    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;
    private boolean mFirstLoad = true;

    public TasksPresenter(@NonNull TasksDbImplementation tasksDbImplementation, @NonNull TasksContract.View tasksView) {
        mTasksView = checkNotNull(tasksView, "tasksView cannot be null!");
        mTasksDbImplementation = checkNotNull(tasksDbImplementation, "tasksRepository cannot be null");
        tasksView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTasks(true);
    }


    @Override
    public void result(int requestCode, int resultCode) {
// If a task was successfully added, show snackbar
        if (AddEditTaskActivity.REQUEST_ADD_TASK == requestCode && Activity.RESULT_OK == resultCode) {
            mTasksView.showSuccessfullySavedMessage();
        }
    }

    @Override
    public void loadTasks() {
        loadTasks(true);
        mFirstLoad = false;
    }

    @Override
    public void addNewTask() {
        mTasksView.showAddTask();
    }

    @Override
    public void openTaskDetails(@NonNull Task requestedTask, TasksAdapter.MyViewHolder myViewHolder) {
        checkNotNull(requestedTask, "requestedTask cannot be null!");
        mTasksView.showTaskDetailsUi(requestedTask.getId(), myViewHolder);
    }


    @Override
    public void completeTask(@NonNull Task completedTask) {
        checkNotNull(completedTask, "completedTask cannot be null!");
        mTasksDbImplementation.completeTask(completedTask);
        mTasksView.showTaskMarkedComplete();
        loadTasks(false);
    }

    @Override
    public void activateTask(@NonNull Task activeTask) {
        checkNotNull(activeTask, "activeTask cannot be null!");
        mTasksDbImplementation.activateTask(activeTask);
        mTasksView.showTaskMarkedActive();
        loadTasks(false);
    }

    @Override
    public void clearCompletedTasks() {
        mTasksDbImplementation.clearCompletedTasks();
        mTasksView.showCompletedTasksCleared();
        loadTasks(false);
    }

    /**
     * Sets the current task filtering type.
     *
     * @param requestType Can be {@link TasksFilterType#ALL_TASKS},
     *                    {@link TasksFilterType#COMPLETED_TASKS}, or
     *                    {@link TasksFilterType#ACTIVE_TASKS}
     */

    @Override
    public void setFiltering(TasksFilterType requestType) {
        mCurrentFiltering = requestType;
    }

    @Override
    public TasksFilterType getFiltering() {
        return mCurrentFiltering;
    }

    @Override
    public void openActionMode(@NonNull Task longPressedTask,int position) {
        checkNotNull(longPressedTask, "requestedTask cannot be null!");
        mTasksView.showActionMode(longPressedTask.getId(),position);
    }

    /**
     * @param showLoadingUI Pass in true to display a loading progressbar in the UI
     */
    private void loadTasks(final boolean showLoadingUI) {
        if (showLoadingUI) {
            mTasksView.setLoadingIndicator(true);
        }

        mTasksDbImplementation.getTasks(new TasksDbImplementation.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                List<Task> tasksToShow = new ArrayList<Task>();

                // We filter the tasks based on the requestType
                for (Task task : tasks) {
                    switch (mCurrentFiltering) {
                        case ALL_TASKS:
                            tasksToShow.add(task);
                            break;
                        case ACTIVE_TASKS:
                            if (task.isActive()) {
                                tasksToShow.add(task);
                            }
                            break;
                        case COMPLETED_TASKS:
                            if (task.isCompleted()) {
                                tasksToShow.add(task);
                            }
                            break;
                        default:
                            tasksToShow.add(task);
                            break;
                    }
                }
                // The view may not be able to handle UI updates anymore
                if (!mTasksView.isActive()) {
                    return;
                }
                if (showLoadingUI) {
                    mTasksView.setLoadingIndicator(false);
                }

                processTasks(tasksToShow);
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mTasksView.isActive()) {
                    return;
                }
                if (showLoadingUI) {
                    mTasksView.setLoadingIndicator(false);
                }

                mTasksView.showLoadingTasksError();
                processEmptyTasks();
            }
        });
    }

    private void processTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            processEmptyTasks();
        } else {
            // Show the list of tasks
            mTasksView.showTasks(tasks);
            // Set the filter label's text.
            showFilterLabel();
        }
    }

    private void processEmptyTasks() {
        switch (mCurrentFiltering) {
            case ACTIVE_TASKS:
                mTasksView.showNoActiveTasks();
                break;
            case COMPLETED_TASKS:
                mTasksView.showNoCompletedTasks();
                break;
            default:
                mTasksView.showNoTasks();
                break;
        }
    }

    private void showFilterLabel() {
        switch (mCurrentFiltering) {
            case ACTIVE_TASKS:
                mTasksView.showActiveFilterLabel();
                break;
            case COMPLETED_TASKS:
                mTasksView.showCompletedFilterLabel();
                break;
            default:
                mTasksView.showAllFilterLabel();
                break;
        }
    }
}

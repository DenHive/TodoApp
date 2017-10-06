package com.derrick.contracts;

import com.derrick.util.BasePresenter;
import com.derrick.util.BaseView;

/**
 * Created by derrick.njeru on 8/22/2017.
 * <p>
 * This specifies the contract between the view and the presenter.
 */
public interface AddEditTaskContract {

    interface View extends BaseView<Presenter> {

        void showEmptyTaskError();

        void showTasksList();

        void setTitle(String title);

        void setDescription(String description);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void saveTask(String title, String description);

        void populateTask();

        boolean isDataMissing();
    }
}


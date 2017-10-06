package com.derrick.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.derrick.fragments.HomeFragment;
import com.derrick.model.Task;
import com.derrick.todoapp.R;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by derrick.njeru on 8/27/2017.
 */

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyViewHolder> {
    private List<Task> mTasks;
    private HomeFragment.TaskItemListener mItemListener;
    private Context mContext;

    private SparseBooleanArray selectedItems;

    private static int currentSelectedIndex = -1;

    public TasksAdapter(List<Task> tasks, HomeFragment.TaskItemListener itemListener, Context context) {
        setList(tasks);
        mItemListener = itemListener;
        mContext = context;
        selectedItems = new SparseBooleanArray();
    }

    private void setList(List<Task> tasks) {
        mTasks = checkNotNull(tasks);
    }

    public void replaceData(List<Task> tasks) {
        setList(tasks);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Task task = mTasks.get(position);


        // Active/completed task UI
        holder.chkComplete.setChecked(task.isCompleted());
        if (task.isCompleted()) {
            /**
             * strike through the title
             */
            SpannableString contentTitle = new SpannableString(task.getTitleForList());
            contentTitle.setSpan(new StrikethroughSpan(), 0, task.getTitleForList().length(), 0);
            holder.txtTitle.setText(contentTitle);
            /**
             * strike through description
             */
            SpannableString contentDesc = new SpannableString(task.getDescription());
            contentDesc.setSpan(new StrikethroughSpan(), 0, task.getDescription().length(), 0);
            holder.txtDescription.setText(contentDesc);
        } else {
            holder.txtTitle.setText(task.getTitleForList());
            holder.txtDescription.setText(task.getDescription());
        }
        holder.chkComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!task.isCompleted()) {
                    mItemListener.onCompleteTaskClick(task);
                } else {
                    mItemListener.onActivateTaskClick(task);
                }
            }
        });
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemListener.onTaskClick(task, holder);
            }
        });

        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mItemListener.onTaskLongClicked(task, position);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return false;
            }
        });

        setSelectedColor(holder, position, mContext, task.isCompleted());
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtDescription;
        public CheckBox chkComplete;
        public CardView card;
        public RelativeLayout inner_layout;

        public MyViewHolder(View view) {
            super(view);
            txtTitle = (TextView) view.findViewById(R.id.title);
            txtDescription = (TextView) view.findViewById(R.id.description);
            chkComplete = (CheckBox) view.findViewById(R.id.complete);
            card = (CardView) view.findViewById(R.id.card_layout);
            inner_layout = (RelativeLayout) view.findViewById(R.id.inner_layout);
        }
    }

    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void removeData(int position) {
        mTasks.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    private void setSelectedColor(MyViewHolder viewHolder, int position, Context mContext, boolean completed) {
        if (selectedItems.get(position, false) && completed) {
            viewHolder.inner_layout.setBackgroundDrawable(mContext
                    .getResources().getDrawable(R.drawable.strike_through_backgroud));
        } else if (selectedItems.get(position, false) && !completed) {
            viewHolder.card.setCardBackgroundColor(mContext.getResources().getColor(R.color.light_grey));
        } else {
            viewHolder.card.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
    }

}

package tiago.homemanagement;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ShoppingCursorAdapter extends RecyclerView.Adapter<ShoppingCursorAdapter.ViewHolder>  {
    private Context context;
    Cursor cursor = null;
    private View.OnClickListener clickListener = null;
    private int lastTaskClicked = -1;

    public ShoppingCursorAdapter(Context context) {
        this.context = context;
    }

    public void refreshData(Cursor cursor) {
        if (this.cursor != cursor){
            this.cursor = cursor;
            notifyDataSetChanged();
        }
    }

    public void setClickListener(View.OnClickListener clickListener){
        this.clickListener = clickListener;
    }

    public int getLastTaskClicked(){
        return lastTaskClicked;
    }

    @NonNull
    @Override
    public ShoppingCursorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new ShoppingCursorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShoppingCursorAdapter.ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        TaskItem task = TableTasklist.getCurrentTaskItem(cursor);
        holder.setTask(task);
    }

    @Override
    public int getItemCount() {
        if (cursor == null) return 0;
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView texto;
        private TextView check;
        private int taskID;

        public ViewHolder(View itemView) {
            super(itemView);
            texto = (TextView) itemView.findViewById(R.id.recycledText);
            check = (TextView) itemView.findViewById(R.id.check);
        }
        public void setTask(TaskItem task){
            texto.setText(task.getName());
            check.setVisibility(
                    task.isDone() ? View.VISIBLE : View.INVISIBLE
            );
            taskID = task.getId();
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (position == RecyclerView.NO_POSITION)
                return;
            if (clickListener != null) {
                lastTaskClicked = taskID;
                clickListener.onClick(v);
            }
        }
    }
}

package tiago.homemanagement;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FloorCursorAdapter extends RecyclerView.Adapter<FloorCursorAdapter.ViewHolder> {

    Cursor cursor = null;
    private View.OnClickListener clickListener = null;
    private Context context;
    private int lastTaskClicked = -1;

    public FloorCursorAdapter(Context context) {
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_floor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
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
        TextView texto;
        TextView check;
        private int taskID;

        public ViewHolder(View itemView) {
            super(itemView);
            texto = itemView.findViewById(R.id.recycledText);
            check = itemView.findViewById(R.id.check);
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

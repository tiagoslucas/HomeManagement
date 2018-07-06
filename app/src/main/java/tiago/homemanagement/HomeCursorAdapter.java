package tiago.homemanagement;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeCursorAdapter extends RecyclerView.Adapter<HomeCursorAdapter.ViewHolder> {

    private Context context;
    Cursor cursor = null;
    private View.OnClickListener clickListener = null;
    private int lastTaskClicked = -1;

    public HomeCursorAdapter(Context context) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        cursor.moveToPosition(position);
        final TaskItem task = TableTasklist.getCurrentTaskItem(cursor);
        holder.setTask(task);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.check.setVisibility(holder.check.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                task.setDone(task.isDone() ? 0 : 1);
                context.getContentResolver().update(Uri.withAppendedPath(HomeContentProvider.TASKLIST_URI, String.valueOf(task.getId())),
                        TableTasklist.getContentValues(task),
                        null,
                        null);
                if (task.getSettid() == MainActivity.FLOOR_SETTID)
                    task.setDate(System.currentTimeMillis());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cursor == null) return 0;
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView texto;
        private TextView check;

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
        }
    }
}

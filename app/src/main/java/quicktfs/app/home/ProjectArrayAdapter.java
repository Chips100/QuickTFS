package quicktfs.app.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import quicktfs.apiclients.contracts.projects.ProjectDto;
import quicktfs.app.R;

/**
 * Adapter for displaying TFS Projects in a list.
 */
public class ProjectArrayAdapter extends ArrayAdapter<ProjectDto> {
    public ProjectArrayAdapter(@NonNull Context context) {
        super(context, R.layout.list_item_project, R.id.listItemProjectName
        );
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        final ProjectDto item = getItem(position);
        if (item == null) return view;

        // Set texts of project in view.
        TextView nameTextView = (TextView)view.findViewById(R.id.listItemProjectName);
        TextView descriptionTextView = (TextView)view.findViewById(R.id.listItemProjectDescription);

        nameTextView.setText(item.getName());
        descriptionTextView.setText(item.getDescription());
        return view;
    }
}

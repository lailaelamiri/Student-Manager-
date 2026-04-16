package com.example.ivoryledgerii;

import android.content.Context;
import android.view.*;
import android.widget.*;
import com.example.ivoryledgerii.beans.ScholarRecord;
import java.util.List;

public class ScholarRosterAdapter extends ArrayAdapter<ScholarRecord> {

    private Context context;
    private List<ScholarRecord> list;

    public ScholarRosterAdapter(Context context, List<ScholarRecord> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    // ✅ SAFE HELPER
    private String safe(String value) {
        return (value == null || value.isEmpty()) ? "N/A" : value;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_scholar_row, parent, false);
        }

        ScholarRecord s = list.get(position);

        TextView fullNameView    = convertView.findViewById(R.id.scholarFullName);
        TextView cityGenderView  = convertView.findViewById(R.id.scholarCityGender);
        TextView idBadgeView     = convertView.findViewById(R.id.scholarIdBadge);
        TextView initialView     = convertView.findViewById(R.id.scholarInitial);

        String fullName = safe(s.getFamilyName()) + " " + safe(s.getGivenName());

        fullNameView.setText(fullName);
        cityGenderView.setText(safe(s.getHomeCity()) + " · " + safe(s.getGenderTag()));
        idBadgeView.setText("#" + s.getScholarId());

        // Show first letter of family name as avatar initial
        String initial = s.getFamilyName() != null && !s.getFamilyName().isEmpty()
                ? String.valueOf(s.getFamilyName().charAt(0)).toUpperCase()
                : "?";
        initialView.setText(initial);

        return convertView;
    }
}
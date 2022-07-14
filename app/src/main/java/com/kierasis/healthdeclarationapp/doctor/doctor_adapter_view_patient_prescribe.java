package com.kierasis.healthdeclarationapp.doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kierasis.healthdeclarationapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class doctor_adapter_view_patient_prescribe extends RecyclerView.Adapter<doctor_adapter_view_patient_prescribe.ViewHolder> {
    LayoutInflater inflater;
    List<doctor_ext_view_patient_prescribe> cases;
    private OnNoteListener mOnNoteListener;

    public doctor_adapter_view_patient_prescribe(Context ctx, List<doctor_ext_view_patient_prescribe> cases, OnNoteListener onCaseListener){
        this.inflater = LayoutInflater.from(ctx);
        this.cases = cases;
        this.mOnNoteListener = onCaseListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.doctor_activity_view_patient_prescribe_list,parent, false);
        return new ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.caseNumber.setText(cases.get(position).getMedicine());
        holder.songArtists.setText(cases.get(position).getAmount());
        holder.songUrl.setText(cases.get(position).getTime());

        holder.day_num.setText(cases.get(position).getDayNum());
        holder.day_text.setText(cases.get(position).getDayText());

        Picasso.get().load(cases.get(position).getImage()).into(holder.songCoverImage);

        /*
        if(cases.get(position).getTitle().equals("Case 8")){
            holder.cardview_case.setCardBackgroundColor(Color.parseColor("#ef5350"));

        }
         */

    }

    @Override
    public int getItemCount() {
        return cases.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView caseNumber, songArtists, songUrl, day_num, day_text;
        ImageView songCoverImage;
        OnNoteListener onCaseListener;
        CardView cardview_case;
        public ViewHolder(@NonNull View itemView, OnNoteListener onCaseListener) {
            super(itemView);

            caseNumber = itemView.findViewById(R.id.caseNumber);
            songArtists = itemView.findViewById(R.id.caseBarangay);
            songCoverImage = itemView.findViewById(R.id.coverImage);
            songUrl = itemView.findViewById(R.id.caseDate);
            cardview_case = itemView.findViewById(R.id.cardview_case);
            day_num = itemView.findViewById(R.id.day_num);
            day_text = itemView.findViewById(R.id.day_text);
            this.onCaseListener = onCaseListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCaseListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface  OnNoteListener{
        void onNoteClick(int position);
    }
}

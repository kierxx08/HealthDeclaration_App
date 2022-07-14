package com.kierasis.healthdeclarationapp.doctor;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.content.SharedPreferences;

public class doctor_adapter_view_patient_result extends RecyclerView.Adapter<doctor_adapter_view_patient_result.ViewHolder> {
    LayoutInflater inflater;
    List<doctor_ext_view_patient_result> cases;
    private OnNoteListener mOnNoteListener;

    public doctor_adapter_view_patient_result(Context ctx, List<doctor_ext_view_patient_result> cases, OnNoteListener onCaseListener){
        this.inflater = LayoutInflater.from(ctx);
        this.cases = cases;
        this.mOnNoteListener = onCaseListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.doctor_activity_view_patient_list,parent, false);

        return new ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.caseNumber.setText(cases.get(position).getTitle());
        holder.songArtists.setText(cases.get(position).getArtist());
        holder.songUrl.setText(cases.get(position).getSongURL());
        Picasso.get().load(cases.get(position).getCoverImage()).into(holder.songCoverImage);

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
        TextView caseNumber, songArtists, songUrl;
        ImageView songCoverImage;
        CardView cardview_case;
        OnNoteListener onCaseListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onCaseListener) {
            super(itemView);

            caseNumber = itemView.findViewById(R.id.caseNumber);
            songArtists = itemView.findViewById(R.id.caseBarangay);
            songCoverImage = itemView.findViewById(R.id.coverImage);
            songUrl = itemView.findViewById(R.id.caseDate);
            cardview_case = itemView.findViewById(R.id.cardview_case);
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

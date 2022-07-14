package com.kierasis.healthdeclarationapp.nurse;

import android.content.Context;
import android.graphics.Color;
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

public class nurse_adapter_add_member_result extends RecyclerView.Adapter<nurse_adapter_add_member_result.ViewHolder> {
    LayoutInflater inflater;
    List<nurse_ext_add_member_result> cases;
    private OnNoteListener mOnNoteListener;

    public nurse_adapter_add_member_result(Context ctx, List<nurse_ext_add_member_result> cases, OnNoteListener onCaseListener){
        this.inflater = LayoutInflater.from(ctx);
        this.cases = cases;
        this.mOnNoteListener = onCaseListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nurse_activity_add_member_result_list,parent, false);
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
        OnNoteListener onCaseListener;
        CardView cardview_case;
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

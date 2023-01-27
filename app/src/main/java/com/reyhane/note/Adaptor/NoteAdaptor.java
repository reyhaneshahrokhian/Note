package com.reyhane.note.Adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.reyhane.note.AddNewNote;
import com.reyhane.note.R;
import com.reyhane.note.SecondActivity;
import com.reyhane.note.SqlHelper;

import java.util.ArrayList;

public class NoteAdaptor extends RecyclerView.Adapter<NoteAdaptor.ViewHolder> {

    public static ArrayList<Integer> id = new ArrayList<Integer>();
    public static ArrayList<String> titles = new ArrayList<String>();
    public static ArrayList<String> desc = new ArrayList<String>();
    public static ArrayList<Integer> image = new ArrayList<Integer>();
    Context ct;

    public NoteAdaptor(Context ct, ArrayList<Integer> id, ArrayList<String> titles, ArrayList<String> desc, ArrayList<Integer> img) {
        this.ct = ct;
        this.id = id;
        this.titles = titles;
        this.desc = desc;
        this.image = img;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ct).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.TitleText.setText(titles.get(position));
        holder.descriptionText.setText(desc.get(position));
        holder.Image.setImageResource(image.get(position));

        //determine favorite image
        SqlHelper SetImage = new SqlHelper(ct);
        if( SetImage.FavoriteStatus(String.valueOf(id.get(position))) == 1){
            holder.LikeButton.setImageResource(R.drawable.ic_favorite_24);
        }
        else {
            holder.LikeButton.setImageResource(R.drawable.ic_favorite_border_24);
        }

        //Edit Button event
        holder.EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ct, SecondActivity.class);
                intent.putExtra("title", titles.get(position));
                intent.putExtra("description", desc.get(position));
                intent.putExtra("image", image.get(position));
                intent.putExtra("id", String.valueOf(id.get(position)));
                ct.startActivity(intent);
            }
        });

        //Like Button event
        holder.LikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SqlHelper addData = new SqlHelper(ct);
                if(addData.FavoriteStatus(String.valueOf(id.get(position))) == 0){
                    holder.LikeButton.setImageResource(R.drawable.ic_favorite_24);
                    addData.AddToFavorite(String.valueOf(id.get(position)),1);
                }
                else {
                    holder.LikeButton.setImageResource(R.drawable.ic_favorite_border_24);
                    addData.AddToFavorite(String.valueOf(id.get(position)),0);
                }
            }
        });

        //Delete Button event
        holder.DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDialog(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView Image;
        TextView TitleText;
        TextView descriptionText;
        ImageButton EditButton;
        ImageButton DeleteButton;
        ImageButton LikeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Image = itemView.findViewById(R.id.HugImage);
            TitleText = itemView.findViewById(R.id.TitleText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            EditButton = itemView.findViewById(R.id.EditButton);
            DeleteButton = itemView.findViewById(R.id.DeleteButton);
            LikeButton = itemView.findViewById(R.id.LikeButton);
        }
    }
    //Delete item dialog
    public void DeleteDialog(int position){
        AlertDialog.Builder dialog = new AlertDialog.Builder(ct);
        dialog.setTitle("Delete " + titles.get(position) + "?");
        dialog.setMessage("Are you sure you want to delete " + titles.get(position) + "?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SqlHelper removeData = new SqlHelper(ct);
                removeData.DeleteData(String.valueOf(id.get(position)));

                titles.remove(position);
                desc.remove(position);
                image.remove(position);

                notifyItemRemoved(position);
                notifyItemRangeChanged(position, titles.size());
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
        dialog.create().show();
    }
}

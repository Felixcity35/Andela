package tk.felixcity.com.travel;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder>
{
    ArrayList<TravelDeal> deals ;
    private FirebaseDatabase firebaseDatabase ;
    private DatabaseReference reference ;
    private ChildEventListener childEventListener ;
    private ImageView imageDeal ;

    Context context;


    public DealAdapter()
    {

     //   FirebaseUtils.openFbReference("traveldeals",);
        firebaseDatabase = FirebaseUtils.mFirebasedatabase;
        deals = FirebaseUtils.mDeal ;
        reference = FirebaseUtils.mDatabaseRefence;


        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                TravelDeal td = dataSnapshot.getValue(TravelDeal.class);
                Log.d("adapter",td.getTitle());

                td.setId(dataSnapshot.getKey());
                deals.add(td);
                notifyItemInserted(deals.size()-1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference.addChildEventListener(childEventListener);
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();

        View itemType = LayoutInflater.from(context).inflate(R.layout.rvrows,viewGroup,false);

        return new DealViewHolder(itemType);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder dealViewHolder, int i) {

        TravelDeal deal = deals.get(i);

        dealViewHolder.bind(deal);

    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public class DealViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        TextView tvTitle ;
        TextView tvDescription ;
        TextView tvprice ;

        public DealViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvprice = itemView.findViewById(R.id.tvPrice);
            imageDeal = itemView.findViewById(R.id.imageDeals);
            itemView.setOnClickListener(this);
        }

        public void bind(TravelDeal deal)
        {
            tvTitle.setText(deal.getTitle());
            tvDescription.setText(deal.getDescription());
            tvprice.setText(deal.getPrice());
            showImage(deal.getImageUrl());
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            TravelDeal selecteddeals = deals.get(position);

            Intent intent = new Intent(v.getContext(),DealActivity.class);
            intent.putExtra("Deal",selecteddeals);
             v.getContext().startActivity(intent);
        }

        private void showImage(String url) {
            if (url != null && url.isEmpty()==false) {
                Picasso.with(imageDeal.getContext())
                        .load(url)
                        .resize(160, 160)
                        .centerCrop()
                        .into(imageDeal);
            }
        }


    }

}

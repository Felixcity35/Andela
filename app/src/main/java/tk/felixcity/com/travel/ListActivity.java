package tk.felixcity.com.travel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

//    ArrayList<TravelDeal> deals ;
//    private FirebaseDatabase firebaseDatabase ;
//    private DatabaseReference reference ;
//    private ChildEventListener childEventListener ;
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

//        FirebaseUtils.openFbReference("traveldeals");
//        firebaseDatabase = FirebaseUtils.mFirebasedatabase;
//        reference = FirebaseUtils.mDatabaseRefence;
//
//        childEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                TextView txtDeals = findViewById(R.id.tvDeal);
//                TravelDeal td = dataSnapshot.getValue(TravelDeal.class);
//                txtDeals.setText(txtDeals.getText() + "/n" + td.getTitle() );
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
//        reference.addChildEventListener(childEventListener);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater() ;
        inflater.inflate(R.menu.listacivitymenufile,menu);

        MenuItem insertMenu = menu.findItem(R.id.insertdeal);

        if (FirebaseUtils.isAdmin == true)
        {
            insertMenu.setVisible(true);
        }
          else
        {
            insertMenu.setVisible(false);
        }
        return  true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
           switch (item.getItemId())
           {
               case R.id.insertdeal :
                   Intent intent = new Intent(this,DealActivity.class);
                   startActivity(intent);
                   return  true ;
               case R.id.logoutmenu :
                   AuthUI.getInstance()
                           .signOut(this)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               public void onComplete(@NonNull Task<Void> task) {
                                   // ...
                                   FirebaseUtils.attachListener();
                               }
                           });

                   FirebaseUtils.detachListener();
                   return true ;


           }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtils.detachListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseUtils.openFbReference("traveldeals",this);
        RecyclerView recyclerView = findViewById(R.id.rvDeals);
        DealAdapter adapter = new DealAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager dealLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(dealLayoutManager);

        FirebaseUtils.attachListener();
    }

    public void showmenu()
    {
        invalidateOptionsMenu();
    }
}

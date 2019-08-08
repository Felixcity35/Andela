package tk.felixcity.com.travel;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.TAG;

public class FirebaseUtils
{
    public static FirebaseDatabase mFirebasedatabase ;
    public  static DatabaseReference mDatabaseRefence ;
    private  static  FirebaseUtils firebaseUtils ;
    public static FirebaseAuth firebaseAuth ;
    public static FirebaseAuth.AuthStateListener mAuthlistener;
    public static ArrayList<TravelDeal> mDeal ;
    private static final int  RC_SIGN_IN =123 ;
    public static FirebaseStorage mStorage;
    public static StorageReference mStorageRef;
   // private static Activity caller ;
   private static ListActivity caller ;
    public static boolean isAdmin ;


    private FirebaseUtils(){}

    public static void openFbReference(String ref, final ListActivity callerActivity)
    {
        if (firebaseUtils == null)
        {
            firebaseUtils = new FirebaseUtils();

            mFirebasedatabase = FirebaseDatabase.getInstance();
            firebaseAuth = FirebaseAuth.getInstance() ;
            caller = callerActivity ;
            mAuthlistener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() == null)
                    {
                        FirebaseUtils.SignIn();
                    }
                    else
                    {
                        String userId = firebaseAuth.getUid();
                        checkAdmin(userId);
                    }
                    Toast.makeText(callerActivity.getBaseContext(), "welcome back", Toast.LENGTH_SHORT).show();
                }
            };
            connectStorage();

        }
        mDeal = new ArrayList<>();
        mDatabaseRefence = mFirebasedatabase.getReference().child(ref);

    }

    private static void SignIn()
    {


        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

                caller.startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN);



    }

    public static void checkAdmin(String uid)
    {
        FirebaseUtils.isAdmin = false ;

        DatabaseReference ref = mFirebasedatabase.getReference().child("administrators").child(uid);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseUtils.isAdmin = true ;
                caller.showmenu();

              //  Log.d(TAG, "Admin: ");
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
        ref.addChildEventListener(childEventListener);

    }

    public static void attachListener()
    {
        firebaseAuth.addAuthStateListener(mAuthlistener);
    }


    public static void detachListener()
    {
        firebaseAuth.removeAuthStateListener(mAuthlistener);
    }

    public static void connectStorage() {
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference().child("deals_image");
    }
}

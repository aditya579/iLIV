package com.example.women_safety_24.iliv;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity  {

    SharedPreferences prefs = null;
    String email;
    String name;
    private Intent intent;
    GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Build a GoogleSignInClient with the options specified by gso.
       // GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Button bttn = findViewById(R.id.button);
            intent = new Intent(this, MapsActivity.class);
            bttn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(intent);
                }
            });
        prefs = getSharedPreferences("com.example.adityapandey.medfetch", MODE_PRIVATE);
    }

    public void onResume() {
        super.onResume();
        if (prefs.getBoolean("firstrun", true)) {
            setContentView(R.layout.registry_activity);
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if(account==null){

                SignInButton signInButton = findViewById(R.id.sign_in_button);
                signInButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        signIn();

                    }
                });

            }
            prefs.edit().putBoolean("firstrun", false).apply();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else{
            Intent intent1 = new Intent(this,MapsActivity.class);
            startActivity(intent1);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            email = account.getEmail();
            String tokkenId = account.getIdToken();
            name = account.getDisplayName();
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        Intent intent1 = new Intent(this,MapsActivity.class);
        intent1.putExtra("name",name);
        intent1.putExtra("email",email);
        startActivity(intent1);
    }
}

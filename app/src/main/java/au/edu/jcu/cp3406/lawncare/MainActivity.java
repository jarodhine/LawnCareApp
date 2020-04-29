package au.edu.jcu.cp3406.lawncare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText userName;
    EditText userPassword;
    Button login;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);



        userName = findViewById(R.id.editID);
        userPassword = findViewById(R.id.editPassword);
        login = findViewById(R.id.btnLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private void validate() {
        String name = userName.getText().toString();
        String password = userPassword.getText().toString();

        //Return if empty
        if (name.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_details), Toast.LENGTH_SHORT).show();
            return;
        }

        //Check user exist
        if (!db.checkUserExist(name)) {
            Toast.makeText(this, getString(R.string.wrong_user), Toast.LENGTH_SHORT).show();
            return;
        }

        //Authenticate User
        if (!db.checkUserPassword(name, password)) {
            Toast.makeText(this, getString(R.string.wrong_password), Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity();
    }

    private void startActivity() {
        Intent intent;
        if (userName.getText().toString().equals("Admin")) {
            intent = new Intent(MainActivity.this, AdminActivity.class);
        } else {
            intent = new Intent(MainActivity.this, HomeActivity.class);
        }
        intent.putExtra("userID", db.getID(userName.getText().toString()));
        startActivity(intent);
    }
}

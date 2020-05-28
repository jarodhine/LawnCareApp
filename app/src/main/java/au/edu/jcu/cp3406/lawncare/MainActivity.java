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

        //Test users
        db.addUser("Admin", "password", "Admin", "Blank");
        db.addUser("John1234", "password", "John", "123 Fake St");
        db.addUser("Jane1234", "password", "Jane", "123 Real St");

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

        //Check fields are filled
        if (name.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_details), Toast.LENGTH_SHORT).show();
            return;
        }

        //Check user exist in database
        if (!db.checkUserExist(name)) {
            Toast.makeText(this, getString(R.string.wrong_user), Toast.LENGTH_SHORT).show();
            return;
        }

        //Check user password
        if (!db.checkUserPassword(name, password)) {
            Toast.makeText(this, getString(R.string.wrong_password), Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity();
    }

    private void startActivity() {
        Intent intent;
        //Select Admin Activity or User Activity based on username
        if (userName.getText().toString().equals("Admin")) {
            intent = new Intent(MainActivity.this, AdminActivity.class);
        } else {
            intent = new Intent(MainActivity.this, HomeActivity.class);
        }
        intent.putExtra("userID", db.getID(userName.getText().toString()));
        startActivity(intent);
    }
}

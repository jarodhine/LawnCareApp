package au.edu.jcu.cp3406.lawncare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class HelpActivity extends AppCompatActivity {

    PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        pdfView = findViewById(R.id.pdfView);
        pdfView.fromAsset("maintain_your_lawn_mower.pdf").load();
    }
}

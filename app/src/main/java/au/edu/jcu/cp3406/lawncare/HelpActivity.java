package au.edu.jcu.cp3406.lawncare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

import java.util.Locale;

public class HelpActivity extends AppCompatActivity {

    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        String default_language = Locale.getDefault().getDisplayLanguage();
        String asset_name = "maintain_your_lawn_mower.pdf";

        //Select pdf based on device language
        switch (default_language){
            case "English":
                asset_name = "maintain_your_lawn_mower.pdf";
                break;
            case "Deutsch":
                asset_name = "maintain_your_lawn_mower_german.pdf";
                break;
            case "italiano":
                asset_name = "maintain_your_lawn_mower_italian.pdf";
                break;
            case "日本語":
                asset_name = "maintain_your_lawn_mower_japanese.pdf";
                System.out.println("TESTING JAPANESE");
                break;
            case "中文":
                asset_name = "maintain_your_lawn_mower_mandarin.pdf";
                break;
            case "ਪੰਜਾਬੀ":
                asset_name = "maintain_your_lawn_mower_punjabi.pdf";
                break;
        }

        pdfView = findViewById(R.id.pdfView);
        pdfView.fromAsset(asset_name).load();
    }
}

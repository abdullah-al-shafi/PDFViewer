package shafi.sbf.pdfviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private PDFView pdfView;
    ProgressBar progress_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pdfView = findViewById(R.id.pdfView);
        progress_dialog = findViewById(R.id.progress_dialog);

        String pdfUrl = "https://usus.org.bd/ictbook/ict%20book.pdf";

        // Create an Executor for background tasks
        Executor backgroundExecutor = Executors.newSingleThreadExecutor();

        // Execute the task in the background
        backgroundExecutor.execute(() -> {
            try {
                InputStream inputStream = fetchPdfFromUrl(pdfUrl);

                // Update the UI on the main thread
                runOnUiThread(() -> {
                    // Process the inputStream or update the UI as needed
                    // For example, you can set the PDF content to a TextView
                    // textView.setText(processInputStream(inputStream));

                    pdfView.fromStream(inputStream)
                            .enableSwipe(true) // allows to block changing pages using swipe
                            .swipeHorizontal(false)
                            .enableDoubletap(true)
                            .defaultPage(0)
                            .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                            .password(null)
                            .scrollHandle(null)
                            .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                            // spacing between pages in dp. To define spacing color, set view background
                            .spacing(0)
                            .onLoad(new OnLoadCompleteListener() {
                                @Override
                                public void loadComplete(int nbPages) {
                                    progress_dialog.setVisibility(View.GONE);
                                }
                            })
                            .load();




                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }

    private InputStream fetchPdfFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return connection.getInputStream();
        }
        return null;
    }
}
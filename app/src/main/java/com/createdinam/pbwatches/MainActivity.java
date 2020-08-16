package com.createdinam.pbwatches;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.createdinam.pbwatches.Adapter.Adapter;
import com.createdinam.pbwatches.Adapter.ExcelAdapter;
import com.createdinam.pbwatches.Adapter.ScannedAdapter;
import com.createdinam.pbwatches.Extras.ExcelUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    List<String> storyTitle, storyContent, thumbImages;
    RecyclerView recyclerView, savedListView;
    Adapter adapter;
    ProgressBar progressBar;
    TextView wait;
    File file = null;
    Uri path_uri;
    FloatingActionButton fab_barcode_scanner;
    String path = "", uri = "";
    List<String> mList = new ArrayList<>();

    // read excel attribute
    public static final String TAG = DocumentList.class.getSimpleName();
    private Context mContext;
    private int FILE_SELECTOR_CODE = 10000;
    private int DIR_SELECTOR_CODE = 20000;
    private List<Map<Integer, Object>> readExcelList = new ArrayList<>();
    private ExcelAdapter excelAdapter;
    private ScannedAdapter scannedAdapter;
    // display data
    ImageView iv_display_complete_list;
    TextView tv_file_name, tv_no_list_found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init
        init();
        // load data
        Intent intent_data = getIntent();
        if (intent_data.getStringExtra("file_found").equalsIgnoreCase("true")) {
            uri = intent_data.getStringExtra("file_path");
            path_uri = Uri.parse(uri);
            try {
                if (path_uri != null) {
                    importExcelDeal(path_uri);
                } else {
                    Toast.makeText(this, "No File Found", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "File Not Valid", Toast.LENGTH_SHORT).show();
        }
        showData();
        // scan code
        fab_barcode_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                scanBarCode();
            }
        });
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView);
        savedListView = findViewById(R.id.savedListView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        savedListView.setLayoutManager(linearLayoutManager);
        iv_display_complete_list = findViewById(R.id.iv_display_complete_list);
        tv_file_name = findViewById(R.id.tv_file_name);
        tv_no_list_found = findViewById(R.id.tv_no_list_found);
        fab_barcode_scanner = findViewById(R.id.fab_barcode_scanner);
        progressBar = findViewById(R.id.progressBar);
        wait = findViewById(R.id.wait);
        mList.clear();
        storyTitle = new ArrayList<>();
        storyContent = new ArrayList<>();
        thumbImages = new ArrayList<>();
        // display saved list
        iv_display_complete_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void showData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(storyTitle, this);
        recyclerView.setAdapter(adapter);
    }

    private void scanBarCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
        intentIntegrator.setCaptureActivity(CaptuerScanerAct.class);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Scanning Code");
        intentIntegrator.initiateScan();
    }

    private void importExcelDeal(final Uri uri) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Log.i(TAG, "doInBackground: Importing...");

                List<Map<Integer, Object>> readExcelNew = ExcelUtil.readExcelNew(MainActivity.this, uri, uri.getPath());

                Log.i(TAG, "onActivityResult:readExcelNew " + ((readExcelNew != null) ? readExcelNew.size() : ""));

                if (readExcelNew != null && readExcelNew.size() > 0) {
                    readExcelList.clear();
                    readExcelList.addAll(readExcelNew);
                    updateUI();
                    Log.i(TAG, "run: successfully imported");
                } else {
                    Log.i(TAG, "no data");
                }
            }
        }.start();
    }

    /**
     * refresh RecyclerView
     */

    private void updateUI() {
        if (readExcelList != null && readExcelList.size() > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Stuff that updates the UI
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    excelAdapter = new ExcelAdapter(readExcelList, "#fafbfc");
                    recyclerView.setAdapter(excelAdapter);

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String code_recived = "";
            if (result.getContents() != null) {
                code_recived = result.getContents();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                builder.setTitle("Code Found");
                final String F_Code_recived = code_recived;
                builder.setPositiveButton("Match Code", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        matchBarCode(F_Code_recived);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                Toast.makeText(this, "Code Not Valid", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void matchBarCode(String code_recived) {
        for (int i = 0; i < readExcelList.size(); i++) {
            Log.e("data_have", code_recived + " > " + readExcelList.get(i).values() + " :" + readExcelList.get(i).values().contains(code_recived));
            if (readExcelList.get(i).values().contains(code_recived)) {
                mList.add(readExcelList.get(i).values().toString());
                Toast.makeText(this, "Code Found " + mList.size(), Toast.LENGTH_SHORT).show();
                readExcelList.remove(i);
                adapter.notifyDataSetChanged();
                recyclerView.getAdapter().notifyItemRemoved(i);
            } else {
                if (mList.size() == 0) {
                    Toast.makeText(this, "Scanned Code Not Found In This List!", Toast.LENGTH_SHORT).show();
                } else {
                    for (int k = 0; k < mList.size(); k++) {
                        Log.d("saved_list", code_recived + " > " + mList.get(k).contains(code_recived));
                        if (mList.get(k).contains(code_recived)) {
                            // Toast.makeText(this, "Already Scanned!", Toast.LENGTH_SHORT).show();
                            Log.d("Yup", "Already Scanned!");
                        } else {
                            Log.d("Ops", "Scanned Code Not Found In This List!");
                        }
                        savedAdapter();
                    }
                }
            }
        }
    }

    private void savedAdapter() {
        if (mList.size() != 0 || mList.size() < 0) {
            savedListView.setVisibility(View.VISIBLE);
            tv_no_list_found.setVisibility(View.GONE);
            scannedAdapter = new ScannedAdapter(mList, this);
            savedListView.setAdapter(scannedAdapter);
        } else {
            savedListView.setVisibility(View.GONE);
            tv_no_list_found.setVisibility(View.VISIBLE);
        }
    }
}
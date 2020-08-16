package com.createdinam.pbwatches;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.createdinam.pbwatches.Adapter.AttachmentAdapter;
import com.createdinam.pbwatches.Adapter.ExcelAdapter;
import com.createdinam.pbwatches.Extras.ExcelUtil;
import com.createdinam.pbwatches.Model.AttachmentModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;

import com.createdinam.pbwatches.Extras.FileUtils;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DocumentList extends AppCompatActivity {
    public static final String TAG = DocumentList.class.getSimpleName();
    private Context mContext;
    private int FILE_SELECTOR_CODE = 10000;
    private int DIR_SELECTOR_CODE = 20000;
    private List<Map<Integer, Object>> readExcelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ExcelAdapter excelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_list);
        initViews();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.excel_content_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.import_excel_btn:
                openFileSelector();
                break;

            case R.id.export_excel_btn:
                if (readExcelList.size() > 0) {
                    openFolderSelector();
                } else {
                    Toast.makeText(mContext, "please import excel first", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * open local filer to select file
     */
    private void openFileSelector() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/*");
        startActivityForResult(intent, FILE_SELECTOR_CODE);
    }

    /**
     * open the local filer and select the folder
     */
    private void openFolderSelector() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("application/*");
        intent.putExtra(Intent.EXTRA_TITLE,
                System.currentTimeMillis() + ".xlsx");
        startActivityForResult(intent, DIR_SELECTOR_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECTOR_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri == null) return;
            Log.i(TAG, "onActivityResult: " + "filePath：" + uri.getPath());
            //select file and import
            // importExcelDeal(uri);
            Intent readExcel = new Intent(DocumentList.this, MainActivity.class);
            readExcel.putExtra("file_found", "true");
            readExcel.putExtra("file_path", uri.toString());
            startActivity(readExcel);
        } else if (requestCode == DIR_SELECTOR_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri == null) return;
            Log.i(TAG, "onActivityResult: " + "filePath：" + uri.getPath());
            Toast.makeText(mContext, "Exporting...", Toast.LENGTH_SHORT).show();
            //you can modify readExcelList, then write to excel.
            ExcelUtil.writeExcelNew(this, readExcelList, uri);
        }
    }

    private void importExcelDeal(final Uri uri) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Log.i(TAG, "doInBackground: Importing...");

                List<Map<Integer, Object>> readExcelNew = ExcelUtil.readExcelNew(DocumentList.this, uri, uri.getPath());

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
                    excelAdapter = new ExcelAdapter(readExcelList,"fafbfc");
                    recyclerView.setAdapter(excelAdapter);

                }
            });
        }
    }

}
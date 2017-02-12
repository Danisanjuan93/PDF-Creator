package com.example.danisj.docapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private final static String NOMBRE_CARPETA_APP = "com.example.danisj.docapp";
    private final static String GENERADOS = "MisArchivos";
    private String fileName = "dietaPacientes";
    private Document document = new Document();
    private PdfPTable table = new PdfPTable(2);
    private File outputFile;
    private EditText floorNumber, blockNumber, firstPlate, secondPlate, portion, dessert;
    private Spinner day, time;
    private ArrayAdapter<String> adapter;
    private List<EditText> editTextList;
    private int numberOfFile = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFields();
        createEditTextList();
        initSpinners();

        findViewById(R.id.btnGenerar).setOnClickListener(this);
        findViewById(R.id.saveData).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSaveButton();
            }
        });
        findViewById(R.id.closePdf).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickExitButton();
            }
        });
    }

    private void initSpinners() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"});
        day.setAdapter(adapter);
        day.setEnabled(false);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Desayuno", "Almuerzo", "Cena"});
        time.setAdapter(adapter);
        time.setEnabled(false);
    }

    private void initFields() {
        floorNumber = (EditText) findViewById(R.id.floorNumber);
        blockNumber = (EditText) findViewById(R.id.blockNumber);
        firstPlate = (EditText) findViewById(R.id.firstPlate);
        secondPlate = (EditText) findViewById(R.id.secondPlate);
        portion = (EditText) findViewById(R.id.portion);
        dessert = (EditText) findViewById(R.id.dessert);
        day = (Spinner) findViewById(R.id.spinnerDay);
        time = (Spinner) findViewById(R.id.spinnerHourOfFood);
        table.setWidthPercentage(80);
        editTextList = new ArrayList<>();
    }

    private void createEditTextList() {
        editTextList.add(floorNumber);
        editTextList.add(blockNumber);
        editTextList.add(firstPlate);
        editTextList.add(secondPlate);
        editTextList.add(portion);
        editTextList.add(dessert);
    }

    private void onClickExitButton() {
        try {
            table.addCell("");
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        showPdfFile(outputFile.getAbsolutePath());
    }

    private void onClickSaveButton() {
        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.addElement(new Paragraph(floorNumber.getText().toString() + "                                      " + blockNumber.getText().toString(), new Font(2, 14, 1)));
        pdfPCell.addElement(new Paragraph("               " + day.getSelectedItem().toString() + "/" + time.getSelectedItem().toString(), new Font(2, 14, 1)));
        pdfPCell.addElement(new Paragraph("\n"));
        pdfPCell.addElement(new Paragraph("               " + firstPlate.getText().toString(), new Font(2, 14, 1)));
        pdfPCell.addElement(new Paragraph("\n"));
        pdfPCell.addElement(new Paragraph("               " + secondPlate.getText().toString(), new Font(2, 14, 1)));
        pdfPCell.addElement(new Paragraph("\n"));
        if (portion.getText().length() != 0) {
            pdfPCell.addElement(new Paragraph("               " + "c/" + portion.getText().toString(), new Font(2, 14, 1)));
            pdfPCell.addElement(new Paragraph("\n"));
        }
        pdfPCell.addElement(new Paragraph("                 " + dessert.getText().toString(), new Font(2, 14, 1)));
        pdfPCell.addElement(new Paragraph("\n\n\n"));
        table.addCell(pdfPCell);
        Toast.makeText(getApplicationContext(), "Se han guardado con exito los datos del paciente", Toast.LENGTH_SHORT).show();
        clearFields();
    }

    private void clearFields() {
        for (int i = 0; i < editTextList.size(); i++) {
            editTextList.get(i).getText().clear();
        }
    }

    @Override
    public void onClick(View v) {
        String tarjetaSD = Environment.getExternalStorageDirectory().toString();
        File pdfDir = new File(tarjetaSD + File.separator + NOMBRE_CARPETA_APP);

        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }

        File subDir = new File(pdfDir.getPath() + File.separator + GENERADOS);
        if (!subDir.exists()) {
            subDir.mkdir();
        }

        String fullPath = Environment.getExternalStorageDirectory() + File.separator + NOMBRE_CARPETA_APP +
                File.separator + GENERADOS + File.separator;


        outputFile = new File(fullPath + fileName + numberOfFile + ".pdf");

        if (outputFile.exists()){
            while (outputFile.exists()){
                numberOfFile += 1;
                outputFile = new File(fullPath + fileName + numberOfFile + ".pdf");
            }
        }

        try {
            FileOutputStream ficheroPdf = new FileOutputStream(outputFile.getAbsolutePath());

            PdfWriter.getInstance(document, ficheroPdf);

            document.open();
            document.newPage();

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }

        setEnabledFields();
    }

    private void setEnabledFields() {
        findViewById(R.id.btnGenerar).setEnabled(false);
        findViewById(R.id.floorNumber).setEnabled(true);
        findViewById(R.id.blockNumber).setEnabled(true);

        findViewById(R.id.firstPlate).setEnabled(true);
        findViewById(R.id.secondPlate).setEnabled(true);
        findViewById(R.id.portion).setEnabled(true);
        findViewById(R.id.dessert).setEnabled(true);

        findViewById(R.id.spinnerDay).setEnabled(true);
        findViewById(R.id.spinnerHourOfFood).setEnabled(true);

        findViewById(R.id.saveData).setEnabled(true);
        findViewById(R.id.closePdf).setEnabled(true);
    }

    public void showPdfFile(String fileName){

        File file = new File(fileName);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            this.startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No Application Available to View PDF", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
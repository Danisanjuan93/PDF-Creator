package com.example.danisj.docapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private final static String NOMBRE_CARPETA_APP = "com.example.danisj.docapp";
    private final static String GENERADOS = "MisArchivos";
    private Document document = new Document();
    private PdfPTable table = new PdfPTable(2);
    private EditText floorNumber, blockNumber, firstPlate, secondPlate, portion, dessert;
    private Spinner day, time;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFields();
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
    }

    private void onClickExitButton() {
        try {
            table.addCell("");
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void onClickSaveButton() {
        table.addCell(floorNumber.getText() + "                                     " + blockNumber.getText() +
                "               " + day.getSelectedItem().toString() + "/" + time.getSelectedItem().toString() + "\n\n\n" +
                "               " + firstPlate.getText() + "\n\n" +
                "               " + secondPlate.getText() + "\n\n" +
                "               " + "c/" + portion.getText() + "\n\n" +
                "               " + dessert.getText() + "\n\n\n\n\n\n\n");
    }

    @Override
    public void onClick(View v) {
        System.out.println("Evento lanzado");
        String fileName = "MiArchivoPDF.pdf";
        String tarjetaSD = Environment.getExternalStorageDirectory().toString();
        File pdfDir = new File(tarjetaSD + File.separator + NOMBRE_CARPETA_APP);

        if (!pdfDir.exists()){
            pdfDir.mkdir();
            System.out.println("Directorio creado");
        }

        File subDir = new File(pdfDir.getPath() + File.separator + GENERADOS);
        if (!subDir.exists()){
            subDir.mkdir();
            System.out.println("SubDirectorio creado");
        }

        String fullPath = Environment.getExternalStorageDirectory() + File.separator + NOMBRE_CARPETA_APP +
                File.separator + GENERADOS + File.separator + fileName;

        File outputFile = new File(fullPath);

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
}
/*package com.example.danisj.docapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private final static String NOMBRE_CARPETA_APP = "com.example.danisj.docapp";
    private final static String GENERADOS = "MisArchivos";
    private Document document = new Document();
    private PdfPTable table = new PdfPTable(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        table.setWidthPercentage(80);
        final EditText editText = (EditText) findViewById(R.id.blockNumber);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                table.addCell(addCell());
                /*table.addCell("Floor                              Block\n           Test1\n\n\n" +
                        "\n\n" +
                        "\n\n" + "Test2" +  "\n\n\n\n\n\n\n"
                + "Test3" + "\n\n\n" + editText.getText());
}
        });
        Button exitButton = (Button) findViewById(R.id.closeFile);
        exitButton.setOnClickListener(new OnClickListener() {
@Override
public void onClick(View v) {
        try {
        table.addCell("");
        document.add(table);
        } catch (DocumentException e) {
        e.printStackTrace();
        }
        document.close();
        }
        });
        findViewById(R.id.btnGenerar).setOnClickListener(this);
        }

@Override
public void onClick(View v) {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
        String estado = Environment.getExternalStorageState();
        boolean sdDisponible = false;
        boolean sdAccesoEscritura = false;
        if (estado.equals(Environment.MEDIA_MOUNTED))
        {
        sdDisponible = true;
        sdAccesoEscritura = true;
        }
        else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
        sdDisponible = true;
        sdAccesoEscritura = false;
        }
        else
        {
        sdDisponible = false;
        sdAccesoEscritura = false;
        }

        System.out.println("Esta disponible: " + sdDisponible);
        System.out.println("Se puede escribir: " + sdAccesoEscritura);


        String fileName = "MiArchivoPDF.pdf";
        String tarjetaSD = Environment.getExternalStorageDirectory().toString();
        File pdfDir = new File(tarjetaSD + File.separator + NOMBRE_CARPETA_APP);

        if (!pdfDir.exists()){
        pdfDir.mkdir();
        }else{
        System.out.println("EXISTE EL DIRECTORIO");
        }

        File subDir = new File(pdfDir.getPath() + File.separator + GENERADOS);
        if (!subDir.exists()){
        subDir.mkdir();
        }else{
        System.out.println("EXISTE EL SUBDIRECTORIO");
        }


        String fullPath = Environment.getExternalStorageDirectory() + File.separator + NOMBRE_CARPETA_APP +
        File.separator + GENERADOS + File.separator + fileName;

        File outputFile = new File(fullPath);

        try {
        FileOutputStream ficheroPdf = new FileOutputStream(outputFile.getAbsolutePath());

        PdfWriter.getInstance(document, ficheroPdf);

        document.open();

        } catch (DocumentException | FileNotFoundException e) {
        e.printStackTrace();
        }
        }

private PdfPCell addCell() {
        return new PdfPCell(new Phrase("Hola"));
        }
        }
        */
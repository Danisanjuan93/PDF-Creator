package com.example.danisj.docapp;

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

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfImportedPage;
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
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                table.addCell("Floor                              Block\n           Test1\n\n\n" +
                        "\n\n" +
                        "\n\n" + "Test2" +  "\n\n\n\n\n\n\n"
                + "Test3" + "\n\n\n");
            }
        });
        Button exitButton = (Button) findViewById(R.id.closeFile);
        exitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
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
/*        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);*/
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
            document.add(new Paragraph("Titulo 1"));

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}

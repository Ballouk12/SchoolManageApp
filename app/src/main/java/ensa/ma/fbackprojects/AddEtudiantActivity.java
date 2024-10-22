package ensa.ma.fbackprojects;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ensa.ma.fbackprojects.adapter.EtudiantAdapter;
import ensa.ma.fbackprojects.beans.Etudiant;
import ensa.ma.fbackprojects.service.EtudiantService;

public class AddEtudiantActivity extends AppCompatActivity {
    private EditText etNom, etPrenom, etVille;
    Spinner etSexe ;
    private Button btnSave;
    private static final int PICK_IMAGE_REQUEST = 1;
    private CircleImageView circleImageView;
    private Bitmap selectedBitmap;


    public String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_etudiant);

        etNom = findViewById(R.id.etNom);
        etPrenom = findViewById(R.id.etPrenom);
        etVille = findViewById(R.id.etVille);
        etSexe = (Spinner) findViewById(R.id.etSexe);
        btnSave = findViewById(R.id.btnSave);
        circleImageView = findViewById(R.id.circleImageView);
        circleImageView.setOnClickListener(v -> openImageChooser());


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = etNom.getText().toString();
                String prenom = etPrenom.getText().toString();
                String ville = etVille.getText().toString();
                String sexe = etSexe.getSelectedItem().toString();
                String imageStr = null ;
                circleImageView.setDrawingCacheEnabled(true);
                circleImageView.buildDrawingCache();
                Bitmap selectedBitmap = circleImageView.getDrawingCache();

                if (selectedBitmap != null) {
                     imageStr = encodeImageToBase64(selectedBitmap);
                    Log.d("imgload", "onClick: "+imageStr);
                    // Envoyer les imageBytes via Volley pour stockage dans la base de données
                }
                if (!nom.isEmpty() && !prenom.isEmpty() && !ville.isEmpty() && !sexe.isEmpty()) {
                    // Créer un nouvel étudiant et l'ajouter
                    Etudiant etudiant = new Etudiant(0,nom, prenom,  sexe ,ville,imageStr);
                   EtudiantService.getInstance().create(etudiant);  // Ajoutez la méthode addEtudiant dans le service
                    Toast.makeText(AddEtudiantActivity.this, "Étudiant ajouté avec succès", Toast.LENGTH_SHORT).show();
                    finish(); // Fermer l'activity et revenir à la liste
                } else {
                    Toast.makeText(AddEtudiantActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                circleImageView.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

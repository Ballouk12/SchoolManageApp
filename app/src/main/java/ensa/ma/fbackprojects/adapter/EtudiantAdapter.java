package ensa.ma.fbackprojects.adapter;


import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ensa.ma.fbackprojects.R;
import ensa.ma.fbackprojects.beans.Etudiant;
import ensa.ma.fbackprojects.service.EtudiantService;

public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder> implements Filterable {
    private static final String TAG = "StarAdapter";
    private List<Etudiant> students; // Liste originale
    private List<Etudiant> studentsFilter; // Liste filtrée
    private Context context;
    private NewFilter mFilter;

    public EtudiantAdapter(Context context, List<Etudiant> students) {
        this.context = context;
        this.students = students;
        this.studentsFilter = new ArrayList<>(students); // Initialise avec tous les éléments
        this.mFilter = new NewFilter();
    }

    @NonNull

    public String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    public EtudiantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.student_item, viewGroup, false);
        final EtudiantViewHolder holder = new EtudiantViewHolder(v);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer la vue pour l'édition
                View popup = LayoutInflater.from(context).inflate(R.layout.student_edit_item, null, false);
                Spinner spinnerSexe = popup.findViewById(R.id.spinner_sexe);
                ArrayAdapter<CharSequence> stradapter = ArrayAdapter.createFromResource(context, R.array.sexe, android.R.layout.simple_spinner_item);
                stradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSexe.setAdapter(stradapter);

                final TextView idss = popup.findViewById(R.id.idss);
                  EditText nom = popup.findViewById(R.id.nomUp);
                  EditText prenom = popup.findViewById(R.id.prenomUp);
                  EditText ville = popup.findViewById(R.id.villeUp);
                  CircleImageView circleImageView;
                 Bitmap selectedBitmap;
                circleImageView = popup.findViewById(R.id.imgUp);
                //circleImageView.setOnClickListener(v -> openImageChooser());



                // Récupérer les informations de l'item cliqué
                Etudiant student = studentsFilter.get(holder.getAdapterPosition());
                //===================== Attention ==========================
                //Glide.with(context)
                  //      .load(student.getImg())
                    //    .into(img);
                //===========================================================
                idss.setText(String.valueOf(student.getId()));
                nom.setText(String.valueOf(student.getNom()));
                prenom.setText(String.valueOf(student.getPrenom()));
                circleImageView.setImageBitmap(decodeBase64ToBitmap(student.getImg()));
                String sexe = student.getSexe();
                String image = student.getImg();

                if (sexe != null) {
                    int spinnerPosition = stradapter.getPosition(sexe);
                    spinnerSexe.setSelection(spinnerPosition);
                }
                ville.setText(String.valueOf(student.getVille()));

                // Créer et afficher la boîte de dialogue
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Moudifier :")
                        .setMessage("Vous pouvez modifier les props:")
                        .setView(popup)
                        .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newNom = nom.getText().toString() ;
                                String newPrenom = prenom.getText().toString();
                                String newSexe = spinnerSexe.getSelectedItem().toString();
                                String newVille = ville.getText().toString();
                                String newimage = image ;
                                //String imageStr = null ;
                                //circleImageView.setDrawingCacheEnabled(true);
                                //circleImageView.buildDrawingCache();
                                //Bitmap selectedBitmap = circleImageView.getDrawingCache();


                                int studentId = Integer.parseInt(idss.getText().toString());
                                // Mettre à jou les info de etudiant
                                Etudiant student = new Etudiant(studentId,newNom,newPrenom,newSexe,newVille,newimage);
                               EtudiantService.getInstance().update(student);
                                Log.d("ups", "onClickm sur update: "+student.toString());

                                // Notifiez l'adapter pour rafraîchir l'affichage de l'item modifié
                                notifyItemChanged(holder.getAdapterPosition());
                            }
                        })
                        .setNegativeButton("Annuler", null)
                        .create();
                dialog.show();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EtudiantViewHolder EtudiantViewHolder, int i) {
        Log.d(TAG, "onBindView call! Position: " + i);
        Etudiant student = studentsFilter.get(i); // Utilisez starsFilter pour afficher

        // Utilisation avec Glide
        String base64Image = student.getImg(); // Récupérer l'image encodée
        Bitmap bitmap = decodeBase64ToBitmap(base64Image);

        if (bitmap != null) {
            Glide.with(context)
                    .load(bitmap)
                    .apply(new RequestOptions().override(100, 100))
                    .into(EtudiantViewHolder.img);

        } else {
           Glide.with(context)
                    .asBitmap()
                    .load(R.drawable.imgg)
                    .apply(new RequestOptions().override(100, 100))
                    .into(EtudiantViewHolder.img);
        }
// Charger le bitmap avec Glide

        EtudiantViewHolder.name.setText(student.getNom().toUpperCase());
        EtudiantViewHolder.idss.setText(String.valueOf(student.getId()));
        EtudiantViewHolder.prenom.setText(student.getPrenom().toUpperCase());

        EtudiantViewHolder.ville.setText(student.getVille());
        EtudiantViewHolder.sexe.setText(String.valueOf(student.getSexe()));


    }

    public Bitmap decodeBase64ToBitmap(String encodedImage) {
        if (encodedImage == null || encodedImage.isEmpty()) {
            Log.e("DecodeImage", "Encoded image string is null or empty");
            return null; // Retournez null ou une image par défaut
        }

        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }



    @Override
    public int getItemCount() {
        return studentsFilter.size(); // Retourne la taille de la liste filtrée
    }

    public List<Etudiant> getStudentsFilter() {
        return studentsFilter;
    }

    public List<Etudiant> getStudents() {
        return students;
    }

    @Override
    public Filter getFilter() {
        return mFilter; // Retourne le filtre
    }

    public void updateData(List<Etudiant> newStudents) {
        this.students.clear();
        studentsFilter.clear();
        this.students.addAll(newStudents);
        studentsFilter.addAll(newStudents);
        notifyDataSetChanged(); // Notify that the data has changed
    }

    public class EtudiantViewHolder extends RecyclerView.ViewHolder {
        TextView idss;
        ImageView img;
        TextView name;
        TextView sexe ;
        TextView prenom ;
        TextView ville;
        RelativeLayout parent;

        public EtudiantViewHolder(@NonNull View itemView) {
            super(itemView);
            idss = itemView.findViewById(R.id.ids);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            prenom = itemView.findViewById(R.id.prenom);
            ville = itemView.findViewById(R.id.ville);
            sexe = itemView.findViewById(R.id.sexe);
            parent = itemView.findViewById(R.id.parent);
        }
    }

    private class NewFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Etudiant> filteredList = new ArrayList<>();
            final FilterResults results = new FilterResults();

            // Vérifiez si la chaîne de recherche est vide
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(students); // Ajouter tous les éléments si aucune recherche
            } else {
                final String filterPattern = charSequence.toString().toLowerCase().trim(); // Convertir en minuscules
                for (Etudiant student : students) {
                    // Vérifiez si le nom contient le texte de recherche
                    if (student.getNom().toLowerCase().startsWith(filterPattern)) {
                        filteredList.add(student);
                    }
                }
            }

            results.values = filteredList; // Stockez les résultats filtrés
            results.count = filteredList.size(); // Mettez à jour le compte
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            studentsFilter.clear(); // Effacez la liste actuelle
            studentsFilter.addAll((List<Etudiant>) filterResults.values); // Mettez à jour avec les résultats filtrés
            notifyDataSetChanged(); // Notifiez l'adapter pour rafraîchir l'affichage
        }
    }
}

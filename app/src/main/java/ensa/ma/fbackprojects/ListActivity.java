package ensa.ma.fbackprojects;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

;import java.util.ArrayList;
import java.util.List;

import ensa.ma.fbackprojects.adapter.EtudiantAdapter;
import ensa.ma.fbackprojects.beans.Etudiant;
import ensa.ma.fbackprojects.service.EtudiantService;

public class ListActivity extends AppCompatActivity {
    private List<Etudiant> students;
    private RecyclerView recyclerView;
    private EtudiantAdapter etudiantAdapter;
    private EtudiantService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Configurer la Toolbar si vous en utilisez une
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Le reste de votre configuration pour le RecyclerView...

        students = new ArrayList<>() ;
        recyclerView = findViewById(R.id.recycle_view);
        etudiantAdapter = new EtudiantAdapter(this, students);
        this.service = EtudiantService.getInstance(getApplicationContext(),etudiantAdapter);
        recyclerView.setAdapter(etudiantAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        service.getStudents(etudiantAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // Not needed for swipe, return false
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Etudiant student = etudiantAdapter.getStudentsFilter().get(position); // Get the student object

                // Display a message with the student's ID
                Toast.makeText(ListActivity.this, "Vous avez swipé sur cet élément d'id : " + student.getId(), Toast.LENGTH_SHORT).show();

                // Optionally handle the action based on the swipe direction
                if (direction == ItemTouchHelper.LEFT) {
                    // Show a confirmation dialog before deleting
                    new AlertDialog.Builder(ListActivity.this)
                            .setTitle("Confirmation")
                            .setMessage("Êtes-vous sûr de vouloir supprimer cet étudiant d'id : " + student.getId() + " ?")
                            .setPositiveButton("Supprimer", (dialog, which) -> {
                                // Handle the delete action here if the user confirms
                                EtudiantService.getInstance().delete(student);
                                etudiantAdapter.getStudentsFilter().remove(position);
                                etudiantAdapter.getStudents().remove(position);
                                etudiantAdapter.notifyItemRemoved(position);
                                Toast.makeText(ListActivity.this, "Étudiant supprimé avec succès", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Annuler", (dialog, which) -> {
                                // User cancelled the deletion, so we refresh the item to put it back
                                etudiantAdapter.notifyItemChanged(position);
                                dialog.dismiss();
                            })
                            .setCancelable(false) // Prevent dialog from being canceled outside
                            .show();
                } else if (direction == ItemTouchHelper.RIGHT) {
                    // Handle swipe right action (e.g., update student)
                    Toast.makeText(ListActivity.this, "Vous avez swipé sur cet élément à droite d'id : " + student.getId(), Toast.LENGTH_SHORT).show();
                    // Optionally call etudiantAdapter.notifyItemChanged(position); if you want to refresh the item after the dialog
                    etudiantAdapter.notifyItemChanged(position);
                }
            }


            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                // Optionally, add some visual feedback like background color when swiping
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Here !");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.d("SearchView", "Text changed: " + query);
                if (etudiantAdapter != null) {
                    etudiantAdapter.getFilter().filter(query);
                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.app_bar_create) {
            // Lancer l'activity pour ajouter un nouvel étudiant
            Intent intent = new Intent(ListActivity.this, AddEtudiantActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

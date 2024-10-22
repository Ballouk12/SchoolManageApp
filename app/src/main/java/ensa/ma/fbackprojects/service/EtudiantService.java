package ensa.ma.fbackprojects.service;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ensa.ma.fbackprojects.ListActivity;
import ensa.ma.fbackprojects.R;
import ensa.ma.fbackprojects.adapter.EtudiantAdapter;
import ensa.ma.fbackprojects.beans.Etudiant;
import ensa.ma.fbackprojects.dao.IDao;
import ensa.ma.fbackprojects.ListActivity;


public class EtudiantService implements IDao<Etudiant> {

    private static String TAG = "errstudents";
    private Context context ;
    private List<Etudiant> students;
    private static EtudiantService instance;
    private static EtudiantAdapter STRADAPTER;

    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2/vPojectphp/src/ws/createEtudiant.php";
    String gettUrl = "http://10.0.2.2/vPojectphp/src/ws/loadEtudiant.php";
    String updateURL = "http://10.0.2.2/vPojectphp/src/ws/updateEtudiant.php";
    String deleteURL = "http://10.0.2.2/vPojectphp/src/ws/deleteEtudiant.php";


    private EtudiantService(Context context ,EtudiantAdapter adapter){
        this.students = new ArrayList<>();
        this.context = context ;
        this.STRADAPTER = adapter ;

    }

    public static EtudiantService getInstance(Context context,EtudiantAdapter adapter) {
        if(instance == null)
            instance = new EtudiantService(context, adapter);
        return instance;
    }

    public static EtudiantService getInstance() {
        return instance;
    }
    @Override
    public boolean create(Etudiant o ) {
        getRequestQueue() ;

        StringRequest request = new StringRequest(Request.Method.POST,
                insertUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("crtResp", "onResponse: "+response.toString());
                getStudents(STRADAPTER);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                if (error.networkResponse != null) {
                    Log.e(TAG, "Status Code: " + error.networkResponse.statusCode);
                    Log.e(TAG, "Response Data: " + new String(error.networkResponse.data));
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("nom", o.getNom());
                params.put("prenom", o.getPrenom());
                params.put("ville", o.getVille());
                params.put("sexe", o.getSexe());
                params.put("img",o.getImg());
                return params;
            }
        };
        requestQueue.add(request);
        getStudents(STRADAPTER);
        return true;

    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue ;
    }

    public String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }



    @Override
    public void getStudents(final EtudiantAdapter adapter) {
        getRequestQueue() ;
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET,gettUrl,null,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                 students.clear();
                 for(int i=0;i<response.length();i++) {
                     try {
                         JSONObject obj = response.getJSONObject(i) ;
                         int id = obj.getInt("id");
                         String nom = obj.getString("nom");
                         String prenom = obj.getString("prenom");
                         String sexe = obj.getString("sexe");
                         String ville = obj.getString("ville");
                         String image = obj.getString("img");
                         Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.imgg);
                         //String image = encodeImageToBase64(bitmap);

                         students.add(new Etudiant(id,nom,prenom,sexe,ville,image));

                     } catch (JSONException e ) {
                         e.printStackTrace();
                     }

                 }
                adapter.updateData(students);
            }

                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Erreur de la requette: "+error.getMessage() );
            }

        }) ;
        requestQueue.add(getRequest) ;
    }

    @Override
    public boolean update(Etudiant o) {
        getRequestQueue();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nom", o.getNom());
            jsonObject.put("prenom", o.getPrenom());
            jsonObject.put("sexe", o.getSexe());
            jsonObject.put("ville", o.getVille());
            jsonObject.put("img" , o.getImg()) ;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("up", "update: erreur pour construire JosnObjet" );
            return false; // Retourner false en cas d'erreur de création du JSON.
        }

        JsonObjectRequest updateRequest = new JsonObjectRequest(
                Request.Method.PUT,
                updateURL + "?id=" + o.getId(),
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("upEr", "updte  ffffff"+response);
                        Log.d("update", "Liste des étudiants mise à jour avec succès.");
                        getStudents(STRADAPTER);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Erreur lors de la mise à jour des données : " + error.getMessage());
                        getStudents(STRADAPTER);
                    }
                }
        );

        requestQueue.add(updateRequest);
        return true;
    }

    @Override
   public boolean delete(Etudiant o) {

        getRequestQueue() ;
        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE,deleteURL+"?id="+o.getId(),
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("true")) {
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                if (error.networkResponse != null) {
                    Log.e(TAG, "Status Code Delete: " + error.networkResponse.statusCode);
                    Log.e(TAG, "Response Data: " + new String(error.networkResponse.data));
                }
            }
        });
        requestQueue.add(deleteRequest);
        return true ;
    }


    @Override
    public Etudiant findById(int id) {
        for(Etudiant student : students){
            if(student.getId() == id)
                return student;
        }
        return null;
    }
    @Override
    public List<Etudiant> findAll() {
        return students;
    }
}
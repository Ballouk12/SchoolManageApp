package ensa.ma.fbackprojects.dao;


import android.content.Context;

import java.util.List;

import ensa.ma.fbackprojects.adapter.EtudiantAdapter;
import ensa.ma.fbackprojects.beans.Etudiant;

public interface IDao<T>{
    boolean create(T o);
    boolean update(T o);
    boolean delete(T o);
    void getStudents(final EtudiantAdapter adapter) ;
    T findById(int id);
    List<T> findAll();
}


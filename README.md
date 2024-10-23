## Projet Android : Gestion des Étudiants

### Description

Ce projet est une application mobile Android permettant la gestion des étudiants, qui interagit avec un backend PHP via des services web. L'application permet d'ajouter, supprimer, modifier et afficher des étudiants, en utilisant une architecture modulaire avec des packages dédiés aux entités, aux services, et aux interactions avec la base de données. L'application utilise Volley pour effectuer des requêtes HTTP au backend.

### Fonctionnalités

- **Affichage de la liste des étudiants** : L'application utilise un `RecyclerView` pour afficher la liste des étudiants.
- **Ajout d'un étudiant** : L'utilisateur peut ajouter un étudiant via un formulaire dans l'activité `AddActivity`.
- **Modification et suppression d'étudiants** : Les étudiants peuvent être modifiés ou supprimés par des interactions directes dans la liste (via des swipes).
- **Consommation de services web PHP** : Les données sont échangées avec un backend PHP qui communique avec une base de données MySQL.

### Structure du Projet

- **Package `beans`** : Contient la classe `Etudiant`, représentant l'entité de base.
- **Package `dao`** : Contient l'interface `IDao` pour la définition des opérations CRUD.
- **Package `service`** : Contient la classe `EtudiantService` qui implémente l'interface `IDao` pour la gestion des étudiants.
- **Package `adapter`** : Contient la classe `EtudiantAdapter`, qui configure le `RecyclerView` et gère les interactions avec les étudiants ( clic pour modification).
- **Activités** :
  - `SplashActivity` : Écran de démarrage de l'application.
  - `AddActivity` : Permet l'ajout d'un étudiant.
  - `ListActivity` : Affiche la liste des étudiants et permet les opérations de gestion.

### Prérequis

- **Android Studio** (version 3.5 ou supérieure)
- **PHP** et **MySQL** (via XAMPP ou tout autre serveur local)
- **Volley** : Pour gérer les requêtes réseau
- **Backend PHP** avec services web pour l'ajout, la suppression et la modification d'étudiants

### Installation et Configuration

1. **Cloner le projet** :
   ```bash
   git clone https://github.com/Ballouk12/SchoolManageApp.git
2. **Ouvrir le projet dans Android Studio** :
   - Assurez-vous que votre environnement de développement est configuré avec la version compatible d'Android Studio.

3. **Configurer le backend PHP** :
   - Démarrez XAMPP et assurez-vous que les services Apache et MySQL sont activés.
   - Créez une base de données `school1` dans phpMyAdmin.
   - Ajoutez une table `Etudiant` avec les colonnes suivantes : `id`, `nom`, `prenom`, `ville`, et `sexe`.
   - Déployez les fichiers PHP dans le dossier `htdocs` de XAMPP pour gérer les opérations CRUD (ajout, suppression, mise à jour, et lecture des étudiants).

4. **Configurer les permissions réseau** :
   - Dans le fichier `AndroidManifest.xml`, assurez-vous que la permission Internet est activée :
     ```xml
     <uses-permission android:name="android.permission.INTERNET" />
     ```

### Utilisation

1. **Lancer l'application** :
   - Exécutez l'application sur un émulateur ou un appareil Android.
   - Assurez-vous que le serveur backend PHP est en cours d'exécution sur la même connexion réseau que l'appareil Android (ou l'émulateur).
  
2. **Ajouter des étudiants** :
   - Accédez à l'activité `AddActivity` pour ajouter un nouvel étudiant via le formulaire dédié.

3. **Voir et gérer les étudiants** :
   - Consultez la liste des étudiants dans `ListActivity`. Vous pouvez supprimer ou modifier les étudiants en utilisant les fonctionnalités de swipe ou de clic.

### Technologies Utilisées

- **Java** : Langages de programmation pour le développement Android.
- **Volley** : Bibliothèque de requêtes HTTP pour Android.
- **PHP** : Langage de script côté serveur pour la gestion des web services.
- **MySQL** : Base de données relationnelle pour stocker les informations des étudiants.

### Realisation 

https://github.com/user-attachments/assets/1efb1ef7-5c80-4e03-b8c1-227230696c66

### Auteur

- **Ballouk Mohamed**


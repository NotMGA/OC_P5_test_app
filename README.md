# Projet Yoga App

Ce projet est une application complète composée de deux parties : le Frontend (interface utilisateur) et le Backend (services et API). Ce guide explique comment installer, configurer et utiliser chaque partie de l'application, ainsi que comment exécuter les tests et générer les rapports de couverture de code.

## Prérequis

Avant de commencer, assurez-vous d'avoir les éléments suivants installés :

- Java 17 ou version supérieure
- Maven 3.6 ou version supérieure
- Node.js et npm (pour le frontend)
- MySQL ou un autre système de gestion de base de données compatible
- Git

## Installation de la base de données

1. **Installer MySQL (ou un autre SGBD)**

   Si vous n'avez pas MySQL installé, vous pouvez le télécharger et l'installer depuis le site officiel : [MySQL Downloads](https://dev.mysql.com/downloads/).

2. **Créer la base de données :**

   Connectez-vous à MySQL et créez une nouvelle base de données pour l'application :

   ```sql
   CREATE DATABASE yoga_app;
   ```

   Le script SQL pour créer le schéma est disponible ici : `ressources/sql/script.sql`

3. **Configurer les informations de connexion :**

   Mettez à jour les informations de connexion à la base de données dans le fichier `src/main/resources/application.properties` du Backend :

   ```properties
   spring.datasource.username=your_db_username
   spring.datasource.password=your_db_password
   ```

## Installation de l'application Backend

1. **Installer les dépendances :**

   Utilisez Maven pour installer les dépendances du projet :

   ```bash
   mvn clean install
   ```

2. **Configurer l'application :**

   Assurez-vous que le fichier `src/main/resources/application.properties` contient les bonnes informations pour la configuration de votre base de données et d'autres propriétés nécessaires à l'application.

## Installation de l'application Frontend

1. **Naviguer dans le dossier Frontend :**

   Allez dans le dossier `frontend` qui contient le code de l'interface utilisateur :

   ```bash
   cd front
   ```

2. **Installer les dépendances :**

   Utilisez npm pour installer les dépendances :

   ```bash
   npm install
   ```

3. **Configurer l'application Frontend :**

   Assurez-vous de configurer les variables d'environnement nécessaires dans un fichier `.env` si nécessaire.

## Lancer l'application

### Lancer le Backend

1. **Exécuter l'application Backend avec Maven :**

   Pour démarrer le Backend, exécutez la commande suivante depuis le back :

   ```bash
   mvn spring-boot:run
   ```

   Le Backend sera accessible à l'adresse suivante : `http://localhost:8080`.

2. **Accéder au Backend :**

   Vous pouvez maintenant accéder aux différentes fonctionnalités de l'application via les points d'accès REST.

3. **Compte administrateur par défaut :**

   Par défaut, un compte administrateur est disponible :

   - **Login** : [yoga@studio.com](mailto:yoga@studio.com)
   - **Mot de passe** : test!1234

### Lancer le Frontend

1. **Exécuter l'application Frontend :**

   Pour démarrer le serveur de développement du Frontend, utilisez la commande suivante :
   ```bash
   npm run start
   ```
   Le Frontend sera accessible à l'adresse suivante : `http://localhost:3000`.

## Lancer les tests

### Tests Backend

1. **Exécuter les tests unitaires et d'intégration du Backend :**

   Les tests peuvent être exécutés avec la commande Maven suivante :

   ```bash
   mvn clean test
   ```

   Cette commande exécute tous les tests définis dans le projet.

### Tests Frontend

1. **Exécuter les tests du Frontend :**

   Pour exécuter les tests de l'interface utilisateur :
   ```bash
   npm run test
   ```

   ![image](https://github.com/user-attachments/assets/cc01cd07-103b-44ba-ba4a-385fbfec8254)


### E2E

1. **Lancer les tests end-to-end (E2E) :**

   ```bash
   npm run e2e
   ```

2. **Générer le rapport de couverture (lancer d'abord les tests E2E) :**

   ```bash
   npm run e2e:coverage
   ```

   Le rapport est disponible ici : `front/coverage/lcov-report/index.html`.
   ![image](https://github.com/user-attachments/assets/6f80339a-fcbd-4f60-99d0-d6913fdd0ad5)


## Générer les rapports de couverture de code

### Backend

1. **Exécuter les tests avec couverture de code :**

   Pour générer des rapports de couverture de code avec JaCoCo, utilisez la commande suivante :

   ```bash
   mvn clean test 
   ```

   Les rapports seront générés dans le dossier suivant : `target/site/jacoco/index.html`.
   
![image](https://github.com/user-attachments/assets/d5beeee8-9ff7-42d3-a33a-281ea406b624

Le taux de couverture actuel du code back end est de 66%. Cette couverture est impactée par l'utilisation de Lombok, qui génère automatiquement des fonctions  qui ne sont pas explicitement utilisées dans le projet, ce qui diminue artificiellement le taux de couverture.


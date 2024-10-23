describe('Create Session tests', () => {
  beforeEach(() => {
    // Intercepter toutes les requêtes pour déboguer
    cy.intercept('*', (req) => {
      console.log('Intercepted request:', req.url);
    }).as('allRequests');

    // Intercepter l'appel d'authentification et simuler une réponse JWT

    // Intercepter la requête pour récupérer les informations utilisateur

    // Intercepter la requête pour récupérer les enseignants
    cy.intercept('GET', '/api/teacher', {
      statusCode: 200,
      body: [
        {
          id: 1,
          firstName: 'John',
          lastName: 'Doe',
        },
        {
          id: 2,
          firstName: 'Jane',
          lastName: 'Smith',
        },
      ],
    }).as('getTeachers');

    // Intercepter la requête pour créer une session
    cy.intercept('POST', '/api/session', {
      statusCode: 201,
      body: {
        id: 3,
        name: 'New Session',
        description: 'This is a new session',
        date: '2023-12-15T08:00:00.000Z',
        teacher_id: 1,
        users: [],
        createdAt: '2023-11-01T00:00:00.000Z',
        updatedAt: '2023-11-15T00:00:00.000Z',
      },
    }).as('createSession');

    // Simuler la session de l'utilisateur en configurant localStorage directement
    cy.loginadmin();
    // Enregistrer les informations de session dans le localStorage

    // Naviguer vers la page de la liste des sessions en cliquant sur le lien

    // Attendre que la requête pour obtenir les sessions soit interceptée

    // Cliquer sur le bouton "Create" pour aller sur le formulaire de création
    cy.get('button[routerLink="create"]').click();
    cy.wait('@getTeachers'); // Attendre que la requête pour obtenir les enseignants soit interceptée
  });

  it('should create a new session successfully', () => {
    // Remplir le formulaire de création de session
    cy.get('input[formControlName="name"]').type('New Session');
    cy.get('input[formControlName="date"]').type('2023-12-15');
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('mat-option').contains('John Doe').click(); // Sélectionner un enseignant
    cy.get('textarea[formControlName="description"]').type(
      'This is a new session'
    );

    // Soumettre le formulaire
    cy.get('button[type=submit]').click();
    cy.wait('@createSession'); // Attendre que la requête de création de session soit interceptée

    // Vérifier que l'utilisateur est redirigé vers la page de la liste des sessions après création
    cy.url().should('include', '/sessions');

    // Vérifier le message de succès
    cy.get('.mat-snack-bar-container').should('contain', 'Session created !');
  });
  it('should not submit the form when a required field is missing', () => {
    // Remplir partiellement le formulaire de création de session
    cy.get('input[formControlName="name"]').type('New Session');
    // Ne pas remplir le champ date pour tester le cas où il est manquant
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('mat-option').contains('John Doe').click(); // Sélectionner un enseignant
    cy.get('textarea[formControlName="description"]').type(
      'This is a new session'
    );

    // Vérifier que le bouton de soumission est désactivé car le champ "date" est manquant
    cy.get('button[type=submit]').should('be.disabled');
  });
});

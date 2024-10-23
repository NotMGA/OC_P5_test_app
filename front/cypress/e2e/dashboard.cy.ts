describe('ListComponent tests', () => {
  beforeEach(() => {
    // Intercepter toutes les requêtes pour déboguer
    cy.intercept('*', (req) => {
      console.log('Intercepted request:', req.url);
    }).as('allRequests');

    // Intercepter l'appel d'authentification et simuler une réponse JWT

    // Intercepter la requête pour récupérer les informations utilisateur
    cy.intercept('GET', '/api/user/1', {
      statusCode: 200,
      body: {
        id: 1,
        firstName: 'Admin',
        lastName: 'User',
        email: 'admin@example.com',
        admin: true,
        createdAt: '2023-01-01',
        updatedAt: '2023-10-01',
      },
    }).as('getUser');

    // Intercepter la requête pour récupérer les sessions
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [
        {
          id: 1,
          name: 'Morning Yoga',
          description: 'Start your day with a relaxing yoga session.',
          date: '2023-12-01T08:00:00.000Z',
          teacher_id: 1,
          users: [1, 2],
          createdAt: '2023-11-01T00:00:00.000Z',
          updatedAt: '2023-11-15T00:00:00.000Z',
        },
        {
          id: 2,
          name: 'Evening Yoga',
          description: 'Unwind your day with an evening yoga session.',
          date: '2023-12-02T18:00:00.000Z',
          teacher_id: 2,
          users: [],
          createdAt: '2023-11-02T00:00:00.000Z',
          updatedAt: '2023-11-16T00:00:00.000Z',
        },
      ],
    }).as('getSessions');
    cy.loginadmin();
    // Simuler la session de l'utilisateur en configurant localStorage directement
    // Attendre que la requête de login soit interceptée

    // Naviguer vers la page de la liste des sessions en cliquant sur le lien
    cy.get('span[routerlink="sessions"]').click();
    cy.wait('@getSessions'); // Attendre que la requête pour obtenir les sessions soit interceptée
  });

  it('should display the list of available sessions', () => {
    // Vérifier que le titre "Rentals available" est affiché
    cy.get('mat-card-title').should('contain', 'Rentals available');

    // Vérifier que les sessions sont bien affichées
    cy.get('mat-card').should('have.length', 3); // 1 pour le conteneur principal + 2 pour les sessions

    // Vérifier les détails de la première session
    cy.get('mat-card')
      .eq(1)
      .within(() => {
        cy.get('mat-card-title').should('contain', 'Morning Yoga');
        cy.get('mat-card-subtitle').should(
          'contain',
          'Session on December 1, 2023'
        );
        cy.get('p').should(
          'contain',
          'Start your day with a relaxing yoga session.'
        );
      });

    // Vérifier les détails de la deuxième session
    cy.get('mat-card')
      .eq(2)
      .within(() => {
        cy.get('mat-card-title').should('contain', 'Evening Yoga');
        cy.get('mat-card-subtitle').should(
          'contain',
          'Session on December 2, 2023'
        );
        cy.get('p').should(
          'contain',
          'Unwind your day with an evening yoga session.'
        );
      });
  });

  it('should display the create button for admin users', () => {
    // Vérifier que le bouton "Create" est visible pour les administrateurs
    cy.get('button[routerLink="create"]').should('be.visible');
  });

  it('should display the edit button for each session if the user is an admin', () => {
    // Vérifier que chaque session a un bouton "Edit" visible pour les administrateurs
    cy.get('mat-card')
      .eq(1)
      .within(() => {
        cy.get('button').contains('Edit').should('be.visible');
      });

    cy.get('mat-card')
      .eq(2)
      .within(() => {
        cy.get('button').contains('Edit').should('be.visible');
      });
  });
});

describe('DetailComponent tests', () => {
  beforeEach(() => {
    // Intercepter toutes les requêtes pour déboguer
    cy.intercept('*', (req) => {
      console.log('Intercepted request:', req.url);
    }).as('allRequests');

    // Intercepter l'appel d'authentification et simuler une réponse JWT
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        jwt: 'valid-jwt-token',
        id: 1,
        username: 'admin@example.com',
        firstName: 'Admin',
        lastName: 'User',
        admin: true,
      },
    }).as('loginRequest');

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
      ],
    }).as('getSessions');

    // Intercepter la requête pour obtenir les détails d'une session
    cy.intercept('GET', '/api/session/1', {
      statusCode: 200,
      body: {
        id: 1,
        name: 'Yoga Session',
        description: 'A relaxing yoga session.',
        date: new Date().toISOString(),
        teacher_id: 1,
        users: [1, 2],
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      },
    }).as('getSessionDetail');

    // Intercepter la requête pour obtenir les détails d'un enseignant
    cy.intercept('GET', '/api/teacher/1', {
      statusCode: 200,
      body: {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
      },
    }).as('getTeacherDetail');

    // Simuler la session de l'utilisateur en configurant localStorage directement
    cy.visit('/login').then(() => {
      const sessionInfo = {
        token: 'valid-jwt-token',
        type: 'Bearer',
        id: 1,
        username: 'admin@example.com',
        firstName: 'Admin',
        lastName: 'User',
        admin: true,
      };
      window.localStorage.setItem(
        'sessionInformation',
        JSON.stringify(sessionInfo)
      );
      window.localStorage.setItem('isLogged', 'true');
    });

    // Remplir le formulaire de login
    cy.get('input[formControlName=email]').type('admin@example.com');
    cy.get('input[formControlName=password]').type('password123');
    cy.get('button[type=submit]').click();
    cy.wait('@loginRequest'); // Attendre que la requête de login soit interceptée

    // Naviguer vers la page de la liste des sessions en cliquant sur le lien
    // Attendre que la requête pour obtenir les sessions soit interceptée

    // Cliquer sur un bouton de détail de session pour accéder à la page de détail
    cy.wait('@getSessions');
    cy.get('button').contains('Detail').click();
    cy.wait('@getSessionDetail'); // Attendre que la requête pour obtenir les détails de la session soit interceptée
    cy.wait('@getTeacherDetail'); // Attendre que la requête pour obtenir les détails de l'enseignant soit interceptée
  });

  it('should display session details correctly', () => {
    cy.get('h1').should('contain', 'Yoga Session'); // Vérifier le titre de la session
    cy.get('mat-card-subtitle').should('contain', 'John DOE'); // Vérifier l'enseignant
    cy.get('div').should('contain', 'A relaxing yoga session.'); // Vérifier la description
  });
  it('should display delete button for admin', () => {
    // Vérifier que le bouton "Delete" est présent et visible pour les administrateurs
    cy.get('button').contains('Delete').should('be.visible');
  });
  it('should delete session when delete button is clicked', () => {
    // Mocker la requête de suppression
    cy.intercept('DELETE', '/api/session/1', {
      statusCode: 200,
    }).as('deleteSession');

    // Cliquer sur le bouton de suppression
    cy.get('button').contains('Delete').click();

    // Attendre que la requête de suppression soit interceptée
    cy.wait('@deleteSession');

    // Vérifier la présence du message de confirmation
    cy.get('.mat-snack-bar-container').should('contain', 'Session deleted !');
  });
});

describe('MeComponent tests with authenticated session', () => {
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
        username: 'john.doe@example.com',
        firstName: 'John',
        lastName: 'Doe',
        admin: false,
      },
    }).as('loginRequest');

    // Intercepter la requête pour récupérer les informations utilisateur
    cy.intercept('GET', '/api/user/1', {
      statusCode: 200,
      body: {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        email: 'john.doe@example.com',
        admin: false,
        createdAt: '2023-01-01',
        updatedAt: '2023-10-01',
      },
    }).as('getUser');

    // Simuler la session de l'utilisateur en configurant localStorage directement
    cy.visit('/login').then(() => {
      const sessionInfo = {
        token: 'valid-jwt-token',
        type: 'Bearer',
        id: 1,
        username: 'john.doe@example.com',
        firstName: 'John',
        lastName: 'Doe',
        admin: false,
      };
      // Enregistrer les informations de session dans le localStorage
      window.localStorage.setItem(
        'sessionInformation',
        JSON.stringify(sessionInfo)
      );
      window.localStorage.setItem('isLogged', 'true');
    });
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234');

    // Submit the form
    cy.get('button[type=submit]').click();

    // Visitez la page /me après avoir configuré la session utilisateur
    cy.get('span[routerlink="me"].link').click();
    cy.wait('@getUser'); // Attendre que la requête pour obtenir l'utilisateur soit interceptée
  });

  it('should display user information correctly', () => {
    cy.get('h1').should('contain', 'User information'); // Vérifie le titre
    cy.get('p').should('contain', 'John DOE'); // Vérifie le nom de l'utilisateur
    cy.get('p').should('contain', 'john.doe@example.com'); // Vérifie l'email de l'utilisateur
  });
  it('should delete the user and navigate to the home page', () => {
    // Mock the delete API response
    cy.intercept('DELETE', '/api/user/1', {
      statusCode: 200,
    }).as('deleteUser');

    // Simuler le clic sur le bouton "Delete"
    cy.get('button').contains('Detail').click();

    // Attendre que l'appel de suppression soit intercepté
    cy.wait('@deleteUser');

    // Vérifier que le message de suppression est affiché
    cy.contains('Your account has been deleted !').should('be.visible');

    // Vérifier la redirection vers la page d'accueil
    cy.url().should('include', '/');
  });
  it('should navigate back when the back button is clicked', () => {
    // Simuler le clic sur le bouton "Retour"
    cy.get('button[mat-icon-button]').click();

    // Vous pourriez utiliser cy.go pour vérifier que l'on revient à la page précédente
    cy.go('back');
  });
});

describe('Update Session spec', () => {
  beforeEach(() => {
    // Visiter la page de mise à jour de la session
    // Supposons que 1 est l'ID de la session existante
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
    // Mock la réponse de l'API pour récupérer les détails de la session existante
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

    cy.intercept('GET', '/api/teacher', {
      statusCode: 200,
      body: [
        { id: 1, firstName: 'Jane', lastName: 'Doe' },
        { id: 2, firstName: 'John', lastName: 'Smith' },
      ],
    }).as('getTeachers');

    cy.intercept('GET', '/api/session/1', {
      statusCode: 200,
      body: {
        id: 1,
        name: 'Yoga Session',
        date: '2024-12-01T00:00:00.000Z',
        teacher_id: 2,
        description: 'This is a yoga session description',
        users: [],
      },
    }).as('getSession');

    // Mock la réponse de l'API pour récupérer la liste des enseignants

    cy.loginadmin();

    cy.get('span[routerlink="sessions"]').click();
    cy.wait('@getSessions');
    cy.get('mat-card')
      .eq(1)
      .within(() => {
        cy.get('button').contains('Edit').click();
      });
  });

  it('should load session data and display it in the form', () => {
    // Attendre que la requête de récupération de la session et des enseignants soit terminée
    cy.wait('@getSession');
    cy.wait('@getTeachers');

    // Vérifier que le formulaire est pré-rempli avec les données de la session existante
    cy.get('input[formControlName="name"]').should(
      'have.value',
      'Yoga Session'
    );
    cy.get('input[formControlName="date"]').should('have.value', '2024-12-01');
    cy.get('textarea[formControlName="description"]').should(
      'have.value',
      'This is a yoga session description'
    );
    cy.get('mat-select[formControlName="teacher_id"]').should(
      'contain',
      'John Smith'
    );
  });

  it('should successfully update the session', () => {
    // Modifier les valeurs du formulaire
    cy.get('input[formControlName="name"]')
      .clear()
      .type('Updated Yoga Session');
    cy.get('input[formControlName="date"]').clear().type('2024-12-02');
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('mat-option').contains('Jane Doe').click();
    cy.get('textarea[formControlName="description"]')
      .clear()
      .type('Updated description for the yoga session');

    // Mock la réponse de l'API pour la mise à jour de la session
    cy.intercept('PUT', '/api/session/1', {
      statusCode: 200,
    }).as('updateSession');

    // Soumettre le formulaire
    cy.get('button[type="submit"]').click();

    // Attendre la requête de mise à jour
    cy.wait('@updateSession');

    // Vérifier que l'utilisateur est redirigé vers la liste des sessions après la mise à jour
    cy.url().should('include', '/sessions');
  });

  it('Form validation errors', () => {
    // Ensure the form controls are invalid without input
    cy.get('input[formControlName="name"]').clear().focus().blur();
    cy.get('input[formControlName="date"]').clear().focus().blur();
    cy.get('textarea[formControlName="description"]').clear().focus().blur();
    cy.get('mat-form-field')
      .eq(0)
      .should('have.class', 'mat-form-field-invalid');

    cy.get('mat-form-field')
      .eq(1)
      .should('have.class', 'mat-form-field-invalid');

    cy.get('mat-form-field')
      .eq(3)
      .should('have.class', 'mat-form-field-invalid');

    // Ensure the submit button is disabled due to form validation
    cy.get('button[type="submit"]').should('be.disabled');
  });
});

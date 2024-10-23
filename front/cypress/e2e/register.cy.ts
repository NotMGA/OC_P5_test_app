describe('Register spec', () => {
  beforeEach(() => {
    // Visit the registration page before each test
    cy.visit('/register');
  });

  it('Successful registration', () => {
    // Mock the successful API response for registration
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
    }).as('registerRequest');

    // Fill in the registration form with valid data
    cy.get('input[formControlName="firstName"]').type('John');
    cy.get('input[formControlName="lastName"]').type('Doe');
    cy.get('input[formControlName="email"]').type('test@user.com');
    cy.get('input[formControlName="password"]').type('securePassword123');

    // Ensure the submit button becomes enabled
    cy.get('button[type="submit"]').should('not.be.disabled');

    // Submit the form
    cy.get('button[type="submit"]').click();

    // Wait for the registration request to complete
    cy.wait('@registerRequest');

    // Ensure the URL changes to the login page after successful registration
    cy.url().should('include', '/login');
  });

  it('Failed registration (server error)', () => {
    // Mock a failed registration request (e.g., email already registered)
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 400,
      body: {
        error: 'Email already in use',
      },
    }).as('registerFailed');

    // Fill in the registration form with valid data
    cy.get('input[formControlName="firstName"]').type('Jane');
    cy.get('input[formControlName="lastName"]').type('Doe');
    cy.get('input[formControlName="email"]').type('duplicate@user.com');
    cy.get('input[formControlName="password"]').type('securePassword123');

    // Ensure the submit button becomes enabled
    cy.get('button[type="submit"]').should('not.be.disabled');

    // Submit the form
    cy.get('button[type="submit"]').click();

    // Wait for the failed registration request
    cy.wait('@registerFailed');

    // Check if the error message is visible
    cy.get('.error').should('be.visible').and('contain', 'An error occurred');
  });

  it('Form validation errors', () => {
    // Ensure the form controls are invalid without input
    cy.get('input[formControlName="firstName"]').should(
      'have.class',
      'ng-invalid'
    );
    cy.get('input[formControlName="lastName"]').should(
      'have.class',
      'ng-invalid'
    );
    cy.get('input[formControlName="email"]').should('have.class', 'ng-invalid');
    cy.get('input[formControlName="password"]').should(
      'have.class',
      'ng-invalid'
    );

    // Ensure the submit button is disabled due to form validation
    cy.get('button[type="submit"]').should('be.disabled');
  });
  it('should mark fields as invalid and red if they are empty', () => {
    // Tentez de soumettre le formulaire sans remplir les champs pour activer les validations
    cy.get('input[formControlName="firstName"]').focus().blur();
    cy.get('input[formControlName="lastName"]').focus().blur();
    cy.get('input[formControlName="email"]').focus().blur();
    cy.get('input[formControlName="password"]').focus().blur();

    // Vérifiez que chaque champ a la bordure rouge
    cy.get('mat-form-field')
      .eq(0)
      .should('have.class', 'mat-form-field-invalid');

    cy.get('mat-form-field')
      .eq(1)
      .should('have.class', 'mat-form-field-invalid');

    cy.get('mat-form-field')
      .eq(2)
      .should('have.class', 'mat-form-field-invalid');

    cy.get('mat-form-field')
      .eq(3)
      .should('have.class', 'mat-form-field-invalid');
  });
});

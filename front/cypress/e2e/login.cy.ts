describe('Login spec', () => {
  it('Successful login', () => {
    // Visit the login page
    cy.visit('/login');

    // Mock successful login API response
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
        token: 'fake-jwt-token',
      },
    }).as('loginRequest');

    // Fill in the form
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234');

    // Submit the form
    cy.get('button[type=submit]').click();

    // Wait for the login request to be intercepted
    cy.wait('@loginRequest');

    // Check URL for successful login redirect
    cy.url().should('include', '/sessions');
  });

  it('Failed login', () => {
    // Visit the login page
    cy.visit('/login');

    // Mock failed login API response
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: {
        error: 'Invalid credentials',
      },
    }).as('loginFailure');

    // Fill in the form with wrong credentials
    cy.get('input[formControlName=email]').type('wrong@user.com');
    cy.get('input[formControlName=password]').type('wrongpassword');

    // Submit the form
    cy.get('button[type=submit]').click();

    // Wait for the failed login request to be intercepted
    cy.wait('@loginFailure');

    // Check URL stays on the login page
    cy.url().should('include', '/login');

    // Optionally, check for error message
    cy.contains('An error occurred').should('be.visible');
  });

  it('should lougout user', () => {
    cy.visit('/login');

    // Mock successful login API response
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
        token: 'fake-jwt-token',
      },
    }).as('loginRequest');

    // Fill in the form
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234');

    // Submit the form
    cy.get('button[type=submit]').click();

    // Wait for the login request to be intercepted
    cy.wait('@loginRequest');
    cy.get('span.link').contains('Logout').click();
    cy.get('span[routerlink="login"].link').should('be.visible');
  });
  it('should show red border when email field is empty', () => {
    // Visit the login page
    cy.visit('/login');

    // Focus on the email field and then blur without typing anything
    cy.get('input[formControlName=email]').focus().blur();

    // Verify that the email field has the ng-invalid class
    cy.get('input[formControlName=email]').should('have.class', 'ng-invalid');

    // Check if the parent mat-form-field has the error state
    cy.get('input[formControlName=email]')
      .parents('.mat-form-field')
      .should('have.class', 'mat-form-field-invalid');
    cy.get('button[type=submit]').should('be.disabled');
  });
  it('should show red border when password field is empty', () => {
    // Visit the login page
    cy.visit('/login');

    // Focus on the password field and then blur without typing anything
    cy.get('input[formControlName=password]').focus().blur();

    // Verify that the password field has the ng-invalid class
    cy.get('input[formControlName=password]').should(
      'have.class',
      'ng-invalid'
    );

    // Check if the parent mat-form-field has the error state
    cy.get('input[formControlName=password]')
      .parents('.mat-form-field')
      .should('have.class', 'mat-form-field-invalid');
    cy.get('button[type=submit]').should('be.disabled');
  });
});

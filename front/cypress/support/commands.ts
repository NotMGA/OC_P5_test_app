// ***********************************************
// This example namespace declaration will help
// with Intellisense and code completion in your
// IDE or Text Editor.
// ***********************************************
// declare namespace Cypress {
//   interface Chainable<Subject = any> {
//     customCommand(param: any): typeof customCommand;
//   }
// }
//
// function customCommand(param: any): void {
//   console.warn(param);
// }
//
// NOTE: You can use it like so:
// Cypress.Commands.add('customCommand', customCommand);
//
// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })
Cypress.Commands.add('loginadmin', () => {
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
    // Enregistrer les informations de session dans le localStorage
    window.localStorage.setItem(
      'sessionInformation',
      JSON.stringify(sessionInfo)
    );
    window.localStorage.setItem('isLogged', 'true');
  });
  cy.get('input[formControlName=email]').type('admin@example.com');
  cy.get('input[formControlName=password]').type('password123');
  cy.get('button[type=submit]').click();
  cy.wait('@loginRequest');
});

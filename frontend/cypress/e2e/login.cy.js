// https://on.cypress.io/api

// cypress/e2e/user.cy.ts

beforeEach(() => {
  cy.visit('/login');

  cy.get('form').invoke('removeAttr', 'novalidate');
});

describe('Form Submission Test', () => {
  it('should get correct response from server when invalid data is submitted', () => {
    cy.visit('http://localhost:5173');
    cy.contains('button', 'Logg inn').click();
    cy.url().should('include', '/login');

    cy.intercept('POST', 'http://localhost:3000/login').as('loginRequest');

    cy.get('#email').clear().type('pierbattista.pizzaballa@mail.com');
    cy.get('button[type="submit"]').click();

    cy.url().should('include', '/login');
  });

  it('should get correct response from server when valid user data is submitted', () => {
    cy.visit('http://localhost:5173/login');

    cy.get('#email').clear().type('pierbattista.pizzaballa@mail.com');
    cy.get('#password').clear().type('wordpass');
    cy.get('button[type="submit"]').click();

    cy.intercept('POST', 'http://localhost:3000/Status', (req) => {
      req.reply((res) => {
        expect(res.statusCode).to.equal(200);
        expect(res.body).to.have.property('Success', true);
      });
    });
  });

  it('should get correct response from server when valid admin data is submitted', () => {
    cy.visit('http://localhost:5173/login');

    cy.get('#email').clear().type('brotherman@testern.no');
    cy.get('#password').clear().type('wordpass');
    cy.get('button[type="submit"]').click();

    cy.intercept('POST', 'http://localhost:3000/admin', (req) => {
      req.reply((res) => {
        expect(res.statusCode).to.equal(200);
        expect(res.body).to.have.property('Success', true);
      });
    });
  });

  it('should get correct response from server when valid admin data is submitted', () => {
    cy.visit('http://localhost:5173/login');

    cy.get('button[type="underline"]').click();

    cy.intercept('POST', 'http://localhost:3000/signup', (req) => {
      req.reply((res) => {
        expect(res.statusCode).to.equal(200);
        expect(res.body).to.have.property('Success', true);
      });
    });
  });
});

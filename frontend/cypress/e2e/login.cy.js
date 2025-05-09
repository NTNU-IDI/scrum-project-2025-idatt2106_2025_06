// https://on.cypress.io/api

// cypress/e2e/user.cy.js

describe('Login and register Submission Tests', () => {
  it('should get no response when invalid data is submitted', () => {
    cy.visit('http://localhost:5173');
    cy.contains('button', 'Logg inn').click();
    cy.url().should('include', '/login');

    cy.intercept('POST', 'http://localhost:8080/auth/login').as('loginRequest');

    cy.get('#email').clear().type('pierbattista.pizzaballa@mail.com');
    cy.get('button[type="submit"]').click();

    cy.get('#email').clear();
    cy.get('#password').clear().type('wordpass');
    cy.get('button[type="submit"]').click();

    cy.url().should('include', '/login');
  });

  it('should get correct response from server when valid user data is submitted', () => {
    cy.visit('http://localhost:5173/login');


    cy.get('#email').clear().type('pierbattista.pizzaballa@mail.com');
    cy.get('#password').clear().type('wordpass');
    cy.get('button[type="submit"]').click();

    cy.intercept('POST', 'http://localhost:8080//auth/login', (req) => {
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

    cy.intercept('POST', 'http://localhost:8080/admin', (req) => {
      req.reply((res) => {
        expect(res.statusCode).to.equal(200);
        expect(res.body).to.have.property('Success', true);
      });
    });
  });

  it('should get correct response from server when valid user data is submitted', () => {
    cy.visit('http://localhost:5173/login');

    cy.get('#email').clear().type('pierbattista.pizzaballa@mail.com');
    cy.get('#password').clear().type('wordpass');
    cy.get('button[type="submit"]').click();

    cy.intercept('POST', 'http://localhost:8080//auth/login', (req) => {
      req.reply((res) => {
        expect(res.statusCode).to.equal(200);
        expect(res.body).to.have.property('Success', true);
      });
    });
  });

  /* TODO mock Recaptcha correctly
  it('Should go to signup page and be able to make new user', () => {
    cy.visit('http://localhost:5173/login');

    cy.get('#signup-link').click();

    cy.intercept('POST', 'http://localhost:3000/signup', (req) => {
      req.reply((res) => {
        expect(res.statusCode).to.equal(200);
        expect(res.body).to.have.property('Success', true);
      });
    });

    cy.get('.g-recaptcha').should('be.visible');

    cy.get('#name').clear().type('Ola Normann');
    cy.get('#email').clear().type('ola@normann.no');
    cy.get('#password').clear().type('wordpass');
    cy.get('#confirmpassword').clear().type('wordpass');

    cy.window().then((win) => {
      win.handleRecaptchaResponse('dummy-recaptcha-token');
    });

    cy.get('button[type="submit"]').click();

    cy.intercept('POST', 'http://localhost:8080/login', (req) => {
      req.reply((res) => {
        expect(res.statusCode).to.equal(200);
        expect(res.body).to.have.property('Success', true);
      });
    });
  });*/
});

// https://on.cypress.io/api
// cypress/e2e/user.cy.ts

describe('User-userstories', () => {
  beforeEach(() => {
    cy.visit('http://localhost:5173/login');

    cy.get('#email').clear().type('pierbattista.pizzaballa@mail.com');
    cy.get('#password').clear().type('wordpass');
    cy.get('button[type="submit"]').click();

    cy.intercept('POST', 'http://localhost:8080/Status', (req) => {
      req.reply((res) => {
        expect(res.statusCode).to.equal(200);
        expect(res.body).to.have.property('Success', true);
      });
    });
  });

  it('user should not go to admin page', () => {
    cy.visit('http://localhost:5173/admin');
    cy.url().should('not.include', '/admin');
  });

  it('user should not go to admin page', () => {
    cy.visit('http://localhost:5173/inventory');
    cy.url().should('include', '/inventory');
    cy.get('newItem').click();
  });
});

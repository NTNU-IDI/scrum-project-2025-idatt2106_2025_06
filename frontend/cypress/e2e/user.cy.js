describe('User-userstories', () => {
  beforeEach(() => {
    cy.visit('http://localhost:5173/login');
    cy.get('#email').clear().type('pierbattista.pizzaballa@mail.com');
    cy.get('#password').clear().type('wordpass');
    cy.get('button[type="submit"]').click();

    cy.intercept('POST', 'http://localhost:8080/auth/login', (req) => {
      req.reply((res) => {
        expect(res.statusCode).to.equal(200);
        expect(res.body).to.have.property('Success', true);
      });
    });
  });

  it('user should not go to admin page', () => {
    cy.visit('http://localhost:5173/admin');
    cy.url().should('not.include', '/admin');
    cy.url().should('include', '/');
  });

  it('user should not go to admin map page', () => {
    cy.visit('http://localhost:5173/admin/map');
    cy.url().should('not.include', '/admin/map');
    cy.url().should('include', '/');
  });

  it('user should not go to admin map page', () => {
    cy.wait(2000);

    cy.visit('http://localhost:5173/inventory');

  });
});

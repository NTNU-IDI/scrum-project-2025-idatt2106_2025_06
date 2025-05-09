describe('User-userstories', () => {
  it('user can add items to inventory', () => {
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

    cy.wait(2000);

    cy.visit('http://localhost:5173/inventory');
    cy.get('button').contains('Legg til').click();

    cy.get('input[placeholder="Skriv inn navn på vare..."]').type('Test Vare {enter}{enter}');
    cy.get('input#amount').click();
    cy.get('input#amount').clear().type('5');

    //TODO må hente fra databsen :)

    cy.get('#submitNewItem').click();

    cy.contains('Test Vare').should('be.visible');
    cy.contains('5').should('be.visible');
    cy.contains('stk').should('be.visible');
  });
});

describe('FoodFrenzy E2E Test', () => {

  it('User can login and order food', () => {
    cy.visit('http://localhost:8080/login');
    cy.get('input[name="userEmail"]').type('testuser@example.com');
    cy.get('input[name="userPassword"]').type('pass1234');
    cy.get('.user .loginButton').click(); // form submits


    cy.visit('http://localhost:8080/products');
    cy.url().should('include', '/products'); // or whatever page user lands on

    cy.get('#buy').first().click();
    cy.visit('http://localhost:8080/order_success');
    cy.contains('Ordered SuccessFully... !!!');
  });

});

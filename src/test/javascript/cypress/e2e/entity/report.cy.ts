import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Report e2e test', () => {
  const reportPageUrl = '/report';
  const reportPageUrlPattern = new RegExp('/report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const reportSample = { name: 'fugato traumatic', query: 'smooth cleverly', fileName: 'fault hassle thrive' };

  let report;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/reports/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (report) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/reports/${report.rid}`,
      }).then(() => {
        report = undefined;
      });
    }
  });

  it('Reports menu should load Reports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Report').should('exist');
    cy.url().should('match', reportPageUrlPattern);
  });

  describe('Report page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(reportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Report page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/report/new$'));
        cy.getEntityCreateUpdateHeading('Report');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/reports',
          body: reportSample,
        }).then(({ body }) => {
          report = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/reports?page=0&size=20>; rel="last",<http://localhost/api/reports?page=0&size=20>; rel="first"',
              },
              body: [report],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(reportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Report page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('report');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportPageUrlPattern);
      });

      it('edit button click should load edit Report page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Report');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportPageUrlPattern);
      });

      it('edit button click should load edit Report page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Report');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportPageUrlPattern);
      });

      it('last delete button click should delete instance of Report', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('report').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportPageUrlPattern);

        report = undefined;
      });
    });
  });

  describe('new Report page', () => {
    beforeEach(() => {
      cy.visit(`${reportPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Report');
    });

    it('should create an instance of Report', () => {
      cy.get(`[data-cy="rid"]`).type('637fb2e5-3b01-4d4f-8513-e8ade216f619');
      cy.get(`[data-cy="rid"]`).should('have.value', '637fb2e5-3b01-4d4f-8513-e8ade216f619');

      cy.get(`[data-cy="cid"]`).type('irritably gadzooks');
      cy.get(`[data-cy="cid"]`).should('have.value', 'irritably gadzooks');

      cy.get(`[data-cy="name"]`).type('hastily counter barring');
      cy.get(`[data-cy="name"]`).should('have.value', 'hastily counter barring');

      cy.get(`[data-cy="description"]`).type('negative');
      cy.get(`[data-cy="description"]`).should('have.value', 'negative');

      cy.get(`[data-cy="query"]`).type('anti thanX');
      cy.get(`[data-cy="query"]`).should('have.value', 'anti thanX');

      cy.get(`[data-cy="queryType"]`).select('HQL');

      cy.get(`[data-cy="fileName"]`).type('where cycle regularly');
      cy.get(`[data-cy="fileName"]`).should('have.value', 'where cycle regularly');

      cy.get(`[data-cy="reportType"]`).select('HTML');

      cy.get(`[data-cy="licenseHolder"]`).type('even');
      cy.get(`[data-cy="licenseHolder"]`).should('have.value', 'even');

      cy.get(`[data-cy="owner"]`).type('hence once');
      cy.get(`[data-cy="owner"]`).should('have.value', 'hence once');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        report = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', reportPageUrlPattern);
    });
  });
});

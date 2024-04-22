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

describe('ReportColumnMapping e2e test', () => {
  const reportColumnMappingPageUrl = '/report-column-mapping';
  const reportColumnMappingPageUrlPattern = new RegExp('/report-column-mapping(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const reportColumnMappingSample = { rid: '3b7fdffc-f2b6-4ba1-9b3e-dbba07744045' };

  let reportColumnMapping;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/report-column-mappings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/report-column-mappings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/report-column-mappings/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (reportColumnMapping) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/report-column-mappings/${reportColumnMapping.rid}`,
      }).then(() => {
        reportColumnMapping = undefined;
      });
    }
  });

  it('ReportColumnMappings menu should load ReportColumnMappings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('report-column-mapping');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReportColumnMapping').should('exist');
    cy.url().should('match', reportColumnMappingPageUrlPattern);
  });

  describe('ReportColumnMapping page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(reportColumnMappingPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ReportColumnMapping page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/report-column-mapping/new$'));
        cy.getEntityCreateUpdateHeading('ReportColumnMapping');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportColumnMappingPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/report-column-mappings',
          body: reportColumnMappingSample,
        }).then(({ body }) => {
          reportColumnMapping = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/report-column-mappings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [reportColumnMapping],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(reportColumnMappingPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ReportColumnMapping page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('reportColumnMapping');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportColumnMappingPageUrlPattern);
      });

      it('edit button click should load edit ReportColumnMapping page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReportColumnMapping');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportColumnMappingPageUrlPattern);
      });

      it('edit button click should load edit ReportColumnMapping page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReportColumnMapping');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportColumnMappingPageUrlPattern);
      });

      it('last delete button click should delete instance of ReportColumnMapping', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('reportColumnMapping').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportColumnMappingPageUrlPattern);

        reportColumnMapping = undefined;
      });
    });
  });

  describe('new ReportColumnMapping page', () => {
    beforeEach(() => {
      cy.visit(`${reportColumnMappingPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ReportColumnMapping');
    });

    it('should create an instance of ReportColumnMapping', () => {
      cy.get(`[data-cy="rid"]`).type('0cb13c17-818a-488c-94ea-ba769c6f3620');
      cy.get(`[data-cy="rid"]`).should('have.value', '0cb13c17-818a-488c-94ea-ba769c6f3620');

      cy.get(`[data-cy="sourceColumnName"]`).type('in');
      cy.get(`[data-cy="sourceColumnName"]`).should('have.value', 'in');

      cy.get(`[data-cy="sourceColumnIndex"]`).type('23740');
      cy.get(`[data-cy="sourceColumnIndex"]`).should('have.value', '23740');

      cy.get(`[data-cy="columnTitle"]`).type('an');
      cy.get(`[data-cy="columnTitle"]`).should('have.value', 'an');

      cy.get(`[data-cy="lang"]`).type('searchingly exactly');
      cy.get(`[data-cy="lang"]`).should('have.value', 'searchingly exactly');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        reportColumnMapping = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', reportColumnMappingPageUrlPattern);
    });
  });
});

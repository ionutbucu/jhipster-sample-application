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

describe('ReportDataSource e2e test', () => {
  const reportDataSourcePageUrl = '/report-data-source';
  const reportDataSourcePageUrlPattern = new RegExp('/report-data-source(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const reportDataSourceSample = { rid: '47f010b0-288f-492a-830e-8003b77b2a6b' };

  let reportDataSource;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/report-data-sources+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/report-data-sources').as('postEntityRequest');
    cy.intercept('DELETE', '/api/report-data-sources/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (reportDataSource) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/report-data-sources/${reportDataSource.rid}`,
      }).then(() => {
        reportDataSource = undefined;
      });
    }
  });

  it('ReportDataSources menu should load ReportDataSources page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('report-data-source');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReportDataSource').should('exist');
    cy.url().should('match', reportDataSourcePageUrlPattern);
  });

  describe('ReportDataSource page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(reportDataSourcePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ReportDataSource page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/report-data-source/new$'));
        cy.getEntityCreateUpdateHeading('ReportDataSource');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportDataSourcePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/report-data-sources',
          body: reportDataSourceSample,
        }).then(({ body }) => {
          reportDataSource = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/report-data-sources+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [reportDataSource],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(reportDataSourcePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ReportDataSource page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('reportDataSource');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportDataSourcePageUrlPattern);
      });

      it('edit button click should load edit ReportDataSource page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReportDataSource');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportDataSourcePageUrlPattern);
      });

      it('edit button click should load edit ReportDataSource page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReportDataSource');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportDataSourcePageUrlPattern);
      });

      it('last delete button click should delete instance of ReportDataSource', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('reportDataSource').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportDataSourcePageUrlPattern);

        reportDataSource = undefined;
      });
    });
  });

  describe('new ReportDataSource page', () => {
    beforeEach(() => {
      cy.visit(`${reportDataSourcePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ReportDataSource');
    });

    it('should create an instance of ReportDataSource', () => {
      cy.get(`[data-cy="rid"]`).type('7fd26a2b-ef56-42e4-ac11-4a0a1ea5d422');
      cy.get(`[data-cy="rid"]`).should('have.value', '7fd26a2b-ef56-42e4-ac11-4a0a1ea5d422');

      cy.get(`[data-cy="type"]`).type('forceful gad');
      cy.get(`[data-cy="type"]`).should('have.value', 'forceful gad');

      cy.get(`[data-cy="url"]`).type('https://first-frontier.com/');
      cy.get(`[data-cy="url"]`).should('have.value', 'https://first-frontier.com/');

      cy.get(`[data-cy="user"]`).type('shocking');
      cy.get(`[data-cy="user"]`).should('have.value', 'shocking');

      cy.get(`[data-cy="password"]`).type('consequently');
      cy.get(`[data-cy="password"]`).should('have.value', 'consequently');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        reportDataSource = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', reportDataSourcePageUrlPattern);
    });
  });
});

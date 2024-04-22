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

describe('ReportExecution e2e test', () => {
  const reportExecutionPageUrl = '/report-execution';
  const reportExecutionPageUrlPattern = new RegExp('/report-execution(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const reportExecutionSample = { date: '2024-04-21T23:39:47.981Z' };

  let reportExecution;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/report-executions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/report-executions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/report-executions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (reportExecution) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/report-executions/${reportExecution.rid}`,
      }).then(() => {
        reportExecution = undefined;
      });
    }
  });

  it('ReportExecutions menu should load ReportExecutions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('report-execution');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReportExecution').should('exist');
    cy.url().should('match', reportExecutionPageUrlPattern);
  });

  describe('ReportExecution page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(reportExecutionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ReportExecution page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/report-execution/new$'));
        cy.getEntityCreateUpdateHeading('ReportExecution');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportExecutionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/report-executions',
          body: reportExecutionSample,
        }).then(({ body }) => {
          reportExecution = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/report-executions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/report-executions?page=0&size=20>; rel="last",<http://localhost/api/report-executions?page=0&size=20>; rel="first"',
              },
              body: [reportExecution],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(reportExecutionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ReportExecution page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('reportExecution');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportExecutionPageUrlPattern);
      });

      it('edit button click should load edit ReportExecution page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReportExecution');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportExecutionPageUrlPattern);
      });

      it('edit button click should load edit ReportExecution page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReportExecution');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportExecutionPageUrlPattern);
      });

      it('last delete button click should delete instance of ReportExecution', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('reportExecution').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportExecutionPageUrlPattern);

        reportExecution = undefined;
      });
    });
  });

  describe('new ReportExecution page', () => {
    beforeEach(() => {
      cy.visit(`${reportExecutionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ReportExecution');
    });

    it('should create an instance of ReportExecution', () => {
      cy.get(`[data-cy="rid"]`).type('3c6da7ad-6d13-4fe3-918c-28308d914239');
      cy.get(`[data-cy="rid"]`).should('have.value', '3c6da7ad-6d13-4fe3-918c-28308d914239');

      cy.get(`[data-cy="date"]`).type('2024-04-22T08:05');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2024-04-22T08:05');

      cy.get(`[data-cy="error"]`).type('broadly');
      cy.get(`[data-cy="error"]`).should('have.value', 'broadly');

      cy.get(`[data-cy="url"]`).type('https://incompatible-experimentation.com');
      cy.get(`[data-cy="url"]`).should('have.value', 'https://incompatible-experimentation.com');

      cy.get(`[data-cy="user"]`).type('as consequently macaw');
      cy.get(`[data-cy="user"]`).should('have.value', 'as consequently macaw');

      cy.get(`[data-cy="additionalInfo"]`).type('knottily until');
      cy.get(`[data-cy="additionalInfo"]`).should('have.value', 'knottily until');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        reportExecution = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', reportExecutionPageUrlPattern);
    });
  });
});

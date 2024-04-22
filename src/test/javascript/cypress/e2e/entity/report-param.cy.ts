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

describe('ReportParam e2e test', () => {
  const reportParamPageUrl = '/report-param';
  const reportParamPageUrlPattern = new RegExp('/report-param(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const reportParamSample = { rid: 'b4205924-162d-4660-a0c9-e364696f5d32' };

  let reportParam;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/report-params+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/report-params').as('postEntityRequest');
    cy.intercept('DELETE', '/api/report-params/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (reportParam) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/report-params/${reportParam.rid}`,
      }).then(() => {
        reportParam = undefined;
      });
    }
  });

  it('ReportParams menu should load ReportParams page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('report-param');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReportParam').should('exist');
    cy.url().should('match', reportParamPageUrlPattern);
  });

  describe('ReportParam page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(reportParamPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ReportParam page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/report-param/new$'));
        cy.getEntityCreateUpdateHeading('ReportParam');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportParamPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/report-params',
          body: reportParamSample,
        }).then(({ body }) => {
          reportParam = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/report-params+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [reportParam],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(reportParamPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ReportParam page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('reportParam');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportParamPageUrlPattern);
      });

      it('edit button click should load edit ReportParam page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReportParam');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportParamPageUrlPattern);
      });

      it('edit button click should load edit ReportParam page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReportParam');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportParamPageUrlPattern);
      });

      it('last delete button click should delete instance of ReportParam', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('reportParam').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportParamPageUrlPattern);

        reportParam = undefined;
      });
    });
  });

  describe('new ReportParam page', () => {
    beforeEach(() => {
      cy.visit(`${reportParamPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ReportParam');
    });

    it('should create an instance of ReportParam', () => {
      cy.get(`[data-cy="rid"]`).type('bb3790aa-f904-4a55-9b92-2fa7b775452b');
      cy.get(`[data-cy="rid"]`).should('have.value', 'bb3790aa-f904-4a55-9b92-2fa7b775452b');

      cy.get(`[data-cy="name"]`).type('bleak commemorate of');
      cy.get(`[data-cy="name"]`).should('have.value', 'bleak commemorate of');

      cy.get(`[data-cy="type"]`).type('and');
      cy.get(`[data-cy="type"]`).should('have.value', 'and');

      cy.get(`[data-cy="value"]`).type('yet notwithstanding');
      cy.get(`[data-cy="value"]`).should('have.value', 'yet notwithstanding');

      cy.get(`[data-cy="conversionRule"]`).type('composed');
      cy.get(`[data-cy="conversionRule"]`).should('have.value', 'composed');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        reportParam = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', reportParamPageUrlPattern);
    });
  });
});

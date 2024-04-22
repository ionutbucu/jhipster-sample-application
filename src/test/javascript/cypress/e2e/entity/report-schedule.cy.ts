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

describe('ReportSchedule e2e test', () => {
  const reportSchedulePageUrl = '/report-schedule';
  const reportSchedulePageUrlPattern = new RegExp('/report-schedule(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const reportScheduleSample = { cron: 'lazily aha evergreen' };

  let reportSchedule;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/report-schedules+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/report-schedules').as('postEntityRequest');
    cy.intercept('DELETE', '/api/report-schedules/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (reportSchedule) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/report-schedules/${reportSchedule.rid}`,
      }).then(() => {
        reportSchedule = undefined;
      });
    }
  });

  it('ReportSchedules menu should load ReportSchedules page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('report-schedule');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReportSchedule').should('exist');
    cy.url().should('match', reportSchedulePageUrlPattern);
  });

  describe('ReportSchedule page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(reportSchedulePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ReportSchedule page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/report-schedule/new$'));
        cy.getEntityCreateUpdateHeading('ReportSchedule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportSchedulePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/report-schedules',
          body: reportScheduleSample,
        }).then(({ body }) => {
          reportSchedule = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/report-schedules+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [reportSchedule],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(reportSchedulePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ReportSchedule page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('reportSchedule');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportSchedulePageUrlPattern);
      });

      it('edit button click should load edit ReportSchedule page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReportSchedule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportSchedulePageUrlPattern);
      });

      it('edit button click should load edit ReportSchedule page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReportSchedule');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportSchedulePageUrlPattern);
      });

      it('last delete button click should delete instance of ReportSchedule', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('reportSchedule').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportSchedulePageUrlPattern);

        reportSchedule = undefined;
      });
    });
  });

  describe('new ReportSchedule page', () => {
    beforeEach(() => {
      cy.visit(`${reportSchedulePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ReportSchedule');
    });

    it('should create an instance of ReportSchedule', () => {
      cy.get(`[data-cy="rid"]`).type('71b32a22-0c6c-4411-b107-4968f03fef4e');
      cy.get(`[data-cy="rid"]`).should('have.value', '71b32a22-0c6c-4411-b107-4968f03fef4e');

      cy.get(`[data-cy="cron"]`).type('harbour');
      cy.get(`[data-cy="cron"]`).should('have.value', 'harbour');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        reportSchedule = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', reportSchedulePageUrlPattern);
    });
  });
});

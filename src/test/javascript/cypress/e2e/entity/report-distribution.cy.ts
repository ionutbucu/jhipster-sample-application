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

describe('ReportDistribution e2e test', () => {
  const reportDistributionPageUrl = '/report-distribution';
  const reportDistributionPageUrlPattern = new RegExp('/report-distribution(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const reportDistributionSample = { email: "CRO@mZNm8'" };

  let reportDistribution;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/report-distributions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/report-distributions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/report-distributions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (reportDistribution) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/report-distributions/${reportDistribution.rid}`,
      }).then(() => {
        reportDistribution = undefined;
      });
    }
  });

  it('ReportDistributions menu should load ReportDistributions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('report-distribution');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReportDistribution').should('exist');
    cy.url().should('match', reportDistributionPageUrlPattern);
  });

  describe('ReportDistribution page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(reportDistributionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ReportDistribution page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/report-distribution/new$'));
        cy.getEntityCreateUpdateHeading('ReportDistribution');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportDistributionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/report-distributions',
          body: reportDistributionSample,
        }).then(({ body }) => {
          reportDistribution = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/report-distributions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [reportDistribution],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(reportDistributionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ReportDistribution page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('reportDistribution');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportDistributionPageUrlPattern);
      });

      it('edit button click should load edit ReportDistribution page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReportDistribution');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportDistributionPageUrlPattern);
      });

      it('edit button click should load edit ReportDistribution page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReportDistribution');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportDistributionPageUrlPattern);
      });

      it('last delete button click should delete instance of ReportDistribution', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('reportDistribution').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportDistributionPageUrlPattern);

        reportDistribution = undefined;
      });
    });
  });

  describe('new ReportDistribution page', () => {
    beforeEach(() => {
      cy.visit(`${reportDistributionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ReportDistribution');
    });

    it('should create an instance of ReportDistribution', () => {
      cy.get(`[data-cy="rid"]`).type('55416286-d6c1-4fa5-86e9-7a9324ee64eb');
      cy.get(`[data-cy="rid"]`).should('have.value', '55416286-d6c1-4fa5-86e9-7a9324ee64eb');

      cy.get(`[data-cy="email"]`).type('[==@R}1');
      cy.get(`[data-cy="email"]`).should('have.value', '[==@R}1');

      cy.get(`[data-cy="description"]`).type('well-off drat');
      cy.get(`[data-cy="description"]`).should('have.value', 'well-off drat');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        reportDistribution = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', reportDistributionPageUrlPattern);
    });
  });
});

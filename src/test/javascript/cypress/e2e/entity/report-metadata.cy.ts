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

describe('ReportMetadata e2e test', () => {
  const reportMetadataPageUrl = '/report-metadata';
  const reportMetadataPageUrlPattern = new RegExp('/report-metadata(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const reportMetadataSample = { rid: 'f6f530e1-8f53-4730-a038-b22ed774c0ad' };

  let reportMetadata;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/report-metadata+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/report-metadata').as('postEntityRequest');
    cy.intercept('DELETE', '/api/report-metadata/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (reportMetadata) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/report-metadata/${reportMetadata.rid}`,
      }).then(() => {
        reportMetadata = undefined;
      });
    }
  });

  it('ReportMetadata menu should load ReportMetadata page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('report-metadata');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReportMetadata').should('exist');
    cy.url().should('match', reportMetadataPageUrlPattern);
  });

  describe('ReportMetadata page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(reportMetadataPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ReportMetadata page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/report-metadata/new$'));
        cy.getEntityCreateUpdateHeading('ReportMetadata');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportMetadataPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/report-metadata',
          body: reportMetadataSample,
        }).then(({ body }) => {
          reportMetadata = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/report-metadata+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [reportMetadata],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(reportMetadataPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ReportMetadata page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('reportMetadata');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportMetadataPageUrlPattern);
      });

      it('edit button click should load edit ReportMetadata page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReportMetadata');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportMetadataPageUrlPattern);
      });

      it('edit button click should load edit ReportMetadata page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReportMetadata');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportMetadataPageUrlPattern);
      });

      it('last delete button click should delete instance of ReportMetadata', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('reportMetadata').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportMetadataPageUrlPattern);

        reportMetadata = undefined;
      });
    });
  });

  describe('new ReportMetadata page', () => {
    beforeEach(() => {
      cy.visit(`${reportMetadataPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ReportMetadata');
    });

    it('should create an instance of ReportMetadata', () => {
      cy.get(`[data-cy="rid"]`).type('9a45b562-ee3c-44e9-bb17-b7ea23e9c279');
      cy.get(`[data-cy="rid"]`).should('have.value', '9a45b562-ee3c-44e9-bb17-b7ea23e9c279');

      cy.get(`[data-cy="metadata"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="metadata"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        reportMetadata = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', reportMetadataPageUrlPattern);
    });
  });
});

import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReportDistribution } from '../report-distribution.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../report-distribution.test-samples';

import { ReportDistributionService } from './report-distribution.service';

const requireRestSample: IReportDistribution = {
  ...sampleWithRequiredData,
};

describe('ReportDistribution Service', () => {
  let service: ReportDistributionService;
  let httpMock: HttpTestingController;
  let expectedResult: IReportDistribution | IReportDistribution[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReportDistributionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ReportDistribution', () => {
      const reportDistribution = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reportDistribution).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportDistribution', () => {
      const reportDistribution = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reportDistribution).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportDistribution', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportDistribution', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReportDistribution', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReportDistributionToCollectionIfMissing', () => {
      it('should add a ReportDistribution to an empty array', () => {
        const reportDistribution: IReportDistribution = sampleWithRequiredData;
        expectedResult = service.addReportDistributionToCollectionIfMissing([], reportDistribution);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportDistribution);
      });

      it('should not add a ReportDistribution to an array that contains it', () => {
        const reportDistribution: IReportDistribution = sampleWithRequiredData;
        const reportDistributionCollection: IReportDistribution[] = [
          {
            ...reportDistribution,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReportDistributionToCollectionIfMissing(reportDistributionCollection, reportDistribution);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportDistribution to an array that doesn't contain it", () => {
        const reportDistribution: IReportDistribution = sampleWithRequiredData;
        const reportDistributionCollection: IReportDistribution[] = [sampleWithPartialData];
        expectedResult = service.addReportDistributionToCollectionIfMissing(reportDistributionCollection, reportDistribution);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportDistribution);
      });

      it('should add only unique ReportDistribution to an array', () => {
        const reportDistributionArray: IReportDistribution[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const reportDistributionCollection: IReportDistribution[] = [sampleWithRequiredData];
        expectedResult = service.addReportDistributionToCollectionIfMissing(reportDistributionCollection, ...reportDistributionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportDistribution: IReportDistribution = sampleWithRequiredData;
        const reportDistribution2: IReportDistribution = sampleWithPartialData;
        expectedResult = service.addReportDistributionToCollectionIfMissing([], reportDistribution, reportDistribution2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportDistribution);
        expect(expectedResult).toContain(reportDistribution2);
      });

      it('should accept null and undefined values', () => {
        const reportDistribution: IReportDistribution = sampleWithRequiredData;
        expectedResult = service.addReportDistributionToCollectionIfMissing([], null, reportDistribution, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportDistribution);
      });

      it('should return initial array if no ReportDistribution is added', () => {
        const reportDistributionCollection: IReportDistribution[] = [sampleWithRequiredData];
        expectedResult = service.addReportDistributionToCollectionIfMissing(reportDistributionCollection, undefined, null);
        expect(expectedResult).toEqual(reportDistributionCollection);
      });
    });

    describe('compareReportDistribution', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReportDistribution(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { rid: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareReportDistribution(entity1, entity2);
        const compareResult2 = service.compareReportDistribution(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { rid: 'ABC' };
        const entity2 = { rid: 'CBA' };

        const compareResult1 = service.compareReportDistribution(entity1, entity2);
        const compareResult2 = service.compareReportDistribution(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { rid: 'ABC' };
        const entity2 = { rid: 'ABC' };

        const compareResult1 = service.compareReportDistribution(entity1, entity2);
        const compareResult2 = service.compareReportDistribution(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

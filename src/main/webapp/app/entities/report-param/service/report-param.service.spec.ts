import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReportParam } from '../report-param.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../report-param.test-samples';

import { ReportParamService } from './report-param.service';

const requireRestSample: IReportParam = {
  ...sampleWithRequiredData,
};

describe('ReportParam Service', () => {
  let service: ReportParamService;
  let httpMock: HttpTestingController;
  let expectedResult: IReportParam | IReportParam[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReportParamService);
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

    it('should create a ReportParam', () => {
      const reportParam = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reportParam).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportParam', () => {
      const reportParam = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reportParam).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportParam', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportParam', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReportParam', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReportParamToCollectionIfMissing', () => {
      it('should add a ReportParam to an empty array', () => {
        const reportParam: IReportParam = sampleWithRequiredData;
        expectedResult = service.addReportParamToCollectionIfMissing([], reportParam);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportParam);
      });

      it('should not add a ReportParam to an array that contains it', () => {
        const reportParam: IReportParam = sampleWithRequiredData;
        const reportParamCollection: IReportParam[] = [
          {
            ...reportParam,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReportParamToCollectionIfMissing(reportParamCollection, reportParam);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportParam to an array that doesn't contain it", () => {
        const reportParam: IReportParam = sampleWithRequiredData;
        const reportParamCollection: IReportParam[] = [sampleWithPartialData];
        expectedResult = service.addReportParamToCollectionIfMissing(reportParamCollection, reportParam);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportParam);
      });

      it('should add only unique ReportParam to an array', () => {
        const reportParamArray: IReportParam[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const reportParamCollection: IReportParam[] = [sampleWithRequiredData];
        expectedResult = service.addReportParamToCollectionIfMissing(reportParamCollection, ...reportParamArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportParam: IReportParam = sampleWithRequiredData;
        const reportParam2: IReportParam = sampleWithPartialData;
        expectedResult = service.addReportParamToCollectionIfMissing([], reportParam, reportParam2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportParam);
        expect(expectedResult).toContain(reportParam2);
      });

      it('should accept null and undefined values', () => {
        const reportParam: IReportParam = sampleWithRequiredData;
        expectedResult = service.addReportParamToCollectionIfMissing([], null, reportParam, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportParam);
      });

      it('should return initial array if no ReportParam is added', () => {
        const reportParamCollection: IReportParam[] = [sampleWithRequiredData];
        expectedResult = service.addReportParamToCollectionIfMissing(reportParamCollection, undefined, null);
        expect(expectedResult).toEqual(reportParamCollection);
      });
    });

    describe('compareReportParam', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReportParam(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { rid: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareReportParam(entity1, entity2);
        const compareResult2 = service.compareReportParam(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { rid: 'ABC' };
        const entity2 = { rid: 'CBA' };

        const compareResult1 = service.compareReportParam(entity1, entity2);
        const compareResult2 = service.compareReportParam(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { rid: 'ABC' };
        const entity2 = { rid: 'ABC' };

        const compareResult1 = service.compareReportParam(entity1, entity2);
        const compareResult2 = service.compareReportParam(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

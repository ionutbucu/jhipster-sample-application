import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReportColumnMapping } from '../report-column-mapping.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../report-column-mapping.test-samples';

import { ReportColumnMappingService } from './report-column-mapping.service';

const requireRestSample: IReportColumnMapping = {
  ...sampleWithRequiredData,
};

describe('ReportColumnMapping Service', () => {
  let service: ReportColumnMappingService;
  let httpMock: HttpTestingController;
  let expectedResult: IReportColumnMapping | IReportColumnMapping[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReportColumnMappingService);
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

    it('should create a ReportColumnMapping', () => {
      const reportColumnMapping = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reportColumnMapping).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportColumnMapping', () => {
      const reportColumnMapping = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reportColumnMapping).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportColumnMapping', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportColumnMapping', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReportColumnMapping', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReportColumnMappingToCollectionIfMissing', () => {
      it('should add a ReportColumnMapping to an empty array', () => {
        const reportColumnMapping: IReportColumnMapping = sampleWithRequiredData;
        expectedResult = service.addReportColumnMappingToCollectionIfMissing([], reportColumnMapping);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportColumnMapping);
      });

      it('should not add a ReportColumnMapping to an array that contains it', () => {
        const reportColumnMapping: IReportColumnMapping = sampleWithRequiredData;
        const reportColumnMappingCollection: IReportColumnMapping[] = [
          {
            ...reportColumnMapping,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReportColumnMappingToCollectionIfMissing(reportColumnMappingCollection, reportColumnMapping);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportColumnMapping to an array that doesn't contain it", () => {
        const reportColumnMapping: IReportColumnMapping = sampleWithRequiredData;
        const reportColumnMappingCollection: IReportColumnMapping[] = [sampleWithPartialData];
        expectedResult = service.addReportColumnMappingToCollectionIfMissing(reportColumnMappingCollection, reportColumnMapping);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportColumnMapping);
      });

      it('should add only unique ReportColumnMapping to an array', () => {
        const reportColumnMappingArray: IReportColumnMapping[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const reportColumnMappingCollection: IReportColumnMapping[] = [sampleWithRequiredData];
        expectedResult = service.addReportColumnMappingToCollectionIfMissing(reportColumnMappingCollection, ...reportColumnMappingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportColumnMapping: IReportColumnMapping = sampleWithRequiredData;
        const reportColumnMapping2: IReportColumnMapping = sampleWithPartialData;
        expectedResult = service.addReportColumnMappingToCollectionIfMissing([], reportColumnMapping, reportColumnMapping2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportColumnMapping);
        expect(expectedResult).toContain(reportColumnMapping2);
      });

      it('should accept null and undefined values', () => {
        const reportColumnMapping: IReportColumnMapping = sampleWithRequiredData;
        expectedResult = service.addReportColumnMappingToCollectionIfMissing([], null, reportColumnMapping, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportColumnMapping);
      });

      it('should return initial array if no ReportColumnMapping is added', () => {
        const reportColumnMappingCollection: IReportColumnMapping[] = [sampleWithRequiredData];
        expectedResult = service.addReportColumnMappingToCollectionIfMissing(reportColumnMappingCollection, undefined, null);
        expect(expectedResult).toEqual(reportColumnMappingCollection);
      });
    });

    describe('compareReportColumnMapping', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReportColumnMapping(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { rid: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareReportColumnMapping(entity1, entity2);
        const compareResult2 = service.compareReportColumnMapping(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { rid: 'ABC' };
        const entity2 = { rid: 'CBA' };

        const compareResult1 = service.compareReportColumnMapping(entity1, entity2);
        const compareResult2 = service.compareReportColumnMapping(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { rid: 'ABC' };
        const entity2 = { rid: 'ABC' };

        const compareResult1 = service.compareReportColumnMapping(entity1, entity2);
        const compareResult2 = service.compareReportColumnMapping(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

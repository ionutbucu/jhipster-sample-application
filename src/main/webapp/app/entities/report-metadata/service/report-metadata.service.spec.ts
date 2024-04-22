import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReportMetadata } from '../report-metadata.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../report-metadata.test-samples';

import { ReportMetadataService } from './report-metadata.service';

const requireRestSample: IReportMetadata = {
  ...sampleWithRequiredData,
};

describe('ReportMetadata Service', () => {
  let service: ReportMetadataService;
  let httpMock: HttpTestingController;
  let expectedResult: IReportMetadata | IReportMetadata[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReportMetadataService);
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

    it('should create a ReportMetadata', () => {
      const reportMetadata = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reportMetadata).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportMetadata', () => {
      const reportMetadata = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reportMetadata).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportMetadata', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportMetadata', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReportMetadata', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReportMetadataToCollectionIfMissing', () => {
      it('should add a ReportMetadata to an empty array', () => {
        const reportMetadata: IReportMetadata = sampleWithRequiredData;
        expectedResult = service.addReportMetadataToCollectionIfMissing([], reportMetadata);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportMetadata);
      });

      it('should not add a ReportMetadata to an array that contains it', () => {
        const reportMetadata: IReportMetadata = sampleWithRequiredData;
        const reportMetadataCollection: IReportMetadata[] = [
          {
            ...reportMetadata,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReportMetadataToCollectionIfMissing(reportMetadataCollection, reportMetadata);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportMetadata to an array that doesn't contain it", () => {
        const reportMetadata: IReportMetadata = sampleWithRequiredData;
        const reportMetadataCollection: IReportMetadata[] = [sampleWithPartialData];
        expectedResult = service.addReportMetadataToCollectionIfMissing(reportMetadataCollection, reportMetadata);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportMetadata);
      });

      it('should add only unique ReportMetadata to an array', () => {
        const reportMetadataArray: IReportMetadata[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const reportMetadataCollection: IReportMetadata[] = [sampleWithRequiredData];
        expectedResult = service.addReportMetadataToCollectionIfMissing(reportMetadataCollection, ...reportMetadataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportMetadata: IReportMetadata = sampleWithRequiredData;
        const reportMetadata2: IReportMetadata = sampleWithPartialData;
        expectedResult = service.addReportMetadataToCollectionIfMissing([], reportMetadata, reportMetadata2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportMetadata);
        expect(expectedResult).toContain(reportMetadata2);
      });

      it('should accept null and undefined values', () => {
        const reportMetadata: IReportMetadata = sampleWithRequiredData;
        expectedResult = service.addReportMetadataToCollectionIfMissing([], null, reportMetadata, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportMetadata);
      });

      it('should return initial array if no ReportMetadata is added', () => {
        const reportMetadataCollection: IReportMetadata[] = [sampleWithRequiredData];
        expectedResult = service.addReportMetadataToCollectionIfMissing(reportMetadataCollection, undefined, null);
        expect(expectedResult).toEqual(reportMetadataCollection);
      });
    });

    describe('compareReportMetadata', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReportMetadata(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { rid: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareReportMetadata(entity1, entity2);
        const compareResult2 = service.compareReportMetadata(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { rid: 'ABC' };
        const entity2 = { rid: 'CBA' };

        const compareResult1 = service.compareReportMetadata(entity1, entity2);
        const compareResult2 = service.compareReportMetadata(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { rid: 'ABC' };
        const entity2 = { rid: 'ABC' };

        const compareResult1 = service.compareReportMetadata(entity1, entity2);
        const compareResult2 = service.compareReportMetadata(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

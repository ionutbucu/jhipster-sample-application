import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReportDataSource } from '../report-data-source.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../report-data-source.test-samples';

import { ReportDataSourceService } from './report-data-source.service';

const requireRestSample: IReportDataSource = {
  ...sampleWithRequiredData,
};

describe('ReportDataSource Service', () => {
  let service: ReportDataSourceService;
  let httpMock: HttpTestingController;
  let expectedResult: IReportDataSource | IReportDataSource[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReportDataSourceService);
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

    it('should create a ReportDataSource', () => {
      const reportDataSource = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reportDataSource).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportDataSource', () => {
      const reportDataSource = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reportDataSource).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportDataSource', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportDataSource', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReportDataSource', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReportDataSourceToCollectionIfMissing', () => {
      it('should add a ReportDataSource to an empty array', () => {
        const reportDataSource: IReportDataSource = sampleWithRequiredData;
        expectedResult = service.addReportDataSourceToCollectionIfMissing([], reportDataSource);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportDataSource);
      });

      it('should not add a ReportDataSource to an array that contains it', () => {
        const reportDataSource: IReportDataSource = sampleWithRequiredData;
        const reportDataSourceCollection: IReportDataSource[] = [
          {
            ...reportDataSource,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReportDataSourceToCollectionIfMissing(reportDataSourceCollection, reportDataSource);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportDataSource to an array that doesn't contain it", () => {
        const reportDataSource: IReportDataSource = sampleWithRequiredData;
        const reportDataSourceCollection: IReportDataSource[] = [sampleWithPartialData];
        expectedResult = service.addReportDataSourceToCollectionIfMissing(reportDataSourceCollection, reportDataSource);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportDataSource);
      });

      it('should add only unique ReportDataSource to an array', () => {
        const reportDataSourceArray: IReportDataSource[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const reportDataSourceCollection: IReportDataSource[] = [sampleWithRequiredData];
        expectedResult = service.addReportDataSourceToCollectionIfMissing(reportDataSourceCollection, ...reportDataSourceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportDataSource: IReportDataSource = sampleWithRequiredData;
        const reportDataSource2: IReportDataSource = sampleWithPartialData;
        expectedResult = service.addReportDataSourceToCollectionIfMissing([], reportDataSource, reportDataSource2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportDataSource);
        expect(expectedResult).toContain(reportDataSource2);
      });

      it('should accept null and undefined values', () => {
        const reportDataSource: IReportDataSource = sampleWithRequiredData;
        expectedResult = service.addReportDataSourceToCollectionIfMissing([], null, reportDataSource, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportDataSource);
      });

      it('should return initial array if no ReportDataSource is added', () => {
        const reportDataSourceCollection: IReportDataSource[] = [sampleWithRequiredData];
        expectedResult = service.addReportDataSourceToCollectionIfMissing(reportDataSourceCollection, undefined, null);
        expect(expectedResult).toEqual(reportDataSourceCollection);
      });
    });

    describe('compareReportDataSource', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReportDataSource(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { rid: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareReportDataSource(entity1, entity2);
        const compareResult2 = service.compareReportDataSource(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { rid: 'ABC' };
        const entity2 = { rid: 'CBA' };

        const compareResult1 = service.compareReportDataSource(entity1, entity2);
        const compareResult2 = service.compareReportDataSource(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { rid: 'ABC' };
        const entity2 = { rid: 'ABC' };

        const compareResult1 = service.compareReportDataSource(entity1, entity2);
        const compareResult2 = service.compareReportDataSource(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReportSchedule } from '../report-schedule.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../report-schedule.test-samples';

import { ReportScheduleService } from './report-schedule.service';

const requireRestSample: IReportSchedule = {
  ...sampleWithRequiredData,
};

describe('ReportSchedule Service', () => {
  let service: ReportScheduleService;
  let httpMock: HttpTestingController;
  let expectedResult: IReportSchedule | IReportSchedule[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReportScheduleService);
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

    it('should create a ReportSchedule', () => {
      const reportSchedule = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reportSchedule).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportSchedule', () => {
      const reportSchedule = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reportSchedule).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportSchedule', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportSchedule', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReportSchedule', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReportScheduleToCollectionIfMissing', () => {
      it('should add a ReportSchedule to an empty array', () => {
        const reportSchedule: IReportSchedule = sampleWithRequiredData;
        expectedResult = service.addReportScheduleToCollectionIfMissing([], reportSchedule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportSchedule);
      });

      it('should not add a ReportSchedule to an array that contains it', () => {
        const reportSchedule: IReportSchedule = sampleWithRequiredData;
        const reportScheduleCollection: IReportSchedule[] = [
          {
            ...reportSchedule,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReportScheduleToCollectionIfMissing(reportScheduleCollection, reportSchedule);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportSchedule to an array that doesn't contain it", () => {
        const reportSchedule: IReportSchedule = sampleWithRequiredData;
        const reportScheduleCollection: IReportSchedule[] = [sampleWithPartialData];
        expectedResult = service.addReportScheduleToCollectionIfMissing(reportScheduleCollection, reportSchedule);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportSchedule);
      });

      it('should add only unique ReportSchedule to an array', () => {
        const reportScheduleArray: IReportSchedule[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const reportScheduleCollection: IReportSchedule[] = [sampleWithRequiredData];
        expectedResult = service.addReportScheduleToCollectionIfMissing(reportScheduleCollection, ...reportScheduleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportSchedule: IReportSchedule = sampleWithRequiredData;
        const reportSchedule2: IReportSchedule = sampleWithPartialData;
        expectedResult = service.addReportScheduleToCollectionIfMissing([], reportSchedule, reportSchedule2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportSchedule);
        expect(expectedResult).toContain(reportSchedule2);
      });

      it('should accept null and undefined values', () => {
        const reportSchedule: IReportSchedule = sampleWithRequiredData;
        expectedResult = service.addReportScheduleToCollectionIfMissing([], null, reportSchedule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportSchedule);
      });

      it('should return initial array if no ReportSchedule is added', () => {
        const reportScheduleCollection: IReportSchedule[] = [sampleWithRequiredData];
        expectedResult = service.addReportScheduleToCollectionIfMissing(reportScheduleCollection, undefined, null);
        expect(expectedResult).toEqual(reportScheduleCollection);
      });
    });

    describe('compareReportSchedule', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReportSchedule(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { rid: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareReportSchedule(entity1, entity2);
        const compareResult2 = service.compareReportSchedule(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { rid: 'ABC' };
        const entity2 = { rid: 'CBA' };

        const compareResult1 = service.compareReportSchedule(entity1, entity2);
        const compareResult2 = service.compareReportSchedule(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { rid: 'ABC' };
        const entity2 = { rid: 'ABC' };

        const compareResult1 = service.compareReportSchedule(entity1, entity2);
        const compareResult2 = service.compareReportSchedule(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

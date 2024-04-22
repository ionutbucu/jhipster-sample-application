import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IReportSchedule } from '../report-schedule.model';
import { ReportScheduleService } from '../service/report-schedule.service';

import reportScheduleResolve from './report-schedule-routing-resolve.service';

describe('ReportSchedule routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: ReportScheduleService;
  let resultReportSchedule: IReportSchedule | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(ReportScheduleService);
    resultReportSchedule = undefined;
  });

  describe('resolve', () => {
    it('should return IReportSchedule returned by find', () => {
      // GIVEN
      service.find = jest.fn(rid => of(new HttpResponse({ body: { rid } })));
      mockActivatedRouteSnapshot.params = { rid: 'ABC' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        reportScheduleResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReportSchedule = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultReportSchedule).toEqual({ rid: 'ABC' });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        reportScheduleResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReportSchedule = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultReportSchedule).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IReportSchedule>({ body: null })));
      mockActivatedRouteSnapshot.params = { rid: 'ABC' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        reportScheduleResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReportSchedule = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultReportSchedule).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});

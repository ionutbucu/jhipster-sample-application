import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IReportExecution } from '../report-execution.model';
import { ReportExecutionService } from '../service/report-execution.service';

import reportExecutionResolve from './report-execution-routing-resolve.service';

describe('ReportExecution routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: ReportExecutionService;
  let resultReportExecution: IReportExecution | null | undefined;

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
    service = TestBed.inject(ReportExecutionService);
    resultReportExecution = undefined;
  });

  describe('resolve', () => {
    it('should return IReportExecution returned by find', () => {
      // GIVEN
      service.find = jest.fn(rid => of(new HttpResponse({ body: { rid } })));
      mockActivatedRouteSnapshot.params = { rid: 'ABC' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        reportExecutionResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReportExecution = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultReportExecution).toEqual({ rid: 'ABC' });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        reportExecutionResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReportExecution = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultReportExecution).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IReportExecution>({ body: null })));
      mockActivatedRouteSnapshot.params = { rid: 'ABC' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        reportExecutionResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReportExecution = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultReportExecution).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});

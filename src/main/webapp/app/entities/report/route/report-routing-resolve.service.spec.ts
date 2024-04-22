import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IReport } from '../report.model';
import { ReportService } from '../service/report.service';

import reportResolve from './report-routing-resolve.service';

describe('Report routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: ReportService;
  let resultReport: IReport | null | undefined;

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
    service = TestBed.inject(ReportService);
    resultReport = undefined;
  });

  describe('resolve', () => {
    it('should return IReport returned by find', () => {
      // GIVEN
      service.find = jest.fn(rid => of(new HttpResponse({ body: { rid } })));
      mockActivatedRouteSnapshot.params = { rid: 'ABC' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        reportResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReport = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultReport).toEqual({ rid: 'ABC' });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        reportResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReport = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultReport).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IReport>({ body: null })));
      mockActivatedRouteSnapshot.params = { rid: 'ABC' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        reportResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReport = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultReport).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});

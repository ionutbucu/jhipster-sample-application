import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IReportDataSource } from '../report-data-source.model';
import { ReportDataSourceService } from '../service/report-data-source.service';

import reportDataSourceResolve from './report-data-source-routing-resolve.service';

describe('ReportDataSource routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: ReportDataSourceService;
  let resultReportDataSource: IReportDataSource | null | undefined;

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
    service = TestBed.inject(ReportDataSourceService);
    resultReportDataSource = undefined;
  });

  describe('resolve', () => {
    it('should return IReportDataSource returned by find', () => {
      // GIVEN
      service.find = jest.fn(rid => of(new HttpResponse({ body: { rid } })));
      mockActivatedRouteSnapshot.params = { rid: 'ABC' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        reportDataSourceResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReportDataSource = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultReportDataSource).toEqual({ rid: 'ABC' });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        reportDataSourceResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReportDataSource = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultReportDataSource).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IReportDataSource>({ body: null })));
      mockActivatedRouteSnapshot.params = { rid: 'ABC' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        reportDataSourceResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReportDataSource = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultReportDataSource).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});

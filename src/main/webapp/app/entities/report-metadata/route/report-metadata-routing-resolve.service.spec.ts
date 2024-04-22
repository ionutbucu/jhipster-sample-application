import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IReportMetadata } from '../report-metadata.model';
import { ReportMetadataService } from '../service/report-metadata.service';

import reportMetadataResolve from './report-metadata-routing-resolve.service';

describe('ReportMetadata routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: ReportMetadataService;
  let resultReportMetadata: IReportMetadata | null | undefined;

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
    service = TestBed.inject(ReportMetadataService);
    resultReportMetadata = undefined;
  });

  describe('resolve', () => {
    it('should return IReportMetadata returned by find', () => {
      // GIVEN
      service.find = jest.fn(rid => of(new HttpResponse({ body: { rid } })));
      mockActivatedRouteSnapshot.params = { rid: 'ABC' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        reportMetadataResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReportMetadata = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultReportMetadata).toEqual({ rid: 'ABC' });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        reportMetadataResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReportMetadata = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultReportMetadata).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IReportMetadata>({ body: null })));
      mockActivatedRouteSnapshot.params = { rid: 'ABC' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        reportMetadataResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReportMetadata = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultReportMetadata).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});

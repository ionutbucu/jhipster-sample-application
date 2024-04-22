import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../report-data-source.test-samples';

import { ReportDataSourceFormService } from './report-data-source-form.service';

describe('ReportDataSource Form Service', () => {
  let service: ReportDataSourceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportDataSourceFormService);
  });

  describe('Service methods', () => {
    describe('createReportDataSourceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReportDataSourceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            rid: expect.any(Object),
            type: expect.any(Object),
            url: expect.any(Object),
            user: expect.any(Object),
            password: expect.any(Object),
          }),
        );
      });

      it('passing IReportDataSource should create a new form with FormGroup', () => {
        const formGroup = service.createReportDataSourceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            rid: expect.any(Object),
            type: expect.any(Object),
            url: expect.any(Object),
            user: expect.any(Object),
            password: expect.any(Object),
          }),
        );
      });
    });

    describe('getReportDataSource', () => {
      it('should return NewReportDataSource for default ReportDataSource initial value', () => {
        const formGroup = service.createReportDataSourceFormGroup(sampleWithNewData);

        const reportDataSource = service.getReportDataSource(formGroup) as any;

        expect(reportDataSource).toMatchObject(sampleWithNewData);
      });

      it('should return NewReportDataSource for empty ReportDataSource initial value', () => {
        const formGroup = service.createReportDataSourceFormGroup();

        const reportDataSource = service.getReportDataSource(formGroup) as any;

        expect(reportDataSource).toMatchObject({});
      });

      it('should return IReportDataSource', () => {
        const formGroup = service.createReportDataSourceFormGroup(sampleWithRequiredData);

        const reportDataSource = service.getReportDataSource(formGroup) as any;

        expect(reportDataSource).toMatchObject(sampleWithRequiredData);
      });
    });
  });
});

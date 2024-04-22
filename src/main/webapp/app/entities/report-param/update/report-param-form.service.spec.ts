import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../report-param.test-samples';

import { ReportParamFormService } from './report-param-form.service';

describe('ReportParam Form Service', () => {
  let service: ReportParamFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportParamFormService);
  });

  describe('Service methods', () => {
    describe('createReportParamFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReportParamFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            rid: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            value: expect.any(Object),
            conversionRule: expect.any(Object),
            report: expect.any(Object),
          }),
        );
      });

      it('passing IReportParam should create a new form with FormGroup', () => {
        const formGroup = service.createReportParamFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            rid: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            value: expect.any(Object),
            conversionRule: expect.any(Object),
            report: expect.any(Object),
          }),
        );
      });
    });

    describe('getReportParam', () => {
      it('should return NewReportParam for default ReportParam initial value', () => {
        const formGroup = service.createReportParamFormGroup(sampleWithNewData);

        const reportParam = service.getReportParam(formGroup) as any;

        expect(reportParam).toMatchObject(sampleWithNewData);
      });

      it('should return NewReportParam for empty ReportParam initial value', () => {
        const formGroup = service.createReportParamFormGroup();

        const reportParam = service.getReportParam(formGroup) as any;

        expect(reportParam).toMatchObject({});
      });

      it('should return IReportParam', () => {
        const formGroup = service.createReportParamFormGroup(sampleWithRequiredData);

        const reportParam = service.getReportParam(formGroup) as any;

        expect(reportParam).toMatchObject(sampleWithRequiredData);
      });
    });
  });
});

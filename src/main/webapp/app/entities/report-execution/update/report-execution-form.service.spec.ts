import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../report-execution.test-samples';

import { ReportExecutionFormService } from './report-execution-form.service';

describe('ReportExecution Form Service', () => {
  let service: ReportExecutionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportExecutionFormService);
  });

  describe('Service methods', () => {
    describe('createReportExecutionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReportExecutionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            rid: expect.any(Object),
            date: expect.any(Object),
            error: expect.any(Object),
            url: expect.any(Object),
            user: expect.any(Object),
            additionalInfo: expect.any(Object),
            report: expect.any(Object),
          }),
        );
      });

      it('passing IReportExecution should create a new form with FormGroup', () => {
        const formGroup = service.createReportExecutionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            rid: expect.any(Object),
            date: expect.any(Object),
            error: expect.any(Object),
            url: expect.any(Object),
            user: expect.any(Object),
            additionalInfo: expect.any(Object),
            report: expect.any(Object),
          }),
        );
      });
    });

    describe('getReportExecution', () => {
      it('should return NewReportExecution for default ReportExecution initial value', () => {
        const formGroup = service.createReportExecutionFormGroup(sampleWithNewData);

        const reportExecution = service.getReportExecution(formGroup) as any;

        expect(reportExecution).toMatchObject(sampleWithNewData);
      });

      it('should return NewReportExecution for empty ReportExecution initial value', () => {
        const formGroup = service.createReportExecutionFormGroup();

        const reportExecution = service.getReportExecution(formGroup) as any;

        expect(reportExecution).toMatchObject({});
      });

      it('should return IReportExecution', () => {
        const formGroup = service.createReportExecutionFormGroup(sampleWithRequiredData);

        const reportExecution = service.getReportExecution(formGroup) as any;

        expect(reportExecution).toMatchObject(sampleWithRequiredData);
      });
    });
  });
});

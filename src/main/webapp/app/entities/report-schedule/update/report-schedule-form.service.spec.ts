import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../report-schedule.test-samples';

import { ReportScheduleFormService } from './report-schedule-form.service';

describe('ReportSchedule Form Service', () => {
  let service: ReportScheduleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportScheduleFormService);
  });

  describe('Service methods', () => {
    describe('createReportScheduleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReportScheduleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            rid: expect.any(Object),
            cron: expect.any(Object),
            report: expect.any(Object),
          }),
        );
      });

      it('passing IReportSchedule should create a new form with FormGroup', () => {
        const formGroup = service.createReportScheduleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            rid: expect.any(Object),
            cron: expect.any(Object),
            report: expect.any(Object),
          }),
        );
      });
    });

    describe('getReportSchedule', () => {
      it('should return NewReportSchedule for default ReportSchedule initial value', () => {
        const formGroup = service.createReportScheduleFormGroup(sampleWithNewData);

        const reportSchedule = service.getReportSchedule(formGroup) as any;

        expect(reportSchedule).toMatchObject(sampleWithNewData);
      });

      it('should return NewReportSchedule for empty ReportSchedule initial value', () => {
        const formGroup = service.createReportScheduleFormGroup();

        const reportSchedule = service.getReportSchedule(formGroup) as any;

        expect(reportSchedule).toMatchObject({});
      });

      it('should return IReportSchedule', () => {
        const formGroup = service.createReportScheduleFormGroup(sampleWithRequiredData);

        const reportSchedule = service.getReportSchedule(formGroup) as any;

        expect(reportSchedule).toMatchObject(sampleWithRequiredData);
      });
    });
  });
});

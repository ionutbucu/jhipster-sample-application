import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../report-distribution.test-samples';

import { ReportDistributionFormService } from './report-distribution-form.service';

describe('ReportDistribution Form Service', () => {
  let service: ReportDistributionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportDistributionFormService);
  });

  describe('Service methods', () => {
    describe('createReportDistributionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReportDistributionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            rid: expect.any(Object),
            email: expect.any(Object),
            description: expect.any(Object),
            report: expect.any(Object),
          }),
        );
      });

      it('passing IReportDistribution should create a new form with FormGroup', () => {
        const formGroup = service.createReportDistributionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            rid: expect.any(Object),
            email: expect.any(Object),
            description: expect.any(Object),
            report: expect.any(Object),
          }),
        );
      });
    });

    describe('getReportDistribution', () => {
      it('should return NewReportDistribution for default ReportDistribution initial value', () => {
        const formGroup = service.createReportDistributionFormGroup(sampleWithNewData);

        const reportDistribution = service.getReportDistribution(formGroup) as any;

        expect(reportDistribution).toMatchObject(sampleWithNewData);
      });

      it('should return NewReportDistribution for empty ReportDistribution initial value', () => {
        const formGroup = service.createReportDistributionFormGroup();

        const reportDistribution = service.getReportDistribution(formGroup) as any;

        expect(reportDistribution).toMatchObject({});
      });

      it('should return IReportDistribution', () => {
        const formGroup = service.createReportDistributionFormGroup(sampleWithRequiredData);

        const reportDistribution = service.getReportDistribution(formGroup) as any;

        expect(reportDistribution).toMatchObject(sampleWithRequiredData);
      });
    });
  });
});

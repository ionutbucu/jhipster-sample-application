import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../report.test-samples';

import { ReportFormService } from './report-form.service';

describe('Report Form Service', () => {
  let service: ReportFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportFormService);
  });

  describe('Service methods', () => {
    describe('createReportFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReportFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            rid: expect.any(Object),
            cid: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            query: expect.any(Object),
            queryType: expect.any(Object),
            fileName: expect.any(Object),
            reportType: expect.any(Object),
            licenseHolder: expect.any(Object),
            owner: expect.any(Object),
            datasource: expect.any(Object),
            metadata: expect.any(Object),
          }),
        );
      });

      it('passing IReport should create a new form with FormGroup', () => {
        const formGroup = service.createReportFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            rid: expect.any(Object),
            cid: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            query: expect.any(Object),
            queryType: expect.any(Object),
            fileName: expect.any(Object),
            reportType: expect.any(Object),
            licenseHolder: expect.any(Object),
            owner: expect.any(Object),
            datasource: expect.any(Object),
            metadata: expect.any(Object),
          }),
        );
      });
    });

    describe('getReport', () => {
      it('should return NewReport for default Report initial value', () => {
        const formGroup = service.createReportFormGroup(sampleWithNewData);

        const report = service.getReport(formGroup) as any;

        expect(report).toMatchObject(sampleWithNewData);
      });

      it('should return NewReport for empty Report initial value', () => {
        const formGroup = service.createReportFormGroup();

        const report = service.getReport(formGroup) as any;

        expect(report).toMatchObject({});
      });

      it('should return IReport', () => {
        const formGroup = service.createReportFormGroup(sampleWithRequiredData);

        const report = service.getReport(formGroup) as any;

        expect(report).toMatchObject(sampleWithRequiredData);
      });
    });
  });
});

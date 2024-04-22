import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../report-column-mapping.test-samples';

import { ReportColumnMappingFormService } from './report-column-mapping-form.service';

describe('ReportColumnMapping Form Service', () => {
  let service: ReportColumnMappingFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportColumnMappingFormService);
  });

  describe('Service methods', () => {
    describe('createReportColumnMappingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReportColumnMappingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            rid: expect.any(Object),
            sourceColumnName: expect.any(Object),
            sourceColumnIndex: expect.any(Object),
            columnTitle: expect.any(Object),
            lang: expect.any(Object),
            report: expect.any(Object),
          }),
        );
      });

      it('passing IReportColumnMapping should create a new form with FormGroup', () => {
        const formGroup = service.createReportColumnMappingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            rid: expect.any(Object),
            sourceColumnName: expect.any(Object),
            sourceColumnIndex: expect.any(Object),
            columnTitle: expect.any(Object),
            lang: expect.any(Object),
            report: expect.any(Object),
          }),
        );
      });
    });

    describe('getReportColumnMapping', () => {
      it('should return NewReportColumnMapping for default ReportColumnMapping initial value', () => {
        const formGroup = service.createReportColumnMappingFormGroup(sampleWithNewData);

        const reportColumnMapping = service.getReportColumnMapping(formGroup) as any;

        expect(reportColumnMapping).toMatchObject(sampleWithNewData);
      });

      it('should return NewReportColumnMapping for empty ReportColumnMapping initial value', () => {
        const formGroup = service.createReportColumnMappingFormGroup();

        const reportColumnMapping = service.getReportColumnMapping(formGroup) as any;

        expect(reportColumnMapping).toMatchObject({});
      });

      it('should return IReportColumnMapping', () => {
        const formGroup = service.createReportColumnMappingFormGroup(sampleWithRequiredData);

        const reportColumnMapping = service.getReportColumnMapping(formGroup) as any;

        expect(reportColumnMapping).toMatchObject(sampleWithRequiredData);
      });
    });
  });
});

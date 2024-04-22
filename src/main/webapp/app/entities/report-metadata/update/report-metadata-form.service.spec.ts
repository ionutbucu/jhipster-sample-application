import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../report-metadata.test-samples';

import { ReportMetadataFormService } from './report-metadata-form.service';

describe('ReportMetadata Form Service', () => {
  let service: ReportMetadataFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportMetadataFormService);
  });

  describe('Service methods', () => {
    describe('createReportMetadataFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReportMetadataFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            rid: expect.any(Object),
            metadata: expect.any(Object),
          }),
        );
      });

      it('passing IReportMetadata should create a new form with FormGroup', () => {
        const formGroup = service.createReportMetadataFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            rid: expect.any(Object),
            metadata: expect.any(Object),
          }),
        );
      });
    });

    describe('getReportMetadata', () => {
      it('should return NewReportMetadata for default ReportMetadata initial value', () => {
        const formGroup = service.createReportMetadataFormGroup(sampleWithNewData);

        const reportMetadata = service.getReportMetadata(formGroup) as any;

        expect(reportMetadata).toMatchObject(sampleWithNewData);
      });

      it('should return NewReportMetadata for empty ReportMetadata initial value', () => {
        const formGroup = service.createReportMetadataFormGroup();

        const reportMetadata = service.getReportMetadata(formGroup) as any;

        expect(reportMetadata).toMatchObject({});
      });

      it('should return IReportMetadata', () => {
        const formGroup = service.createReportMetadataFormGroup(sampleWithRequiredData);

        const reportMetadata = service.getReportMetadata(formGroup) as any;

        expect(reportMetadata).toMatchObject(sampleWithRequiredData);
      });
    });
  });
});

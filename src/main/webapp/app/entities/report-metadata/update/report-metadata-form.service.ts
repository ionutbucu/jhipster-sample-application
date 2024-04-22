import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IReportMetadata, NewReportMetadata } from '../report-metadata.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { rid: unknown }> = Partial<Omit<T, 'rid'>> & { rid: T['rid'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportMetadata for edit and NewReportMetadataFormGroupInput for create.
 */
type ReportMetadataFormGroupInput = IReportMetadata | PartialWithRequiredKeyOf<NewReportMetadata>;

type ReportMetadataFormDefaults = Pick<NewReportMetadata, 'rid'>;

type ReportMetadataFormGroupContent = {
  rid: FormControl<IReportMetadata['rid'] | NewReportMetadata['rid']>;
  metadata: FormControl<IReportMetadata['metadata']>;
};

export type ReportMetadataFormGroup = FormGroup<ReportMetadataFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportMetadataFormService {
  createReportMetadataFormGroup(reportMetadata: ReportMetadataFormGroupInput = { rid: null }): ReportMetadataFormGroup {
    const reportMetadataRawValue = {
      ...this.getFormDefaults(),
      ...reportMetadata,
    };
    return new FormGroup<ReportMetadataFormGroupContent>({
      rid: new FormControl(
        { value: reportMetadataRawValue.rid, disabled: reportMetadataRawValue.rid !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      metadata: new FormControl(reportMetadataRawValue.metadata),
    });
  }

  getReportMetadata(form: ReportMetadataFormGroup): IReportMetadata | NewReportMetadata {
    return form.getRawValue() as IReportMetadata | NewReportMetadata;
  }

  resetForm(form: ReportMetadataFormGroup, reportMetadata: ReportMetadataFormGroupInput): void {
    const reportMetadataRawValue = { ...this.getFormDefaults(), ...reportMetadata };
    form.reset(
      {
        ...reportMetadataRawValue,
        rid: { value: reportMetadataRawValue.rid, disabled: reportMetadataRawValue.rid !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportMetadataFormDefaults {
    return {
      rid: null,
    };
  }
}

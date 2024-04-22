import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IReportColumnMapping, NewReportColumnMapping } from '../report-column-mapping.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { rid: unknown }> = Partial<Omit<T, 'rid'>> & { rid: T['rid'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportColumnMapping for edit and NewReportColumnMappingFormGroupInput for create.
 */
type ReportColumnMappingFormGroupInput = IReportColumnMapping | PartialWithRequiredKeyOf<NewReportColumnMapping>;

type ReportColumnMappingFormDefaults = Pick<NewReportColumnMapping, 'rid'>;

type ReportColumnMappingFormGroupContent = {
  rid: FormControl<IReportColumnMapping['rid'] | NewReportColumnMapping['rid']>;
  sourceColumnName: FormControl<IReportColumnMapping['sourceColumnName']>;
  sourceColumnIndex: FormControl<IReportColumnMapping['sourceColumnIndex']>;
  columnTitle: FormControl<IReportColumnMapping['columnTitle']>;
  lang: FormControl<IReportColumnMapping['lang']>;
  report: FormControl<IReportColumnMapping['report']>;
};

export type ReportColumnMappingFormGroup = FormGroup<ReportColumnMappingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportColumnMappingFormService {
  createReportColumnMappingFormGroup(reportColumnMapping: ReportColumnMappingFormGroupInput = { rid: null }): ReportColumnMappingFormGroup {
    const reportColumnMappingRawValue = {
      ...this.getFormDefaults(),
      ...reportColumnMapping,
    };
    return new FormGroup<ReportColumnMappingFormGroupContent>({
      rid: new FormControl(
        { value: reportColumnMappingRawValue.rid, disabled: reportColumnMappingRawValue.rid !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sourceColumnName: new FormControl(reportColumnMappingRawValue.sourceColumnName),
      sourceColumnIndex: new FormControl(reportColumnMappingRawValue.sourceColumnIndex),
      columnTitle: new FormControl(reportColumnMappingRawValue.columnTitle),
      lang: new FormControl(reportColumnMappingRawValue.lang),
      report: new FormControl(reportColumnMappingRawValue.report),
    });
  }

  getReportColumnMapping(form: ReportColumnMappingFormGroup): IReportColumnMapping | NewReportColumnMapping {
    return form.getRawValue() as IReportColumnMapping | NewReportColumnMapping;
  }

  resetForm(form: ReportColumnMappingFormGroup, reportColumnMapping: ReportColumnMappingFormGroupInput): void {
    const reportColumnMappingRawValue = { ...this.getFormDefaults(), ...reportColumnMapping };
    form.reset(
      {
        ...reportColumnMappingRawValue,
        rid: { value: reportColumnMappingRawValue.rid, disabled: reportColumnMappingRawValue.rid !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportColumnMappingFormDefaults {
    return {
      rid: null,
    };
  }
}

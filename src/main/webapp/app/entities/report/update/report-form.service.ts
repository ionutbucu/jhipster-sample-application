import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IReport, NewReport } from '../report.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { rid: unknown }> = Partial<Omit<T, 'rid'>> & { rid: T['rid'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReport for edit and NewReportFormGroupInput for create.
 */
type ReportFormGroupInput = IReport | PartialWithRequiredKeyOf<NewReport>;

type ReportFormDefaults = Pick<NewReport, 'rid'>;

type ReportFormGroupContent = {
  rid: FormControl<IReport['rid'] | NewReport['rid']>;
  cid: FormControl<IReport['cid']>;
  name: FormControl<IReport['name']>;
  description: FormControl<IReport['description']>;
  query: FormControl<IReport['query']>;
  queryType: FormControl<IReport['queryType']>;
  fileName: FormControl<IReport['fileName']>;
  reportType: FormControl<IReport['reportType']>;
  licenseHolder: FormControl<IReport['licenseHolder']>;
  owner: FormControl<IReport['owner']>;
  datasource: FormControl<IReport['datasource']>;
  metadata: FormControl<IReport['metadata']>;
};

export type ReportFormGroup = FormGroup<ReportFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportFormService {
  createReportFormGroup(report: ReportFormGroupInput = { rid: null }): ReportFormGroup {
    const reportRawValue = {
      ...this.getFormDefaults(),
      ...report,
    };
    return new FormGroup<ReportFormGroupContent>({
      rid: new FormControl(
        { value: reportRawValue.rid, disabled: reportRawValue.rid !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      cid: new FormControl(reportRawValue.cid),
      name: new FormControl(reportRawValue.name, {
        validators: [Validators.required, Validators.minLength(3)],
      }),
      description: new FormControl(reportRawValue.description, {
        validators: [Validators.maxLength(512)],
      }),
      query: new FormControl(reportRawValue.query, {
        validators: [Validators.required, Validators.minLength(10), Validators.maxLength(10000)],
      }),
      queryType: new FormControl(reportRawValue.queryType),
      fileName: new FormControl(reportRawValue.fileName, {
        validators: [Validators.required, Validators.minLength(3)],
      }),
      reportType: new FormControl(reportRawValue.reportType),
      licenseHolder: new FormControl(reportRawValue.licenseHolder),
      owner: new FormControl(reportRawValue.owner),
      datasource: new FormControl(reportRawValue.datasource),
      metadata: new FormControl(reportRawValue.metadata),
    });
  }

  getReport(form: ReportFormGroup): IReport | NewReport {
    return form.getRawValue() as IReport | NewReport;
  }

  resetForm(form: ReportFormGroup, report: ReportFormGroupInput): void {
    const reportRawValue = { ...this.getFormDefaults(), ...report };
    form.reset(
      {
        ...reportRawValue,
        rid: { value: reportRawValue.rid, disabled: reportRawValue.rid !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportFormDefaults {
    return {
      rid: null,
    };
  }
}

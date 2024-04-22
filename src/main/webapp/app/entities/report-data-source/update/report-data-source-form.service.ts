import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IReportDataSource, NewReportDataSource } from '../report-data-source.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { rid: unknown }> = Partial<Omit<T, 'rid'>> & { rid: T['rid'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportDataSource for edit and NewReportDataSourceFormGroupInput for create.
 */
type ReportDataSourceFormGroupInput = IReportDataSource | PartialWithRequiredKeyOf<NewReportDataSource>;

type ReportDataSourceFormDefaults = Pick<NewReportDataSource, 'rid'>;

type ReportDataSourceFormGroupContent = {
  rid: FormControl<IReportDataSource['rid'] | NewReportDataSource['rid']>;
  type: FormControl<IReportDataSource['type']>;
  url: FormControl<IReportDataSource['url']>;
  user: FormControl<IReportDataSource['user']>;
  password: FormControl<IReportDataSource['password']>;
};

export type ReportDataSourceFormGroup = FormGroup<ReportDataSourceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportDataSourceFormService {
  createReportDataSourceFormGroup(reportDataSource: ReportDataSourceFormGroupInput = { rid: null }): ReportDataSourceFormGroup {
    const reportDataSourceRawValue = {
      ...this.getFormDefaults(),
      ...reportDataSource,
    };
    return new FormGroup<ReportDataSourceFormGroupContent>({
      rid: new FormControl(
        { value: reportDataSourceRawValue.rid, disabled: reportDataSourceRawValue.rid !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(reportDataSourceRawValue.type),
      url: new FormControl(reportDataSourceRawValue.url),
      user: new FormControl(reportDataSourceRawValue.user),
      password: new FormControl(reportDataSourceRawValue.password),
    });
  }

  getReportDataSource(form: ReportDataSourceFormGroup): IReportDataSource | NewReportDataSource {
    return form.getRawValue() as IReportDataSource | NewReportDataSource;
  }

  resetForm(form: ReportDataSourceFormGroup, reportDataSource: ReportDataSourceFormGroupInput): void {
    const reportDataSourceRawValue = { ...this.getFormDefaults(), ...reportDataSource };
    form.reset(
      {
        ...reportDataSourceRawValue,
        rid: { value: reportDataSourceRawValue.rid, disabled: reportDataSourceRawValue.rid !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportDataSourceFormDefaults {
    return {
      rid: null,
    };
  }
}

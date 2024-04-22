import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReportExecution, NewReportExecution } from '../report-execution.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { rid: unknown }> = Partial<Omit<T, 'rid'>> & { rid: T['rid'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportExecution for edit and NewReportExecutionFormGroupInput for create.
 */
type ReportExecutionFormGroupInput = IReportExecution | PartialWithRequiredKeyOf<NewReportExecution>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReportExecution | NewReportExecution> = Omit<T, 'date'> & {
  date?: string | null;
};

type ReportExecutionFormRawValue = FormValueOf<IReportExecution>;

type NewReportExecutionFormRawValue = FormValueOf<NewReportExecution>;

type ReportExecutionFormDefaults = Pick<NewReportExecution, 'rid' | 'date'>;

type ReportExecutionFormGroupContent = {
  rid: FormControl<ReportExecutionFormRawValue['rid'] | NewReportExecution['rid']>;
  date: FormControl<ReportExecutionFormRawValue['date']>;
  error: FormControl<ReportExecutionFormRawValue['error']>;
  url: FormControl<ReportExecutionFormRawValue['url']>;
  user: FormControl<ReportExecutionFormRawValue['user']>;
  additionalInfo: FormControl<ReportExecutionFormRawValue['additionalInfo']>;
  report: FormControl<ReportExecutionFormRawValue['report']>;
};

export type ReportExecutionFormGroup = FormGroup<ReportExecutionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportExecutionFormService {
  createReportExecutionFormGroup(reportExecution: ReportExecutionFormGroupInput = { rid: null }): ReportExecutionFormGroup {
    const reportExecutionRawValue = this.convertReportExecutionToReportExecutionRawValue({
      ...this.getFormDefaults(),
      ...reportExecution,
    });
    return new FormGroup<ReportExecutionFormGroupContent>({
      rid: new FormControl(
        { value: reportExecutionRawValue.rid, disabled: reportExecutionRawValue.rid !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      date: new FormControl(reportExecutionRawValue.date, {
        validators: [Validators.required],
      }),
      error: new FormControl(reportExecutionRawValue.error, {
        validators: [Validators.maxLength(256)],
      }),
      url: new FormControl(reportExecutionRawValue.url),
      user: new FormControl(reportExecutionRawValue.user),
      additionalInfo: new FormControl(reportExecutionRawValue.additionalInfo),
      report: new FormControl(reportExecutionRawValue.report),
    });
  }

  getReportExecution(form: ReportExecutionFormGroup): IReportExecution | NewReportExecution {
    return this.convertReportExecutionRawValueToReportExecution(
      form.getRawValue() as ReportExecutionFormRawValue | NewReportExecutionFormRawValue,
    );
  }

  resetForm(form: ReportExecutionFormGroup, reportExecution: ReportExecutionFormGroupInput): void {
    const reportExecutionRawValue = this.convertReportExecutionToReportExecutionRawValue({ ...this.getFormDefaults(), ...reportExecution });
    form.reset(
      {
        ...reportExecutionRawValue,
        rid: { value: reportExecutionRawValue.rid, disabled: reportExecutionRawValue.rid !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportExecutionFormDefaults {
    const currentTime = dayjs();

    return {
      rid: null,
      date: currentTime,
    };
  }

  private convertReportExecutionRawValueToReportExecution(
    rawReportExecution: ReportExecutionFormRawValue | NewReportExecutionFormRawValue,
  ): IReportExecution | NewReportExecution {
    return {
      ...rawReportExecution,
      date: dayjs(rawReportExecution.date, DATE_TIME_FORMAT),
    };
  }

  private convertReportExecutionToReportExecutionRawValue(
    reportExecution: IReportExecution | (Partial<NewReportExecution> & ReportExecutionFormDefaults),
  ): ReportExecutionFormRawValue | PartialWithRequiredKeyOf<NewReportExecutionFormRawValue> {
    return {
      ...reportExecution,
      date: reportExecution.date ? reportExecution.date.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}

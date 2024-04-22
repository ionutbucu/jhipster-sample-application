import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IReportSchedule, NewReportSchedule } from '../report-schedule.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { rid: unknown }> = Partial<Omit<T, 'rid'>> & { rid: T['rid'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportSchedule for edit and NewReportScheduleFormGroupInput for create.
 */
type ReportScheduleFormGroupInput = IReportSchedule | PartialWithRequiredKeyOf<NewReportSchedule>;

type ReportScheduleFormDefaults = Pick<NewReportSchedule, 'rid'>;

type ReportScheduleFormGroupContent = {
  rid: FormControl<IReportSchedule['rid'] | NewReportSchedule['rid']>;
  cron: FormControl<IReportSchedule['cron']>;
  report: FormControl<IReportSchedule['report']>;
};

export type ReportScheduleFormGroup = FormGroup<ReportScheduleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportScheduleFormService {
  createReportScheduleFormGroup(reportSchedule: ReportScheduleFormGroupInput = { rid: null }): ReportScheduleFormGroup {
    const reportScheduleRawValue = {
      ...this.getFormDefaults(),
      ...reportSchedule,
    };
    return new FormGroup<ReportScheduleFormGroupContent>({
      rid: new FormControl(
        { value: reportScheduleRawValue.rid, disabled: reportScheduleRawValue.rid !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      cron: new FormControl(reportScheduleRawValue.cron, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      report: new FormControl(reportScheduleRawValue.report),
    });
  }

  getReportSchedule(form: ReportScheduleFormGroup): IReportSchedule | NewReportSchedule {
    return form.getRawValue() as IReportSchedule | NewReportSchedule;
  }

  resetForm(form: ReportScheduleFormGroup, reportSchedule: ReportScheduleFormGroupInput): void {
    const reportScheduleRawValue = { ...this.getFormDefaults(), ...reportSchedule };
    form.reset(
      {
        ...reportScheduleRawValue,
        rid: { value: reportScheduleRawValue.rid, disabled: reportScheduleRawValue.rid !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportScheduleFormDefaults {
    return {
      rid: null,
    };
  }
}
